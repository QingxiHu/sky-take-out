package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-08 19:59
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    void sub(ShoppingCartDTO shoppingCartDTO);
}
