package com.mmall.service;

import java.util.List;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

public interface CategoryService {
	 ServerResponse<String> addCategory(String categoryName,Integer parentId);
	 ServerResponse<String> updateCategory(Category category);
	 ServerResponse<List<Category>> getCategory(Integer parentId);
}
