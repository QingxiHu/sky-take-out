package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 10:14
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> save(@RequestBody Category category){

        log.info("新增分类：{}", category);
        //分类状态默认为禁用状态0
        category.setStatus(StatusConstant.DISABLE);
        categoryService.save(category);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询：{}", categoryPageQueryDTO);

        int page = categoryPageQueryDTO.getPage();
        int pageSize = categoryPageQueryDTO.getPageSize();
        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), Category::getName, name);
        wrapper.eq(Objects.nonNull(type), Category::getType, type);
        wrapper.orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);

        Page<Category> categoryPage = new Page<>(page, pageSize);
        categoryService.page(categoryPage, wrapper);

        PageResult pageResult = new PageResult(categoryPage.getTotal(), categoryPage.getRecords());


        return Result.success(pageResult);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<String> deleteById(Long id){
        log.info("删除分类：{}", id);
        categoryService.removeById(id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){

        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();
        categoryService.updateById(category);

        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        // todo 前端无法找到对应的http接口请求

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType, type);
        wrapper.orderByAsc(Category::getSort)
                .orderByDesc(Category::getCreateTime);

        List<Category> list = categoryService.list(wrapper);

        return Result.success(list);
    }
}
