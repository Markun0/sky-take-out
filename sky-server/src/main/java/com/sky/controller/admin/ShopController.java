package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺管理")
@Slf4j
public class ShopController {
    @Autowired
    private RedisUtil redisUtil;
    private String SHOP_STATUS = "SHOP_STATUS";

    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态为: {}", status);
//        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        redisUtil.set(SHOP_STATUS, status);
        return Result.success(status);
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result getStatus(){
//        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        Integer status = (Integer) redisUtil.get(SHOP_STATUS);
        log.info("获取店铺营业状态为: {}", status);
        return Result.success(status);
    }
}
