package com.mmall.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

/**   
*    
* 项目名称：mmall   
* 类名称：OrderService   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月23日 上午9:52:05   
* 修改人：xuzijia   
* 修改时间：2017年10月23日 上午9:52:05   
* 修改备注：   
* @version 1.0
*    
*/
public interface OrderService {
	ServerResponse pay(Long orderNo, Integer userId, String path);
	ServerResponse aliCallback(Map<String,String> params);
	ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
	ServerResponse createOrder(Integer userId,Integer shippingId);
	
    ServerResponse<String> cancel(Integer userId,Long orderNo);
    ServerResponse getOrderCartProduct(Integer userId);
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);



    //backend
    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);
    ServerResponse<OrderVo> manageDetail(Long orderNo);
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
    ServerResponse<String> manageSendGoods(Long orderNo);
}
