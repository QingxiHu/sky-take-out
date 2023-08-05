package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;

import java.util.List;

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

    /**
     * 删除菜品
     */
    void removeByIds(List<Long> ids);

    /**
     * 根据Id获取菜品及口味
     */
    DishVO getWithFlavor(long id);

    /**
     * 修改菜品及其口味
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
