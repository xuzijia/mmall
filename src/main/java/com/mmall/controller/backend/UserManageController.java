package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.UserService;

/**   
*    
* 项目名称：mmall   
* 类名称：UserManageController   
* 类描述：后台用户controller
* 创建人：xuzijia   
* 创建时间：2017年10月12日 下午8:35:33   
* 修改人：xuzijia   
* 修改时间：2017年10月12日 下午8:35:33   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(User user,HttpSession session){
		ServerResponse<User> response = userService.login(user);
		if(response.isSuccess()){
			if(response.getData().getRole()==Const.ROLE_ADMIN){
				//管理员 保存session
				session.setAttribute(Const.CURRENT_USER, response.getData());
				return response;
			}
			else{
				return ServerResponse.createByErrorMessage("不是管理员 别闹~");
			}
		}
		return response;
	}
}
