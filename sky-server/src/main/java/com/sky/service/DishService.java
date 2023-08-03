package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 12:05
 */
public interface DishService extends IService<Dish> {

    /**
     * 添加菜品
     */
    void saveWithFlavors(DishDTO dishDTO);
}
