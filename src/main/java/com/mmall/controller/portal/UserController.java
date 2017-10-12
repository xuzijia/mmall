package com.mmall.controller.portal;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
* 类名称：UserController   
* 类描述：门户 用户Controller接口
* 创建人：xuzijia   
* 创建时间：2017年10月12日 上午9:27:29   
* 修改人：xuzijia   
* 修改时间：2017年10月12日 上午9:27:29   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
@RequestMapping(value="/user/")
public class UserController {
	@Autowired
	private UserService userService;
	/**
	 * 
	 * @Title: login
	 * @Description: 门户_用户登陆
	 * @param: @param user
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月12日 上午9:33:32
	 * @throws
	 */
	@RequestMapping(value="login.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(User user, HttpSession session) {
		ServerResponse<User> userResponse = userService.login(user);
		//保存session信息
		if(userResponse.isSuccess()){
			session.setAttribute(Const.CURRENT_USER, userResponse.getData());
		}
		return userResponse;
	}
	/**
	 *
	 * @Title: register
	 * @Description: 用户注册功能
	 * @param: @param user
	 * @param: @return     
	 * @return: ServerResponse<String>     
	 * @author:  xuzijia
	 * @date: 2017年10月12日 上午11:17:38
	 * @throws
	 */
	@RequestMapping(value="register.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user){
		ServerResponse<String> response = userService.register(user);
		return response;
	}
	/**
	 * 
	 * @Title: loginout
	 * @Description: 退出登陆功能
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<String>     
	 * @author:  xuzijia
	 * @date: 2017年10月12日 上午11:20:03
	 * @throws
	 */
	@RequestMapping(value="loginout.do",method=RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> loginout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	/**
	 * 
	 * @Title: checkValid
	 * @Description: 用户信息校验
	 * @param: @param str
	 * @param: @param type
	 * @param: @return     
	 * @return: ServerResponse<String>     
	 * @author:  xuzijia
	 * @date: 2017年10月12日 上午11:40:26
	 * @throws
	 */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return userService.checkValid(str,type);
    }
}
