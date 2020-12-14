package com.geyao.barbershop.utils;

import com.geyao.barbershop.constants.Constant;
import com.geyao.barbershop.security.JwtUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    private final String secret = "barbershopSecret";

    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + Constant.JWT_TIMEOUT);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            claims = null;
        }
        return claims;
    }

    public String generateToken(String mobile) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", mobile);
        claims.put("created", new Date());
        return generateToken(claims);
    }

    public String getMobileFromToken(String token) {
        String mobile;
        try {
            Claims claims = getClaimsFromToken(token);
            mobile = claims.getSubject();
        } catch (Exception e) {
            mobile = null;
        }
        return mobile;
    }

    /**
     * 判断token是否过期：
     *
     * @param token
     * @return 过期了-true；没过期-false
     */
    public boolean isExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if(claims == null){
                System.out.println("token past long long time! claims is null !!!");
                return false;
            }
            Date expiration = claims.getExpiration();
            System.out.println("token past time：" + DateUtils.dateToStr(expiration));
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExpireSoon(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        return expiration.before(calendar.getTime());
    }

    public String refreshToken(String token) {
        String refreshToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshToken = generateToken(claims);
        } catch (Exception e) {
            refreshToken = null;
        }
        return refreshToken;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) userDetails;
        String mobile = getMobileFromToken(token);
        return mobile.equals(jwtUserDetails.getUsername());
    }
}
