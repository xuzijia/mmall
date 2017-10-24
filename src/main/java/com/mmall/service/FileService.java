package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**   
*    
* 项目名称：mmall   
* 类名称：FileService   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月17日 上午8:26:17   
* 修改人：xuzijia   
* 修改时间：2017年10月17日 上午8:26:17   
* 修改备注：   
* @version 1.0
*    
*/
public interface FileService {
	public String upload(MultipartFile file,String path);
}
