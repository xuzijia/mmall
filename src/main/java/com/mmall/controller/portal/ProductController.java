package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ProductService;
import com.mmall.vo.ProductDetailVo;

/**   
*    
* 项目名称：mmall   
* 类名称：ProductController   
* 类描述：前台商品
* 创建人：xuzijia   
* 创建时间：2017年10月18日 上午8:55:34   
* 修改人：xuzijia   
* 修改时间：2017年10月18日 上午8:55:34   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	/**
	 * 
	 * @Title: getDetail
	 * @Description: 前台商品详情
	 * @param: @param productId
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<ProductDetailVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月18日 上午9:09:56
	 * @throws
	 */
	@RequestMapping("/detail.do")
	@ResponseBody
	public ServerResponse<ProductDetailVo> getDetail(Integer productId) {
		ServerResponse<ProductDetailVo> response = productService
				.findProductById(productId,true);
		return response;
	}

	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<PageInfo> list(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
		return productService.getProductByKeywordCategory(keyword, categoryId,
				pageNum, pageSize, orderBy);
	}

}
