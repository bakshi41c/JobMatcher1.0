/*
 * Author: Ross Feehan <rossfeehan>
 * Copyright: JumpIn 2014
 */

package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.content.SharedPreferences;

public class SetupSharedPreferences {
	
	public static SharedPreferences setUpSharedPreferences(Context ctx){
		final String file = "JumpInPreferences";
		SharedPreferences sp = ctx.getSharedPreferences(file, Context.MODE_PRIVATE);
		return sp;
	}

}
