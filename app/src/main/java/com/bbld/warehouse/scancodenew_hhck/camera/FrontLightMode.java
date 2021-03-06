package com.bbld.warehouse.scancodenew_hhck.camera;


import android.content.SharedPreferences;

import com.bbld.warehouse.scancodenew_hhck.config.Config;

/**
 * Enumerates settings of the prefernce controlling the front light.
 */
public enum FrontLightMode {

	/** Always on. */
	ON,
	/** On only when ambient light is low. */
	AUTO,
	/** Always off. */
	OFF;

	private static FrontLightMode parse(String modeString) {
		return modeString == null ? OFF : valueOf(modeString);
	}

	public static FrontLightMode readPref(SharedPreferences sharedPrefs) {
		return parse(sharedPrefs.getString(
				Config.KEY_FRONT_LIGHT_MODE, null));
	}

}
