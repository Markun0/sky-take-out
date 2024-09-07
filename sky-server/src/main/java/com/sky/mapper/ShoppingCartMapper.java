package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    ShoppingCart get(ShoppingCart shoppingCart);

    @Insert("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart myShoppingCart);

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time, number)"
            + "values(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, #{createTime}, #{number})")
    void insert(ShoppingCart myShoppingCart);

    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> list(Long userId);

    @Update("update shopping_cart set number = number - 1 where id = #{id}")
    void sub(Long id);

    @Delete("delete from shopping_cart where id = #{id}")
    void del(Long id);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(Long userId);
}
