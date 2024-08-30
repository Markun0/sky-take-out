package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    //根据菜品Id查询套餐ID
    List<Long> getSetmealIdByDishIds(List<Long> dishIds);
}
