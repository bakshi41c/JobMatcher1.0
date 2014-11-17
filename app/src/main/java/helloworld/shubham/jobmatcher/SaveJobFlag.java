/*
 * Author: Ross Feehan <rossfeehan>
 * Copyright: JumpIn 2014
 */

package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SaveJobFlag {

	public static final String USERJOBFLAGKEY = "userJobFlagKey";
	
	public static void saveUserJobFlag(String userJobFlag, Context ctx){
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		Editor editor = sharedPreferences.edit();
		editor.putString(USERJOBFLAGKEY, userJobFlag);
		editor.commit();
	}
	
	public static String getSavedUserJobFlag(Context ctx){
		String userJobFlag = null;
		SharedPreferences sharedPreferences = SetupSharedPreferences.setUpSharedPreferences(ctx);
		if(sharedPreferences.contains(USERJOBFLAGKEY)){
			userJobFlag = sharedPreferences.getString(USERJOBFLAGKEY, null);
		}
		return userJobFlag;
	}

}
