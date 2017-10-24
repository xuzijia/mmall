package com.mmall.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.CartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;

/**   
*    
* 项目名称：mmall   
* 类名称：CartServiceImpl   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月18日 下午3:24:24   
* 修改人：xuzijia   
* 修改时间：2017年10月18日 下午3:24:24   
* 修改备注：   
* @version 1.0
*    
*/
@Service("cartService")
public class CartServiceImpl implements CartService {
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;

	/**
	 * 添加商品到购物车
	 */
	@Override
	public ServerResponse add(Integer userId, Integer productId, Integer count) {
		if (productId == null || count == null) {
			return ServerResponse.createByErrorCodeMessage(
					ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getMsg());
		}
		// 查询是否存在该商品
		Product p = productMapper.selectByPrimaryKey(productId, true);
		if (p == null) {
			return ServerResponse.createByErrorMessage("当前商品不存在或者已经下架");
		}

		// 查询该商品是否存在于购物车
		Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);
		if (cart == null) {
			Cart cartItem = new Cart();
			cartItem.setQuantity(count);
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setProductId(productId);
			cartItem.setUserId(userId);
			cartMapper.insert(cartItem);
		} else {
			cart.setQuantity(cart.getQuantity() + count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}
		return this.list(userId);
	}

	/**
	 * 查询用户的购物车列表
	 */
	@Override
	public ServerResponse<CartVo> list(Integer userId) {
		CartVo cartVo = this.getCartVoLimit(userId);
		return ServerResponse.createBySuccess(cartVo);
	}

	/**
	 * 删除或者批量删除购物车商品
	 */
	public ServerResponse<CartVo> deleteByCart(Integer[] cartId, Integer userId) {
		if (cartId != null && cartId.length > 0) {
			for (Integer id : cartId) {
				cartMapper.deleteByCartId(id,userId);
			}
		}
		return this.list(userId);
	}

	/**
	 * 清空用户购物车
	 */
	public ServerResponse<CartVo> clearCart(Integer userId) {
		int i = cartMapper.clearCartByUserId(userId);
		if (i > 0) {
			// 清空成功返回字符串
			return ServerResponse.createBySuccessMessage("清空成功");
		}
		// 清空失败 返回该用户的购物车列表
		return this.list(userId);
	}
	/**
	 * 
	 * @Title: selectOrUnSelect
	 * @Description: 选择或者反选
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月19日 上午8:39:57
	 * @throws
	 */
	public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked){
		cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
		return this.list(userId);
	}
	
	

	public ServerResponse<Integer> selectCartCount(Integer userId) {
		if(userId==null){
			return ServerResponse.createBySuccess(0);
		}
		int i = cartMapper.selectCartProductCount(userId);
		return ServerResponse.createBySuccess(i);
	}

	/**
	 * 封装vo对象给前台渲染
	 * @Title: getCartVoLimit
	 * @Description: TODO
	 * @param: @param userId
	 * @param: @return     
	 * @return: CartVo     
	 * @author:  xuzijia
	 * @date: 2017年10月18日 下午7:27:05
	 * @throws
	 */
	private CartVo getCartVoLimit(Integer userId) {
		CartVo cartVo = new CartVo();
		List<Cart> cartList = cartMapper.selectCartByUserId(userId);
		List<CartProductVo> cartProductVoList = Lists.newArrayList();

		BigDecimal cartTotalPrice = new BigDecimal("0");

		if (CollectionUtils.isNotEmpty(cartList)) {
			for (Cart cartItem : cartList) {
				CartProductVo cartProductVo = new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserId(userId);
				cartProductVo.setProductId(cartItem.getProductId());

				Product product = productMapper.selectByPrimaryKey(
						cartItem.getProductId(), true);
				if (product != null) {
					cartProductVo.setProductMainImage(product.getMainImage());
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductPrice(product.getPrice());
					cartProductVo.setProductStock(product.getStock());
					// 判断库存
					int buyLimitCount = 0;
					if (product.getStock() >= cartItem.getQuantity()) {
						// 库存充足的时候
						buyLimitCount = cartItem.getQuantity();
						cartProductVo
								.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
					} else {
						buyLimitCount = product.getStock();
						cartProductVo
								.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
						// 购物车中更新有效库存
						Cart cartForQuantity = new Cart();
						cartForQuantity.setId(cartItem.getId());
						cartForQuantity.setQuantity(buyLimitCount);
						cartMapper.updateByPrimaryKeySelective(cartForQuantity);
					}
					cartProductVo.setQuantity(buyLimitCount);
					// 计算总价
					cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(
							product.getPrice().doubleValue(),
							cartProductVo.getQuantity()));
					cartProductVo.setProductChecked(cartItem.getChecked());
				}

				if (cartItem.getChecked() == Const.Cart.CHECKED) {
					// 如果已经勾选,增加到整个的购物车总价中
					cartTotalPrice = BigDecimalUtil.add(cartTotalPrice
							.doubleValue(), cartProductVo
							.getProductTotalPrice().doubleValue());
				}
				cartProductVoList.add(cartProductVo);
			}
		}
		cartVo.setCartTotalPrice(cartTotalPrice);
		cartVo.setCartProductVoList(cartProductVoList);
		cartVo.setAllChecked(this.getAllCheckedStatus(userId));
		cartVo.setImageHost(PropertiesUtil
				.getProperty("ftp.server.http.prefix"));
		return cartVo;
	}

	private boolean getAllCheckedStatus(Integer userId) {
		if (userId == null) {
			return false;
		}
		return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

	}
}
