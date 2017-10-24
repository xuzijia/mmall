package com.mmall.common;

import java.util.Set;

import com.google.common.collect.Sets;

/**   
*    
* 项目名称：mmall   
* 类名称：Const   
* 类描述：application全局常量
* 创建人：xuzijia   
* 创建时间：2017年10月12日 上午10:11:54   ，
* 修改人：xuzijia   
* 修改时间：2017年10月12日 上午10:11:54   
* 修改备注：   
* @version 1.0
*    
*/
public class Const {
	public final static String CURRENT_USER = "currnet_user";

	public static final String EMAIL = "email";
	public static final String USERNAME = "username";

	public final static int ROLE_CUSTOMER = 0; // 普通用户
	public final static int ROLE_ADMIN = 1;// 管理员

	public final static String EMAIL_SEND_USER = "13049257683@163.com";// 邮件发送者
	public final static String EMAIL_USERNAME = "13049257683@163.com";// 163邮箱用户名
	public final static String EMAIL_PASSWORD = "xuzijia123";// 163邮箱授权密码
	public final static String EMAIL_HOST = "smtp.163.com";// 163邮箱主机

	public interface ProductListOrderBy {
		Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
	}

	public enum ProductStatusEnum {
		ON_SALE(1, "在线");
		private String value;
		private int code;

		ProductStatusEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}
	}

	public interface Cart {
		int CHECKED = 1;// 即购物车选中状态
		int UN_CHECKED = 0;// 购物车中未选中状态

		String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
	}
	
	
	


    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");


        OrderStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }
    }
    public interface  AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }



    public enum PayPlatformEnum{
        ALIPAY(1,"支付宝");

        PayPlatformEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        PaymentTypeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }


        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }

    }

}
