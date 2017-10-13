package com.mmall.service.impl;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.MD5Util;

/**   
*    
* 项目名称：mmall   
* 类名称：UserServiceImpl   
* 类描述：门户_用户service实现类
* 创建人：xuzijia   
* 创建时间：2017年10月12日 上午9:35:50   
* 修改人：xuzijia   
* 修改时间：2017年10月12日 上午9:35:50   
* 修改备注：   
* @version 1.0
*    
*/
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	/**
	 * 用户登陆
	 * --用户名校验
	 * --信息校验
	 */
	@Override
	public ServerResponse<User> login(User user) {
		// 校验用户名
		int count = userMapper.checkUsername(user.getUsername());
		if (count == 0) {
			// 用户名不存在
			return ServerResponse.createByErrorMessage("用户名不存在");
		}

		// md5加密
		String md5Password = MD5Util.MD5EncodeUtf8(user.getPassword());

		// 校验信息
		User checkUser = userMapper.checkLogin(user.getUsername(), md5Password);
		if (checkUser == null) {
			// 密码错误
			return ServerResponse.createByErrorMessage("密码错误");
		}

		checkUser.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功", checkUser);
	}

	/**
	 * 用户注册
	 */
	public ServerResponse<String> register(User user) {
		// 校验用户名
		ServerResponse validResponse = this.checkValid(user.getUsername(),
				Const.USERNAME);
		if (!validResponse.isSuccess()) {
			// 用户名已存在
			return validResponse;
		}

		// 校验email
		validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
		if (!validResponse.isSuccess()) {
			// email已存在
			return validResponse;
		}
		// 角色分配
		user.setRole(Const.ROLE_CUSTOMER);

		// md5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

		// 注册
		int i = userMapper.insert(user);
		if (i == 0) {
			// 注册失败
			return ServerResponse.createByErrorMessage("内部错误，注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}

	/**
	 * 参数校验
	 */
	public ServerResponse<String> checkValid(String str, String type) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
			// 开始校验
			if (Const.USERNAME.equals(type)) {
				int resultCount = userMapper.checkUsername(str);
				if (resultCount > 0) {
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if (Const.EMAIL.equals(type)) {
				int resultCount = userMapper.checkEmail(str);
				if (resultCount > 0) {
					return ServerResponse.createByErrorMessage("email已存在");
				}
			}
		} else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccessMessage("校验成功");
	}

	/**
	 * 根据用户名查询用户
	 */
	public ServerResponse<User> findUserByUsername(String username) {
		User user = userMapper.findUserByUsername(username);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccess(user);
	}

	/**
	 * 根据用户名查询用户设置的问题信息
	 */
	public ServerResponse<String> getQuestion(String username) {
		// 校验用户名
		ServerResponse validResponse = this
				.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			// 用户名不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String question = userMapper.getQuestion(username);
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(question)) {
			return ServerResponse.createBySuccess(question);
		}
		return ServerResponse.createByErrorMessage("该用户没有设置问题 请选择用邮箱找回密码");
	}

	/**
	 * 校验用户问题答案
	 */
	public ServerResponse<String> checkAnswer(String username, String question,
			String answer) {
		int i = userMapper.checkAnswer(username, question, answer);
		if (i > 0) {
			// token
			// 说明问题及问题答案是这个用户的,并且是正确的
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		// 答案错误
		return ServerResponse.createByErrorMessage("问题答案错误");
	}

	/**
	 * 忘记密码 设置新密码
	 */
	public ServerResponse<String> forgetPassword(String username,
			String newPassword, String Usertoken) {
		if (StringUtils.isBlank(Usertoken)) {
			return ServerResponse.createByErrorMessage("参数错误,token需要传递");
		}
		ServerResponse validResponse = this
				.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			// 用户不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
		if (StringUtils.isBlank(token)) {
			return ServerResponse.createByErrorMessage("token无效或者过期");
		}
		if (StringUtils.equals(Usertoken, token)) {
			String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
			// 更新用户密码
			int count = userMapper.updatePassword(username, md5Password);
			if (count > 0) {
				return ServerResponse.createBySuccessMessage("修改密码成功");
			} else {
				return ServerResponse.createByErrorMessage("未知错误，请重新操作");
			}
		} else {
			return ServerResponse.createByErrorMessage("token不匹配，请重新操作");
		}
	}

	/**
	 * 登陆状态下重置密码
	 */
	public ServerResponse<String> resetPassword(String username,
			String passwordOld, String passwordNew) {
		ServerResponse validResponse = this
				.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			// 用户不存在
			return ServerResponse.createByErrorMessage("用户不存在");
		}
		// 校验旧密码
		passwordOld = MD5Util.MD5EncodeUtf8(passwordOld);
		passwordNew = MD5Util.MD5EncodeUtf8(passwordNew);
		int count = userMapper.checkPassword(username, passwordOld);
		if (count > 0) {
			// 执行重置操作
			int i = userMapper.updatePassword(username, passwordNew);
			if (i > 0) {
				return ServerResponse.createBySuccessMessage("密码修改成功");
			} else {
				return ServerResponse.createByErrorMessage("密码重置失败");
			}
		} else {
			return ServerResponse.createByErrorMessage("旧密码输入错误");
		}
	}

	/**
	 * 修改个人信息
	 */
	public ServerResponse<User> updateUserInfo(User user) {
		// 校验email是否存在于其他用户
		int count = userMapper.checkEmailById(user.getId(), user.getEmail());
		if (count > 0) {
			// email已存在
			return ServerResponse.createByErrorMessage("email已存在，请更换一个试试");
		}
		User updateUser = new User();
		updateUser.setId(user.getId());
		updateUser.setEmail(user.getEmail());
		updateUser.setPhone(user.getPhone());
		updateUser.setQuestion(user.getQuestion());
		updateUser.setAnswer(user.getAnswer());

		int u = userMapper.updateByPrimaryKeySelective(updateUser);
		if (u > 0) {
			return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
		}
		return ServerResponse.createByErrorMessage("更新个人信息失败");
	}

	//校验是否为后台管理员
	public ServerResponse<String> checkAdminRole(User user){
		if(user!=null && user.getRole()==Const.ROLE_ADMIN){
			return ServerResponse.createBySuccess();
		}
		return ServerResponse.createByError();
		
	}
	
}
