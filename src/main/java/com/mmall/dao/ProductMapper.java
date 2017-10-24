package com.mmall.dao;

import com.mmall.pojo.Product;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(@Param("id")Integer id, @Param("flag")boolean flag);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getList();
    
    List<Product> searchProduct(@Param("searchKey")String searchKey,@Param("categoryId")Integer categoryId);

	List<Product> selectByNameAndCategoryIds(@Param("productName")String productName, @Param("categoryIdList")List<Integer> list);
    

}