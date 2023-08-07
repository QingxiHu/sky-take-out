package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-07 12:48
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @return
     */
    User getUser(UserLoginDTO userLoginDTO);
}
