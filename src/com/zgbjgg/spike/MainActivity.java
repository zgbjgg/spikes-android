package com.zgbjgg.spike;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity {    

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Cretaes a manager
        SpikeManager spikesman = new SpikeManager();
        
        // Set HOME
        spikesman.setHome(this);  
        
        // Run process
        String cmd = Spikes.ESCRIPT_PATH;
        String[] args = {cmd, "/mnt/sdcard/spike-client/spike-node-tool", "zgbjgg@192.168.24.51", "zgbjgg", "rpc", "lists", "seq", "[1,2]"};
        String[] env = {"HOME=/data/data/com.zgbjgg.spike/app_HOME"};
        spikesman.makePidRef(cmd, args, env);
        
        String spikeout = null;
        try {
            spikeout = spikesman.retrieveOutput();
            spikesman.closePidRef();           
        } catch (Exception ex) {
            spikeout = ex.toString();
        }   
        
        // to alert
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Fun with spikes!");
        alertDialog.setMessage(spikeout);
        alertDialog.show();
    }
    
    
}
