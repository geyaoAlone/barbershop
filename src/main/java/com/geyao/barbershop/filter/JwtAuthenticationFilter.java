package com.geyao.barbershop.filter;

import com.geyao.barbershop.constants.Constant;
import com.geyao.barbershop.dao.RedisDao;
import com.geyao.barbershop.security.JwtUserDetailService;
import com.geyao.barbershop.user.web.UserController;
import com.geyao.barbershop.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * token校验过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.info("request url [{}]",request.getRequestURI());
        String header = request.getHeader(Constant.JWT_HEADER);
        if (header != null && header.startsWith(Constant.JWT_PREFIX)) {
            String token = header.replace(Constant.JWT_PREFIX, "");
            String mobile = jwtTokenUtil.getMobileFromToken(token);
            if (mobile != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = jwtUserDetailService.loadUserByUsername(mobile);
                boolean isValidate = jwtTokenUtil.validateToken(token, userDetails);
                boolean isExpired = jwtTokenUtil.isExpired(token);
                boolean canPass = false;

                if (isValidate && !isExpired) {
                    canPass = true;

                    //token一天之内要过期，刷新token
                    if (jwtTokenUtil.isExpireSoon(token)) {
                        System.out.println("token past coming soon !!!");
                        String newToken = jwtTokenUtil.refreshToken(token);

                        //旧token放入黑名单,保留一分钟，解决并发过程中新token刷新同时，旧token请求失效的情况
                        redisDao.set(token, newToken, Constant.REDIS_TOKEN_TIMEOUT);

                        //response中返回新token
                        response.setHeader(Constant.JWT_HEADER, newToken);
                    }
                } else if (isValidate) {
                    //如果token在黑名单中，说明该token已经被并发请求刷新，并且已经返回了新token，但该token一分钟之内就会失效
                    Object newToken = redisDao.get(token);
                    if (!Objects.isNull(newToken)) {
                        canPass = true;

                        //response中返回新token
                        response.setHeader(Constant.JWT_HEADER, Constant.JWT_PREFIX + newToken);
                    }
                }

                if (canPass) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,null);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
