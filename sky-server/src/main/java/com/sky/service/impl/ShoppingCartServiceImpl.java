package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-08 20:00
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        // todo 前端无法传递口味数据
        String dishFlavor = shoppingCartDTO.getDishFlavor();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        // 判断当前购物车是否存在同类商品
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        wrapper.eq(dishId!=null, ShoppingCart::getDishId, dishId);
        wrapper.eq(setmealId!=null, ShoppingCart::getSetmealId, setmealId);
        wrapper.eq(dishFlavor!=null, ShoppingCart::getDishFlavor, dishFlavor);
        List<ShoppingCart> list = super.list(wrapper);

        if (list != null && !list.isEmpty()) {
            // 存在则数量加一
            shoppingCart = list.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            super.updateById(shoppingCart);
        } else {
            // 不存在则插入新数据
            // 插入菜品，反正老师就是说套餐菜品不能同时添加，我也不知道为啥
            if (dishId != null) {
                Dish dish = dishService.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 插入套餐
                Setmeal setmeal = setmealService.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());

            // 插入最终数据
            super.save(shoppingCart);
        }
    }
}
