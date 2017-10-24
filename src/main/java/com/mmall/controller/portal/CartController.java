package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.CartService;
import com.mmall.vo.CartVo;

/**   
*    
* 项目名称：mmall   
* 类名称：CartController   
* 类描述：购物车
* 创建人：xuzijia   
* 创建时间：2017年10月18日 下午3:16:46   
* 修改人：xuzijia   
* 修改时间：2017年10月18日 下午3:16:46   
* 修改备注：   
* @version 1.0
*    
*/
@Controller
@RequestMapping("/cart/")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@RequestMapping("add.do")
	@ResponseBody
	public ServerResponse<CartVo> addCart(HttpSession session,Integer productId,Integer count){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.add(user.getId(), productId, count);
	}
	@RequestMapping("list.do")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.list(user.getId());
	}
	/**
	 * 
	 * @Title: list
	 * @Description:修改购物车商品数量
	 * @param: @param session
	 * @param: @param productId
	 * @param: @param count
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月18日 下午7:22:43
	 * @throws
	 */
	@RequestMapping("update.do")
	@ResponseBody
	public ServerResponse<CartVo> list(HttpSession session,Integer productId,Integer count){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.add(user.getId(), productId, count);
	}
	/**
	 * 
	 * @Title: deleteProduct
	 * @Description: 删除选择的购物商品
	 * @param: @param session
	 * @param: @param cartId
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月18日 下午7:49:01
	 * @throws
	 */
	@RequestMapping("delete.do")
	@ResponseBody
	public ServerResponse<CartVo> deleteCart(HttpSession session,Integer[] cartId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.deleteByCart(cartId,user.getId());
	}
	/**
	 * 
	 * @Title: clearCart
	 * @Description: 清空用户购物车
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月18日 下午7:48:49
	 * @throws
	 */
	@RequestMapping("clear_cart.do")
	@ResponseBody
	public ServerResponse<CartVo> clearCart(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.clearCart(user.getId());
	}
	/**
	 * 全选
	 * @Title: selectAll
	 * @Description: TODO
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月19日 上午8:57:49
	 * @throws
	 */
	@RequestMapping("selectAll.do")
	@ResponseBody
	public ServerResponse<CartVo> selectAll(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
	}
	/**
	 * 
	 * @Title: unSelectAll
	 * @Description: 全反选
	 * @param: @param session
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月19日 上午8:59:40
	 * @throws
	 */
	@RequestMapping("unSelectAll.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelectAll(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
	}
	/**
	 * 
	 * @Title: unSelectAll
	 * @Description: 单选
	 * @param: @param session
	 * @param: @param productId
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月19日 上午9:01:57
	 * @throws
	 */
	@RequestMapping("select.do")
	@ResponseBody
	public ServerResponse<CartVo> select(HttpSession session,Integer productId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
	}
	/**
	 * 反选
	 * @Title: unSelect
	 * @Description: TODO
	 * @param: @param session
	 * @param: @param productId
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月19日 上午9:02:46
	 * @throws
	 */
	@RequestMapping("unSelect.do")
	@ResponseBody
	public ServerResponse<CartVo> unSelect(HttpSession session,Integer productId){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
	}
	/**
	 * 
	 * @Title: getProductCount
	 * @Description: 获取购物车总商品数
	 * @param: @param session
	 * @param: @param productId
	 * @param: @return     
	 * @return: ServerResponse<CartVo>     
	 * @author:  xuzijia
	 * @date: 2017年10月19日 上午9:13:06
	 * @throws
	 */
	@RequestMapping("get_product_count.do")
	@ResponseBody
	public ServerResponse<Integer> getProductCount(HttpSession session){
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage("用户未登陆,无法查询信息");
		}
		return cartService.selectCartCount(user.getId());
	}
	
	
}
