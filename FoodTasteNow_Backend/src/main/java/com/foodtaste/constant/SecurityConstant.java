package com.foodtaste.constant;

public interface SecurityConstant {

	String public_Auth = AppConstants.APP_NAME + AppConstants.PUBLIC_ROUTE_TYPE + "/**";

	String user_Cart = AppConstants.APP_NAME + "/cart" + AppConstants.USER + "/**";

	String admin_MenuItem = AppConstants.APP_NAME + "/items" + AppConstants.ADMIN + "/**";
	String public_MenuItem = AppConstants.APP_NAME + "/items" + AppConstants.PUBLIC_ROUTE_TYPE + "/**";

	String admin_OrderDetails = AppConstants.APP_NAME + "/orders" + AppConstants.ADMIN + "/**";
	String user_OrderDetails = AppConstants.APP_NAME + "/orders" + AppConstants.USER + "/**";
	String common_OrderDetails = AppConstants.APP_NAME + "/orders" + AppConstants.COMMON + "/**";

	String admin_RoleDetails = AppConstants.APP_NAME + "/roles" + AppConstants.ADMIN + "/**";

	String admin_UserDetails = AppConstants.APP_NAME + "/users" + AppConstants.ADMIN + "/**";
	String user_UserDetails = AppConstants.APP_NAME + "/users" + AppConstants.USER + "/**";
	String common_UserDetails = AppConstants.APP_NAME + "/users" + AppConstants.COMMON + "/**";

}
