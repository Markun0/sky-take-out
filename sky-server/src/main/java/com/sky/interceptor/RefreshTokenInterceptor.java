package com.sky.interceptor;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.properties.RedisProperties;
import com.sky.utils.JwtUtil;
import com.sky.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private RedisUtil redisUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());
        if(StrUtil.isBlank(token)){
            return true;
        }
        //2、token自动刷新有效期
        Long id = (Long) redisUtil.get(token);
        if(id == null){
            return true;
        }
        redisUtil.expire(token,redisProperties.getLOGIN_TTL());
        return true;
    }
}
