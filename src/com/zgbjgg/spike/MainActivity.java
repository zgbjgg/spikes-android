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

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

public class MainActivity extends Activity {    

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // First to all create HOME_DIR
        SpikeUtils.setHome(this);
        
        
        /*
         * Follow the steps to use the spikes library:
         * 
         * 1.- Creates an instance of SpikeManager
         * 2.- Define cmd, args and env vars
         * 3.- Crates the native process
         * 4.- Read output
         * 5.- Close the native process
         * 
         * Do whatever you want with the string output
         * 
         * Enjoy!!
         * 
         * 
         */
        
        // Cretaes a manager
        SpikeManager spikesman = new SpikeManager(); 
        
        // Run process
        String cmd = Spikes.LS;
        String args = cmd + " -l /mnt/sdcard/";
        String env = "";
        spikesman.makePidRef(cmd, args, env);
        
        // To store output
        String spikeout;
        try {
            spikeout = spikesman.retrieveOutput(1000);
            
            // Close process
            spikesman.closePidRef();           
        } catch (Exception ex) {
            spikeout = ex.toString();
        }   
        
        // To alert
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Fun with spikes!");
        alertDialog.setMessage(spikeout);
        alertDialog.show();
    }
    
    
}
