package com.foodtaste.constant;

public class SecurityConstants {

	// 🔹 Admin Endpoints
	public static final String menuItem = AppConstants.APP_NAME + "/items" + AppConstants.ADMIN + "/**";

	// 🔹 User Endpoints
	public static final String userDetails = AppConstants.APP_NAME + AppConstants.USER + "/**";

	public static final String cart = AppConstants.APP_NAME + "/cart" + AppConstants.USER + "/**";

	// 🔹 Public Endpoints (Available to everyone)
	public static final String public_Auth = AppConstants.APP_NAME + AppConstants.PUBLIC_ROUTE_TYPE + "/**";
	public static final String public_Item = AppConstants.APP_NAME + "/items" + AppConstants.PUBLIC_ROUTE_TYPE + "/**";

	public static final String admin_user = AppConstants.APP_NAME + "/users" + AppConstants.ADMIN + "/**";
	public static final String common_user = AppConstants.APP_NAME + "/users" + AppConstants.COMMON + "/**";
	public static final String user = AppConstants.APP_NAME + "/users" + AppConstants.USER + "/**";

	public static final String common_Profile = AppConstants.APP_NAME + "/users" + AppConstants.COMMON + "/**";

}
