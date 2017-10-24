package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**   
*    
* 项目名称：mmall   
* 类名称：ProductService   
* 类描述：
* 创建人：xuzijia   
* 创建时间：2017年10月16日 上午8:58:42   
* 修改人：xuzijia   
* 修改时间：2017年10月16日 上午8:58:42   
* 修改备注：   
* @version 1.0
*    
*/
public interface ProductService {
	public ServerResponse<String> saveOrUpdateProduct(Product product);

	public ServerResponse<String> setStatus(Integer productId, Integer status);
	
	ServerResponse<ProductDetailVo> findProductById(Integer productId, boolean flag);
	
	ServerResponse<PageInfo> getList(int pageNum,int pageSize);
	
	ServerResponse<PageInfo> searchProduct(int pageNum,int pageSize,String searchKey, Integer categoryId);
	ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
