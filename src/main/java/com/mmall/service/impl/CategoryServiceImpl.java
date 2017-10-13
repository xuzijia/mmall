package com.mmall.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.CategoryService;

/**   
*    
* 项目名称：mmall   
* 类名称：CategoryServiceImpl   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月13日 上午9:54:14   
* 修改人：xuzijia   
* 修改时间：2017年10月13日 上午9:54:14   
* 修改备注：   
* @version 1.0
*    
*/
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;

	/**
	 * 
	 * @Title: addCategory
	 * @Description: 添加分类
	 * @param: @param categoryName
	 * @param: @param parentId
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月13日 上午10:06:38
	 * @throws
	 */
	public ServerResponse<String> addCategory(String categoryName,
			Integer parentId) {
		if (StringUtils.isBlank(categoryName)) {
			return ServerResponse.createByErrorMessage("请输入分类名称");
		}
		// 校验该父节点下的是否存在一样的分类名称
		int i = categoryMapper.checkCategoryName(parentId, categoryName);
		if (i > 0) {
			return ServerResponse.createByErrorMessage("该分类名称已存在");
		}
		// 校验是否存在该上级节点 排除根节点
		if (parentId != 0) {
			int checkParentId = categoryMapper.checkParentId(parentId);
			if (checkParentId == 0) {
				return ServerResponse.createByErrorMessage("上级分类不存在");
			}
		}
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);
		int insert = categoryMapper.insert(category);
		if (insert > 0) {
			return ServerResponse.createBySuccessMessage("添加分类成功");
		}
		return ServerResponse.createByErrorMessage("添加分类失败");

	}

	/**
	 * 查询父节点下的子分类
	 */
	public ServerResponse<List<Category>> getCategory(Integer parentId) {
		List<Category> list = categoryMapper
				.selectCategoryChildrenByParentId(parentId);
		if (list.size() == 0) {
			return ServerResponse.createBySuccessMessage("该分类暂时没有子分类 或者该分类不存在");
		}
		return ServerResponse.createBySuccess("查询成功", list);
	}

	/**
	 * 修改分类
	 */
	public ServerResponse<String> updateCategory(Category category) {
		if (StringUtils.isBlank(category.getName())
				|| category.getParentId() == null) {
			return ServerResponse.createByErrorMessage("缺少参数或参数为空");
		}

		// 校验名称
		int i = categoryMapper.checkCategoryNameById(category.getId(),category.getParentId(), category.getName());
		if (i > 0) {
			return ServerResponse.createByErrorMessage("该分类名称已存在");
		}
		
		// 校验是否存在该上级节点 排除根节点
		if (category.getParentId() != 0) {
			int checkParentId = categoryMapper.checkParentId(category.getParentId() );
			if (checkParentId == 0) {
				return ServerResponse.createByErrorMessage("上级分类不存在");
			}
		}

		int i2 = categoryMapper.updateByPrimaryKeySelective(category);
		if (i2 > 0) {
			return ServerResponse.createBySuccessMessage("修改成功");
		}

		return ServerResponse.createByErrorMessage("修改失败");
	}

}
