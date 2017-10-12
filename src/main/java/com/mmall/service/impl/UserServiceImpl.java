package com.mmall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
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
		
		//md5加密
		String md5Password=MD5Util.MD5EncodeUtf8(user.getPassword());
		
		// 校验信息
		User checkUser = userMapper.checkLogin(user.getUsername(),
				md5Password);
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
	public ServerResponse<String> register(User user){
		//校验用户名
		ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
		if(!validResponse.isSuccess()){
			// 用户名已存在
			return validResponse;
		}
		
		//校验email
		validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
		if(!validResponse.isSuccess()){
			// email已存在
			return validResponse;
		}
		//角色分配
		user.setRole(Const.ROLE_CUSTOMER);
		
		//md5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		
		//注册
		int i = userMapper.insert(user);
		if(i==0){
			//注册失败
			return ServerResponse.createByErrorMessage("内部错误，注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}
	
	/**
	 * 参数校验
	 */
    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
}
