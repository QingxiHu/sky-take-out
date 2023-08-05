package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 16:24
 */

@Api(tags = "菜品接口")
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品
     */
    @PostMapping
    @ApiOperation("添加菜品")
    public Result<String> add(@RequestBody DishDTO dishDTO) {

        dishService.saveWithFlavors(dishDTO);

        return Result.success("添加成功");
    }


    /**
     * 分页查询
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        int page = dishPageQueryDTO.getPage();
        int pageSize = dishPageQueryDTO.getPageSize();
        Integer categoryId = dishPageQueryDTO.getCategoryId();
        Integer status = dishPageQueryDTO.getStatus();
        String name = dishPageQueryDTO.getName();

        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        LambdaQueryWrapper<DishVO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(DishVO::getName, name);
        wrapper.eq(DishVO::getStatus, status);
        wrapper.eq(DishVO::getCategoryId, categoryId);

        dishService.page(pageInfo);

        Page<DishVO> dishVOPage = new Page<>();
        // 对象拷贝dish分页对象到前端需要的对象dishVo中，忽视dishVo中的records属性
        BeanUtils.copyProperties(pageInfo, dishVOPage, "records");

        // 将dishVo.records中的categoryName进行赋值
        List<DishVO> list = pageInfo.getRecords().stream().map(dish -> {
            DishVO dishVO = new DishVO();
            Long dishCategoryId = dish.getCategoryId();
            Category category = categoryService.getById(dishCategoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishVO.setCategoryName(categoryName);
            }
            BeanUtils.copyProperties(dish, dishVO);
            return dishVO;
        }).collect(Collectors.toList());
        dishVOPage.setRecords(list);


        PageResult pageResult = new PageResult(dishVOPage.getTotal(), dishVOPage.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {

        dishService.removeByIds(ids);

        return Result.success("删除成功");
    }

    /**
     * 根据Id查询菜品以及菜品口味
     * @param id
     * @return
     */
    @ApiOperation("根据Id查询菜品以及菜品口味")
    @GetMapping("/{id}")
    public Result<DishVO> selectById(@PathVariable Long id) {

        DishVO dishVO = dishService.getWithFlavor(id);

        return Result.success(dishVO);
    }

    /**
     * 修改菜品及其口味
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品及其口味")
    public Result<Object> update(@RequestBody DishDTO dishDTO) {

        dishService.updateWithFlavor(dishDTO);

        return Result.success();
    }
}
