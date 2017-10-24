package com.mmall.common;

/**   
*    
* 项目名称：mmall   
* 类名称：ResponseCode   
* 类描述：状态码描述枚举类
* 创建人：xuzijia   
* 创建时间：2017年10月12日 上午9:41:30   
* 修改人：xuzijia   
* 修改时间：2017年10月12日 上午9:41:30   
* 修改备注：   
* @version 1.0
*    
*/
public enum ResponseCode {
	SUCCESS(0,"SUCCESS"),
	ERROR(1,"ERROR"),
	NEED_LOGIN(10,"NEED_LOGIN"),
	ILLEGAL_ARGUMENT(-1,"NEED PARAM");
	private int code;
	private String msg;
	ResponseCode(int code,String msg){
		this.code=code;
		this.msg=msg;
	}
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
}
