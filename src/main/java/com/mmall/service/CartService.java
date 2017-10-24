package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**   
*    
* 项目名称：mmall   
* 类名称：CartService   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月18日 下午3:22:36   
* 修改人：xuzijia   
* 修改时间：2017年10月18日 下午3:22:36   
* 修改备注：   
* @version 1.0
*    
*/
public interface CartService {
	ServerResponse add(Integer userId,Integer productId,Integer count);

	ServerResponse<CartVo> list(Integer userId);

	ServerResponse<CartVo> deleteByCart(Integer[] cartId,Integer userId);

	ServerResponse<CartVo> clearCart(Integer userId); 
	
	ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);
	
	ServerResponse<Integer> selectCartCount(Integer userId);
}
