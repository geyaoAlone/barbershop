package com.geyao.barbershop.user.web;

import com.alibaba.fastjson.JSONObject;
import com.geyao.barbershop.common.ResultVo;
import com.geyao.barbershop.user.pojo.User;
import com.geyao.barbershop.user.service.UserService;
import com.geyao.barbershop.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @Resource
    UserService service;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @GetMapping("/sendCode/{mobile}")
    public ResultVo sendCode(@PathVariable("mobile") String mobile){
        //发短信
        return new ResultVo(service.sendValidateCode(mobile));
    }

    @PostMapping("/login")
    public ResultVo loginUser(@RequestBody User user){

        if(StringUtils.isEmpty(user.getValidateCode())){
            return new ResultVo("验证码不能为空");
        }

        //校验验证码
        String res = service.checkValidateCode(user.getMobile(),user.getValidateCode());
        LOG.info("check validateCode result：{}",res);
        if(!"".equals(res)){
            return new ResultVo(res);
        }

        String token = jwtTokenUtil.generateToken(user.getMobile());
        if(token == null){
            return new ResultVo("登陆失败！授权失败");
        }
        JSONObject data = new JSONObject();
        data.put("token",token);
        User u = service.queryUser(user.getMobile());
        if(!Objects.isNull(u)){
            data.put("user",u);
            return new ResultVo(data);
        }else {
            if (service.saveUser(user)) {
                data.put("isNew", "1");
                data.put("user", service.queryUser(user.getMobile()));
                return new ResultVo(data);

            } else {
                return new ResultVo("登陆失败");
            }
        }
    }

    /**
     * 检查token
     * @return
     */
    @GetMapping("/checkToken")
    public ResultVo checkToken(String token){
        if(jwtTokenUtil.isExpired(token)){
            return new ResultVo(true);
        }else{
            return new ResultVo("token过期");
        }
    }
}
