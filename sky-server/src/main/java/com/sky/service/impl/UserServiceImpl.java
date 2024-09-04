package com.sky.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sky.utils.HttpClientUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service

public class UserServiceImpl implements UserService {
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
    @Autowired
    private WeChatProperties wechatProperties;
    @Autowired
    private UserMapper userMapper;
    @Override
    public User wxlogin(UserLoginDTO user) {
        String openid = getOpenId(user.getCode());
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
//        String openid = "123456";
        User user1 = userMapper.getByOpenId(openid);
        if(user1==null) {
            user1 = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user1);
            user1 = userMapper.getByOpenId(openid);
        }
        return user1;
    }

    @Override
    public String getOpenId(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", wechatProperties.getAppid());
        params.put("secret", wechatProperties.getSecret());
        params.put("grant_type", "authorization_code");
        params.put("js_code", code);

        // 调用微信接口服务获取openid
        JSONObject json = JSONObject.parseObject(HttpClientUtil.doGet(WX_LOGIN_URL, params));
        String openid = json.getString("openid");
        return openid;
    }
}
