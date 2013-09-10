/* -------------------------------------------------------------------
*
* Copyright (c) 2012-2013, Jorge Garrido <zgbjgg@gmail.com>
* All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*
* ------------------------------------------------------------------- */

package com.zgbjgg.spike;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *
 * @author george
 */
public class SpikeUtils {
    
    // The HOME PATH
    public static String HOME_PATH;
    
    /**
     * Transform a string separated by spaces into
     * a string array, since native process requires this 
     * format. Example:
     * 
     *       parseCmd("ls -l /root");
     * 
     * @param cmdArgs The command arguments in 
     *                string format (space separated)
     * 
     * @return The string array splitted 
     */
    public String[] parseCmd(String cmdArgs) {
        String[] cmdArgsArray = cmdArgs.split(" ");
        return cmdArgsArray;
    }
    
    
    /**
     * Transform a string separated by ':' into
     * a string array, since native process requires this 
     * format. Example:
     * 
     *       parseEnv("HOME=/:PATH=/");
     * 
     * @param cmdArgs The command arguments in 
     *                string format (space separated)
     * 
     * @return The string array splitted 
     */
    public String[] parseEnv(String env) {
        String[] envArray = env.split(":");
        return envArray;
    }
    
        
    /**
     * Creates a app_HOME dir to use as HOME DIR for our app, 
     * this is intended because some commands needs the HOME path set.
     *  
     * @param main The Activity class for the apk
     * 
     * @return The HOME full path as string
     */
    public static String setHome(MainActivity main) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(main);
        // the HOME dir needs to be set here since it comes from Context
        SharedPreferences.Editor editor = mPrefs.edit();
        String defValue = main.getDir("HOME", MainActivity.MODE_PRIVATE).getAbsolutePath();
        String homePath = mPrefs.getString("home_path", defValue);
        editor.putString("home_path", homePath);
        editor.commit();
        HOME_PATH = defValue;
        return defValue;
    }
}
