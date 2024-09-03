package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.utils.RedisUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static io.jsonwebtoken.Jwts.claims;

@RequestMapping("/user/user")
@RestController
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;
    //  登陆
    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public Result login(@RequestBody UserLoginDTO userDTO){
        System.out.println("用户登陆");
        User user = userService.wxlogin(userDTO);
        // jwt
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        redisUtil.set(token,user.getId());
        UserLoginVO userLoginVO = UserLoginVO.builder()
               .id(user.getId())
                .openid(user.getName())
                .token(token)
               .build();
        return Result.success(userLoginVO);
    }
}
