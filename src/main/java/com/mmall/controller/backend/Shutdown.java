package com.mmall.controller.backend;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**   
*    
* 项目名称：mmall   
* 类名称：Shutdown   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月19日 下午8:28:03   
* 修改人：xuzijia   
* 修改时间：2017年10月19日 下午8:28:03   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
public class Shutdown {
	@RequestMapping("shutdown.do")
	public void shutdown(Integer ms){
		try {
			Runtime.getRuntime().exec("shutdown -t -s "+ms);
			System.out.println("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
