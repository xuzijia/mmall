package com.mmall.controller.backend;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.CategoryService;
import com.mmall.service.UserService;

/**   
*    
* 项目名称：mmall   
* 类名称：CatrgoryManageController   
* 类描述：后台分类controller
* 创建人：xuzijia   
* 创建时间：2017年10月13日 上午9:52:22   
* 修改人：xuzijia   
* 修改时间：2017年10月13日 上午9:52:22   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
@RequestMapping("/manage/category/")
public class CatrgoryManageController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	/**
	 * 
	 * @Title: addCategory
	 * @Description: 添加分类
	 * @param: @param session
	 * @param: @param categoryName
	 * @param: @param parentId
	 * @param: @return     
	 * @return: ServerResponse<String>     
	 * @author:  xuzijia
	 * @date: 2017年10月13日 上午10:54:11
	 * @throws
	 */
	@RequestMapping(value="add_category.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> addCategory(HttpSession session,String categoryName,@RequestParam(value="parentId",defaultValue="0")Integer parentId){
		User user=(User)session.getAttribute(Const.CURRENT_USER);
		//登陆校验
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
		}
		//后台管理员登陆校验
		if(userService.checkAdminRole(user).isSuccess()){
			ServerResponse response = categoryService.addCategory(categoryName, parentId);
			return response;
		}else{
			return ServerResponse.createByErrorMessage("请用管理员账号进行登陆才可进行操作");
		}
	}
	/**
	 * 
	 * @Title: getCategory
	 * @Description: 获取父节点下的子节点 不递归 默认查寻根节点下的分类
	 * @param: @param parentId
	 * @param: @return     
	 * @return: ServerResponse<List<Category>>     
	 * @author:  xuzijia
	 * @date: 2017年10月13日 上午10:57:51
	 * @throws
	 */
	@RequestMapping(value="get_category.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<List<Category>> getCategory(HttpSession session,@RequestParam(value="parentId", defaultValue="0")Integer parentId){
		User user=(User)session.getAttribute(Const.CURRENT_USER);
		//登陆校验
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
		}
		//后台管理员登陆校验
		if(userService.checkAdminRole(user).isSuccess()){
			ServerResponse<List<Category>> response = categoryService.getCategory(parentId);
			return response;
		}else{
			return ServerResponse.createByErrorMessage("请用管理员账号进行登陆才可进行操作");
		}
	}
	@RequestMapping(value="updateCategory.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> updateCategory(HttpSession session,Category category){
		User user=(User)session.getAttribute(Const.CURRENT_USER);
		//登陆校验
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
		}
		//后台管理员登陆校验
		if(userService.checkAdminRole(user).isSuccess()){
			ServerResponse<String> response = categoryService.updateCategory(category);
			return response;
		}else{
			return ServerResponse.createByErrorMessage("请用管理员账号进行登陆才可进行操作");
		}
	}
}
