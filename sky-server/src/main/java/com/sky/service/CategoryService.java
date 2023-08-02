package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Category;
import io.swagger.models.auth.In;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-02 10:14
 */
public interface CategoryService extends IService<Category> {

    void removeById(Long id);
}
