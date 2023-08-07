package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.service.SetmealDishService;
import com.sky.vo.DishVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 12:06
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishMapper dishMapper;

    @Transactional
    @Override
    public void saveWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        super.save(dish);

        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(flavor -> flavor.setDishId(dishId));

        if (!flavors.isEmpty()) {
            dishFlavorService.saveBatch(flavors);
        }

    }

    /**
     * 删除菜品
     */
    @Transactional
    @Override
    public void removeByIds(List<Long> ids) {

        // 获取list中每一个id值
        ids.forEach(id -> {
            // 根据id获取dish对象
            Dish dish = this.getById(id);
            Integer status = dish.getStatus();
            // 查询售卖状态
            if (Objects.equals(status, StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }

            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getDishId, id);

            List<SetmealDish> list = setmealDishService.list(wrapper);
            if (list != null && !list.isEmpty()) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }

            // 删除菜品中的口味
            dishFlavorService.removeById(id);
        });

        super.removeByIds(ids);
    }

    /**
     * 查菜品和口味
     * @param id
     * @return
     */
    @Override
    public DishVO getWithFlavor(long id) {
        Dish dish = super.getById(id);

        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);

        // 口味集合
        List<DishFlavor> list = dishFlavorService.list(wrapper);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(list);
        return dishVO;
    }

    /**
     * 修改菜品及其口味
     * @param dishDTO
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        super.updateById(dish);

        // 口味数据修改
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishFlavorService.remove(null);
        flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        wrapper.eq(Dish::getStatus, dish.getStatus());
        List<Dish> dishList = super.list(wrapper);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
            dishFlavorWrapper.eq(DishFlavor::getDishId, d.getId());
            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorWrapper);

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
