package com.mmall.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.mmall.service.FileService;
import com.mmall.util.FTPUtil;

/**   
*    
* 项目名称：mmall   
* 类名称：FileServiceImpl   
* 类描述：文件上传
* 创建人：xuzijia   
* 创建时间：2017年10月17日 上午8:27:43   
* 修改人：xuzijia   
* 修改时间：2017年10月17日 上午8:27:43   
* 修改备注：   
* @version 1.0
*    
*/
@Service("fileService")
public class FileServiceImpl implements FileService {
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	/**
	 * 文件上传
	 */
	public String upload(MultipartFile file, String path) {
		String fileName=file.getOriginalFilename();
		//获取扩展名
		String type=fileName.substring(fileName.lastIndexOf(".")+1);
		
		String uploadName=UUID.randomUUID().toString()+"."+type;
		
		
		logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadName);
		
		//年月日
		Date date=new Date();
		SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
		String FormatDate=d.format(date);
		
		File fileDir = new File(path,FormatDate);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path+"/"+FormatDate,uploadName);
        
       
        try {
			file.transferTo(targetFile);
			
			 FTPUtil.uploadFile(Lists.newArrayList(targetFile));
		}  catch (IOException e) {
			logger.error("上传文件异常",e);
            return null;
		}
		return FormatDate+"/"+targetFile.getName();
	}

}
