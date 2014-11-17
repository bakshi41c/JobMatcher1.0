/*
 * Author: Ross Feehan <rossfeehan>
 * Copyright: JumpIn 2014
 */

package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class saveUserID {

	public static final String USERIDKEY = "userIdKey";
	
	public static void saveUserId(String userId, Context ctx){
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		Editor editor = sharedPreferences.edit();
		editor.putString(USERIDKEY, userId);
		editor.commit();
	}
	
	public static String getSavedUserId(Context ctx){
		String userId = null;
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		if(sharedPreferences.contains(USERIDKEY)){
			userId = sharedPreferences.getString(USERIDKEY, null);
		}
		return userId;
	}

}
