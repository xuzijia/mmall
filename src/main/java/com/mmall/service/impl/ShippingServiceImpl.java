package com.mmall.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.ShippingService;

/**   
*    
* 项目名称：mmall   
* 类名称：ShippingServiceImpl   
* 类描述：前台购物地址
* 创建人：xuzijia   
* 创建时间：2017年10月19日 上午11:31:20   
* 修改人：xuzijia   
* 修改时间：2017年10月19日 上午11:31:20   
* 修改备注：   
* @version 1.0
*    
*/
@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {
	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ServerResponse add(Integer userId, Shipping shipping) {
		shipping.setUserId(userId);
		int i = shippingMapper.insert(shipping);
		if (i > 0) {
			Map result = Maps.newHashMap();
			return this.list(userId, 1, 10);
		}
		return ServerResponse.createByErrorMessage("新建地址失败");
	}

	public ServerResponse<String> del(Integer userId, Integer shippingId) {
		int resultCount = shippingMapper.deleteByShippingIdUserId(userId,
				shippingId);
		if (resultCount > 0) {
			return ServerResponse.createBySuccess("删除地址成功");
		}
		return ServerResponse.createByErrorMessage("删除地址失败");
	}

	public ServerResponse update(Integer userId, Shipping shipping) {
		shipping.setUserId(userId);
		int rowCount = shippingMapper.updateByShipping(shipping);
		if (rowCount > 0) {
			return ServerResponse.createBySuccess("更新地址成功");
		}
		return ServerResponse.createByErrorMessage("更新地址失败");
	}

	public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
		Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,
				shippingId);
		if (shipping == null) {
			return ServerResponse.createByErrorMessage("无法查询到该地址");
		}
		return ServerResponse.createBySuccess("查询地址成功", shipping);
	}

	public ServerResponse<PageInfo> list(Integer userId, int pageNum,
			int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
		PageInfo pageInfo = new PageInfo(shippingList);
		return ServerResponse.createBySuccess(pageInfo);
	}

}
