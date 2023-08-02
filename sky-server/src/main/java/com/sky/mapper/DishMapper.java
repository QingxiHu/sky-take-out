package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 10:16
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
