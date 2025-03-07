package com.foodtaste.constant;

public class SecurityConstants {

	// 🔹 Admin Endpoints
	public static final String menuItem = AppConstants.APP_NAME + "/items" + AppConstants.ADMIN + "/**";

	// 🔹 User Endpoints
	public static final String userDetails = AppConstants.APP_NAME + AppConstants.USER + "/**";

	// 🔹 Public Endpoints (Available to everyone)
	public static final String public_Auth = AppConstants.APP_NAME + AppConstants.PUBLIC_ROUTE_TYPE + "/**";
	public static final String public_Item = AppConstants.APP_NAME + "/items" + AppConstants.PUBLIC_ROUTE_TYPE + "/**";

}
