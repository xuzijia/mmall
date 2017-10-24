package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**   
*    
* 项目名称：mmall   
* 类名称：ShippingService   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月19日 上午11:30:48   
* 修改人：xuzijia   
* 修改时间：2017年10月19日 上午11:30:48   
* 修改备注：   
* @version 1.0
*    
*/
public interface ShippingService {

	ServerResponse add(Integer userId, Shipping shipping);

	ServerResponse<String> del(Integer userId, Integer shippingId);

	ServerResponse update(Integer userId, Shipping shipping);

	ServerResponse<Shipping> select(Integer userId, Integer shippingId);

	ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
