package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-05 16:47
 */

@RestController("UserShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "商店状态接口")
public class ShopController {

    public static final String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺营业状态
     * @param
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Object> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(key);
        log.info("获取到当前营业状态: {}", status);
        return Result.success(status);
    }


}