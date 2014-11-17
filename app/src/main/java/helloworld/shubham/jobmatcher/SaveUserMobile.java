/*
 * Author: Ross Feehan <rossfeehan>
 * Copyright: JumpIn 2014
 */

package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveUserMobile {

	public static final String USERMOBILEKEY = "userMobileKey";
	
	public static void saveUserMobile(String userMobile, Context ctx){
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		Editor editor = sharedPreferences.edit();
		editor.putString(USERMOBILEKEY, userMobile);
		editor.commit();
	}
	
	public static String getSavedUserMobile(Context ctx){
		String userMobile = null;
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		if(sharedPreferences.contains(USERMOBILEKEY)){
			userMobile = sharedPreferences.getString(USERMOBILEKEY, null);
		}
		return userMobile;
	}

}
