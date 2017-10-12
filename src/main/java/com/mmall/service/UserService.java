package com.mmall.service;

import org.springframework.stereotype.Service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**   
*    
* 项目名称：mmall   
* 类名称：UserService   
* 类描述：门户_用户service接口
* 创建人：xuzijia   
* 创建时间：2017年10月12日 上午9:34:12   
* 修改人：xuzijia   
* 修改时间：2017年10月12日 上午9:34:12   
* 修改备注：   
* @version 1.0
*    
*/
public interface UserService {
	ServerResponse<User> login(User user);
	ServerResponse<String> register(User user);
	ServerResponse<String> checkValid(String str,String type);
	ServerResponse<User> findUserByUsername(String username);
	ServerResponse<String> getQuestion(String username);
	ServerResponse<String> checkAnswer(String username,String question,String answer);
	ServerResponse<String> forgetPassword(String username,String newPassword,String Usertoken);
	ServerResponse<String> resetPassword(String username,String passwordOld,String passwordNew);
	ServerResponse<User> updateUserInfo(User user);
}
