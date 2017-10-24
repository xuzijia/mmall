package com.mmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.CategoryService;
import com.mmall.service.ProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

/**   
*    
* 项目名称：mmall   
* 类名称：ProductServiceImpl   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月16日 上午9:00:01   
* 修改人：xuzijia   
* 修改时间：2017年10月16日 上午9:00:01   
* 修改备注：   
* @version 1.0
*    
*/
@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private CategoryService categoryService;

	public ServerResponse<String> saveOrUpdateProduct(Product product) {
		if (product != null) {
			if (StringUtils.isNotBlank(product.getSubImages())) {
				String[] arr = product.getSubImages().split(",");
				if (arr.length > 0) {
					product.setMainImage(arr[0]);
				}
			}
			// 新增
			if (product.getId() == null) {
				int insert = productMapper.insert(product);
				if (insert > 0) {
					return ServerResponse.createBySuccessMessage("新增产品成功");
				}
				return ServerResponse.createByErrorMessage("新增产品失败");

			} else {
				int i = productMapper.updateByPrimaryKeySelective(product);
				if (i > 0) {
					return ServerResponse.createBySuccessMessage("更新产品成功");
				}
				return ServerResponse.createByErrorMessage("更新产品失败");
			}

		}
		return ServerResponse.createByErrorMessage("新增或者更新产品参数不正确");
	}

	/**
	 * 商品上下架
	 */
	public ServerResponse<String> setStatus(Integer productId, Integer status) {
		if (productId == null || status == null) {
			ServerResponse.createByErrorMessage("参数错误");
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int i = productMapper.updateByPrimaryKeySelective(product);
		if (i > 0) {
			return ServerResponse.createBySuccessMessage("更新状态成功");
		}
		return ServerResponse.createByErrorMessage("更新状态失败");
	}

	public ServerResponse<ProductDetailVo> findProductById(Integer productId,
			boolean flag) {
		if (productId == null) {
			ServerResponse.createByErrorMessage("参数错误");
		}

		Product product = productMapper.selectByPrimaryKey(productId, flag);

		if (product != null) {
			ProductDetailVo productDetailVo = assembleProductDetailVo(product);
			return ServerResponse.createBySuccess("查询成功", productDetailVo);
		}
		return ServerResponse.createByErrorMessage("商品不存在或者商品下架");
	}

	/**
	 * 获取商品列表
	 */
	public ServerResponse<PageInfo> getList(int pageNum, int pageSize) {
		// startPage--start
		// 填充自己的sql查询逻辑
		// pageHelper-收尾
		PageHelper.startPage(pageNum, pageSize);
		List<Product> list = productMapper.getList();
		List<ProductListVo> listVo = new ArrayList<ProductListVo>();
		if (list.size() > 0) {
			for (Product p : list) {
				ProductListVo productListVo = assembleProductListVo(p);
				listVo.add(productListVo);
			}
			PageInfo pageInfo = new PageInfo(list);
			pageInfo.setList(listVo);
			return ServerResponse.createBySuccess(pageInfo);
		}
		return ServerResponse.createByErrorMessage("列表为空");

	}

	/**
	 * 后台商品搜索
	 */
	public ServerResponse<PageInfo> searchProduct(int pageNum, int pageSize,
			String searchKey, Integer categoryId) {
		PageHelper.startPage(pageNum, pageSize);
		if (StringUtils.isNoneBlank(searchKey)) {
			searchKey = new StringBuffer().append("%").append(searchKey)
					.append("%").toString();
		}
		List<Product> list = productMapper.searchProduct(searchKey, categoryId);
		List<ProductListVo> listVo = new ArrayList<ProductListVo>();
		if (list.size() > 0) {
			for (Product p : list) {
				ProductListVo productListVo = assembleProductListVo(p);
				listVo.add(productListVo);
			}
			PageInfo pageInfo = new PageInfo(list);
			pageInfo.setList(listVo);
			return ServerResponse.createBySuccess(pageInfo);
		}
		return ServerResponse.createByErrorMessage("搜索不到商品...");

		// return ServerResponse.createByErrorMessage("请给出搜索条件");
	}

	/**
	 * 封装list vo对象
	 */
	private ProductListVo assembleProductListVo(Product product) {
		ProductListVo productListVo = new ProductListVo();
		productListVo.setId(product.getId());
		productListVo.setName(product.getName());
		productListVo.setCategoryId(product.getCategoryId());
		productListVo.setImageHost(PropertiesUtil.getProperty(
				"ftp.server.http.prefix", "http://img.happymmall.com/"));
		productListVo.setMainImage(product.getMainImage());
		productListVo.setPrice(product.getPrice());
		productListVo.setSubtitle(product.getSubtitle());
		productListVo.setStatus(product.getStatus());
		return productListVo;
	}

	/**
	 * 
	 * @Title: assembleProductDetailVo
	 * @Description: 封装detail vo对象
	 * @param: @param product
	 * @param: @return     
	 * @return: ProductDetailVo     
	 * @author:  xuzijia
	 * @date: 2017年10月16日 上午11:34:57
	 * @throws
	 */
	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setSubtitle(product.getSubtitle());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setSubImages(product.getSubImages());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setName(product.getName());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setStock(product.getStock());

		productDetailVo.setImageHost(PropertiesUtil.getProperty(
				"ftp.server.http.prefix", "http://img.happymmall.com/"));

		Category category = categoryMapper.selectByPrimaryKey(product
				.getCategoryId());
		if (category == null) {
			productDetailVo.setParentCategoryId(0);// 默认根节点
		} else {
			productDetailVo.setParentCategoryId(category.getParentId());

		}

		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product
				.getCreateTime()));
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product
				.getUpdateTime()));
		return productDetailVo;
	}

	public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,
			Integer categoryId, int pageNum, int pageSize, String orderBy) {
		if (StringUtils.isBlank(keyword) && categoryId == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getMsg());
		}
		List<Integer> categoryIdList = new ArrayList<Integer>();

		if (categoryId != null) {
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if (category == null && StringUtils.isBlank(keyword)) {
				// 没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
				PageHelper.startPage(pageNum, pageSize);
				List<ProductListVo> productListVoList = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(productListVoList);
				return ServerResponse.createBySuccess(pageInfo);
			}
			categoryIdList = categoryService.selectCategoryAndChildrenById(
					category.getId()).getData();
		}
		if (StringUtils.isNotBlank(keyword)) {
			keyword = new StringBuilder().append("%").append(keyword)
					.append("%").toString();
		}

		PageHelper.startPage(pageNum, pageSize);
		// 排序处理
		if (StringUtils.isNotBlank(orderBy)) {
			if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
				String[] orderByArray = orderBy.split("_");
				PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
			}
		}
		List<Product> productList = productMapper.selectByNameAndCategoryIds(
				StringUtils.isBlank(keyword) ? null : keyword,
				categoryIdList.size() == 0 ? null : categoryIdList);

		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVo(product);
			productListVoList.add(productListVo);
		}

		PageInfo pageInfo = new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServerResponse.createBySuccess(pageInfo);
	}

}
