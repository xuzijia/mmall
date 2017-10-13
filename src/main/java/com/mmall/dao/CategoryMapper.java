package com.mmall.dao;

import com.mmall.pojo.Category;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectCategoryChildrenByParentId(Integer parentId);
    
    int checkCategoryName(@Param("parentId")Integer parentId,@Param("name")String categoryName);
    
    int checkParentId(Integer parentId);

	int checkCategoryNameById(@Param("id")Integer id, @Param("parentId")Integer parentId, @Param("name")String name);
    
  
}