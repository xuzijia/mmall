package com.mmall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.FileService;
import com.mmall.service.ProductService;
import com.mmall.service.UserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;

/**   
*    
* 项目名称：mmall   
* 类名称：ProductController   
* 类描述：后台商品Controller
* 创建人：xuzijia   
* 创建时间：2017年10月16日 上午8:57:17   
* 修改人：xuzijia   
* 修改时间：2017年10月16日 上午8:57:17   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private FileService fileService;

	/**
	 * 
	 * @Title: save
	 * @Description: 后台商品新增或者更新
	 * @param: @param product
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<String>     
	 * @author:  xuzijia
	 * @date: 2017年10月16日 上午9:22:56
	 * @throws
	 */
	@RequestMapping(value = "save.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> save(Product product, HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getMsg());
		}
		if (userService.checkAdminRole(user).isSuccess()) {
			ServerResponse<String> response = productService
					.saveOrUpdateProduct(product);
			return response;
		}
		return ServerResponse.createByErrorMessage("管理员权限不足");
	}

	/**
	 * 
	 * @Title: setStatus
	 * @Description: 商品上架或者下架
	 * @param: @param productId
	 * @param: @param status
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<String>     
	 * @author:  xuzijia
	 * @date: 2017年10月16日 下午3:10:07
	 * @throws
	 */
	@RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> setStatus(Integer productId, Integer status,
			HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getMsg());
		}
		if (userService.checkAdminRole(user).isSuccess()) {
			ServerResponse<String> response = productService.setStatus(
					productId, status);
			return response;
		}
		return ServerResponse.createByErrorMessage("管理员权限不足");
	}

	/**
	 * 
	 * @Title: detail
	 * @Description: 商品详情
	 * @param: @param session
	 * @param: @param productId
	 * @param: @return     
	 * @return: ServerResponse<ProductDetailVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月16日 下午3:10:27
	 * @throws
	 */
	@RequestMapping(value = "detail.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<ProductDetailVo> detail(HttpSession session,
			Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getMsg());
		}
		if (userService.checkAdminRole(user).isSuccess()) {
			ServerResponse<ProductDetailVo> response = productService
					.findProductById(productId,false);
			return response;
		}
		return ServerResponse.createByErrorMessage("管理员权限不足");
	}

	/**
	 * 
	 * @Title: getList
	 * @Description: 商品列表分页
	 * @param: @param session
	 * @param: @param pageNum
	 * @param: @param pageSize
	 * @param: @return     
	 * @return: ServerResponse<Product>     
	 * @author:  xuzijia
	 * @date: 2017年10月16日 下午3:10:44
	 * @throws
	 */
	@RequestMapping(value = "list.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse getList(HttpSession session,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");

		}
		if (userService.checkAdminRole(user).isSuccess()) {
			// 填充业务
			return productService.getList(pageNum, pageSize);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	/**
	 * 
	 * @Title: searchProduct
	 * @Description: 后台商品搜索
	 * @param: @param session
	 * @param: @param pageNum
	 * @param: @param pageSize
	 * @param: @param searchKey
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月16日 下午4:05:03
	 * @throws
	 */
	@RequestMapping(value = "search.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse searchProduct(
			HttpSession session,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			String searchKey, Integer categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (userService.checkAdminRole(user).isSuccess()) {
			// 填充业务
			return productService.searchProduct(pageNum, pageSize, StringUtils.isBlank(searchKey)?null:searchKey,
					categoryId);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	/**
	 * 
	 * @Title: upload
	 * @Description: 商品图片上传
	 * @param: @param session
	 * @param: @param file
	 * @param: @param request
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月17日 上午8:35:48
	 * @throws
	 */
	@RequestMapping(value = "upload.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse upload(
			HttpSession session,
			@RequestParam(value = "upload_file", required = false) MultipartFile file,
			HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录管理员");
		}
		if (userService.checkAdminRole(user).isSuccess()) {
			// 填充业务
			String path = request.getSession().getServletContext()
					.getRealPath("upload");
			String targetFileName = fileService.upload(file, path);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")
					+ targetFileName;

			Map fileMap = Maps.newHashMap();
			fileMap.put("uri", targetFileName);
			fileMap.put("url", url);
			return ServerResponse.createBySuccess(fileMap);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作");
		}
	}

	@RequestMapping("richtext_img_upload.do")
	@ResponseBody
	public Map richtextImgUpload(
			HttpSession session,
			@RequestParam(value = "upload_file", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		Map resultMap = Maps.newHashMap();
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			resultMap.put("success", false);
			resultMap.put("msg", "请登录管理员");
			return resultMap;
		}
		// 富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
		// {
		// "success": true/false,
		// "msg": "error message", # optional
		// "file_path": "[real file path]"
		// }
		if (userService.checkAdminRole(user).isSuccess()) {
			String path = request.getSession().getServletContext()
					.getRealPath("upload");
			String targetFileName = fileService.upload(file, path);
			if (StringUtils.isBlank(targetFileName)) {
				resultMap.put("success", false);
				resultMap.put("msg", "上传失败");
				return resultMap;
			}
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")
					+ targetFileName;
			resultMap.put("success", true);
			resultMap.put("msg", "上传成功");
			resultMap.put("file_path", url);
			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
			return resultMap;
		} else {
			resultMap.put("success", false);
			resultMap.put("msg", "无权限操作");
			return resultMap;
		}
	}

}
