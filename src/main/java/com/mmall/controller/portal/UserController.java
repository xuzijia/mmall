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
	@RequestMapping(value="loginout.do",method=RequestMethod.GET)
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
    /**
     * 
     * @Title: getUserInfo
     * @Description: 获取登陆用户信息
     * @param: @return     
     * @return: ServerResponse<User>     
     * @author:  xuzijia
     * @date: 2017年10月12日 下午2:37:33
     * @throws
     */
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
    	User user=(User)session.getAttribute(Const.CURRENT_USER);
    	if(user==null){
    		return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
    	}
    	//获取用户信息
    	return userService.findUserByUsername(user.getUsername());
    }
    /**
     * 
     * @Title: getQuestion
     * @Description: 忘记密码获取用户问题
     * @param: @param username
     * @param: @return     
     * @return: ServerResponse<String>     
     * @author:  xuzijia
     * @date: 2017年10月12日 下午3:27:55
     * @throws
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getQuestion(String username){
    	return userService.getQuestion(username);
    }
    /**
     * 
     * @Title: checkAnswer
     * @Description: 忘记密码 校验问题答案
     * @param: @param username
     * @param: @param question
     * @param: @param answer
     * @param: @return     
     * @return: ServerResponse<String>     
     * @author:  xuzijia
     * @date: 2017年10月12日 下午3:30:53
     * @throws
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
    	return userService.checkAnswer(username,question,answer);
    }
    /**
     * 
     * @Title: forgetPassword
     * @Description: 忘记密码的重设密码
     * @param: @param username
     * @param: @param password
     * @param: @param token
     * @param: @return     
     * @return: ServerResponse<String>     
     * @author:  xuzijia
     * @date: 2017年10月12日 下午4:12:31
     * @throws
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPassword(String username,String passwordNew,String forgetToken){
    	return userService.forgetPassword(username, passwordNew, forgetToken);
    }
    /**
     * 
     * @Title: resetPassword
     * @Description: 登陆状态下重置密码
     * @param: @return     
     * @return: ServerResponse<String>     
     * @author:  xuzijia
     * @date: 2017年10月12日 下午4:21:49
     * @throws
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,HttpSession session){
    	User user=(User) session.getAttribute(Const.CURRENT_USER);
    	if(user==null){
    		//用户未登陆
    		return ServerResponse.createByErrorMessage("请在登陆状态下执行重置密码操作");
    	}
    	return userService.resetPassword(user.getUsername(),passwordOld,passwordNew);
    }
    /**
     * 
     * @Title: updateUserInfo
     * @Description: 登陆状态下更新个人信息
     * @param: @param user
     * @param: @param session
     * @param: @return     
     * @return: ServerResponse<User>     
     * @author:  xuzijia
     * @date: 2017年10月12日 下午7:58:19
     * @throws
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(User user,HttpSession session){
    	//不更新用户名 email是否存在校验
    	User currentUser=(User) session.getAttribute(Const.CURRENT_USER);
    	if(currentUser==null){
    		//用户未登陆
    		return ServerResponse.createByErrorMessage("请在登陆状态下修改信息");
    	}
    	user.setId(currentUser.getId());
    	ServerResponse<User> updateUserInfo = userService.updateUserInfo(user);
    	if(updateUserInfo.isSuccess()){
    		updateUserInfo.getData().setUsername(currentUser.getUsername());
    		session.setAttribute(Const.CURRENT_USER, updateUserInfo.getData());
    	}
    	return updateUserInfo;
    	
    }
    
}
