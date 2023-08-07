package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 * @Description
 * @create 2023-08-07 12:47
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
