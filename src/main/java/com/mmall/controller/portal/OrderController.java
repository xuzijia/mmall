package com.mmall.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.OrderService;

/**   
*    
* 项目名称：mmall   
* 类名称：OrderController   
* 类描述：支付宝支付接口 订单操作接口
* 创建人：xuzijia   
* 创建时间：2017年10月23日 上午9:48:20   
* 修改人：xuzijia   
* 修改时间：2017年10月23日 上午9:48:20   
* 修改备注：   
* @version 1.0
*    
*/

@Controller
@RequestMapping("/order/")
public class OrderController {
	private static final Logger logger = LoggerFactory
			.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	/**
	 * 
	 * @Title: create
	 * @Description: 生成订单
	 * @param: @param session
	 * @param: @param shippingId
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月23日 下午7:55:19
	 * @throws
	 */
	@RequestMapping("create.do")
	@ResponseBody
	public ServerResponse create(HttpSession session,Integer shippingId){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return orderService.createOrder(user.getId(),shippingId);
	}
	
	
	
	/**
	 * 
	 * @Title: cancel
	 * @Description: 取消订单
	 * @param: @param session
	 * @param: @param orderNo
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月23日 下午7:55:37
	 * @throws
	 */
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return orderService.cancel(user.getId(),orderNo);
    }

    /**
     * 
     * @Title: getOrderCartProduct
     * @Description: 获取订单中的商品
     * @param: @param session
     * @param: @return     
     * @return: ServerResponse     
     * @author:  xuzijia
     * @date: 2017年10月23日 下午7:55:50
     * @throws
     */
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return orderService.getOrderCartProduct(user.getId());
    }


    /**
     * 
     * @Title: detail
     * @Description: 订单详情
     * @param: @param session
     * @param: @param orderNo
     * @param: @return     
     * @return: ServerResponse     
     * @author:  xuzijia
     * @date: 2017年10月23日 下午7:56:06
     * @throws
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session,Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return orderService.getOrderDetail(user.getId(),orderNo);
    }
    /**
     * 
     * @Title: list
     * @Description: 订单列表
     * @param: @param session
     * @param: @param pageNum
     * @param: @param pageSize
     * @param: @return     
     * @return: ServerResponse     
     * @author:  xuzijia
     * @date: 2017年10月23日 下午7:56:18
     * @throws
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getMsg());
        }
        return orderService.getOrderList(user.getId(),pageNum,pageSize);
    }
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @Title: pay
	 * @Description: 发起支付请求
	 * @param: @param session
	 * @param: @param orderNo
	 * @param: @param request
	 * @param: @return     
	 * @return: ServerResponse     
	 * @author:  xuzijia
	 * @date: 2017年10月23日 上午10:59:55
	 * @throws
	 */
	@RequestMapping("pay.do")
	@ResponseBody
	public ServerResponse pay(HttpSession session, Long orderNo,
			HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getMsg());
		}
		String path = request.getSession().getServletContext()
				.getRealPath("upload");
		return orderService.pay(orderNo, user.getId(), path);
	}
	/**
	 * 
	 * @Title: alipayCallback
	 * @Description: 支付宝回调函数
	 * @param: @param request
	 * @param: @return     
	 * @return: Object     
	 * @author:  xuzijia
	 * @date: 2017年10月23日 上午11:00:23
	 * @throws
	 */
	@RequestMapping("alipay_callback.do")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();

		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {

				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"),
				params.get("trade_status"), params.toString());

		// 非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

		params.remove("sign_type");
		try {
			boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params,
					Configs.getAlipayPublicKey(), "utf-8",
					Configs.getSignType());

			if (!alipayRSACheckedV2) {
				return ServerResponse
						.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝验证回调异常", e);
		}

		// todo 验证各种数据

		//
		ServerResponse serverResponse = orderService.aliCallback(params);
		if (serverResponse.isSuccess()) {
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	/**
	 * 
	 * @Title: queryOrderPayStatus
	 * @Description: 查询订单支付情况
	 * @param: @param session
	 * @param: @param orderNo
	 * @param: @return     
	 * @return: ServerResponse<Boolean>     
	 * @author:  xuzijia
	 * @date: 2017年10月23日 上午11:00:39
	 * @throws
	 */
	@RequestMapping("query_order_pay_status.do")
	@ResponseBody
	public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session,
			Long orderNo) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getMsg());
		}

		ServerResponse serverResponse = orderService.queryOrderPayStatus(
				user.getId(), orderNo);
		if (serverResponse.isSuccess()) {
			return ServerResponse.createBySuccess(true);
		}
		return ServerResponse.createBySuccess(false);
	}
}
