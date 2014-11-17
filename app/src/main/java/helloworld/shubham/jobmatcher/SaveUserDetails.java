/*
 * Author: Ross Feehan <rossfeehan>
 * Copyright: JumpIn 2014
 */

package helloworld.shubham.jobmatcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveUserDetails {

	public static final String USERNAMEKEY = "userNameKey";
	
	public static void saveUserName(String userName, Context ctx){
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		Editor editor = sharedPreferences.edit();
		editor.putString(USERNAMEKEY, userName);
		editor.commit();
	}
	
	public static String getSavedUserName(Context ctx){
		String userName = null;
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		if(sharedPreferences.contains(USERNAMEKEY)){
			userName = sharedPreferences.getString(USERNAMEKEY, null);
		}
		return userName;
	}

}
