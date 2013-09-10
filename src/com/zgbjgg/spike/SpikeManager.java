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

import android.preference.PreferenceManager;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author george
 * 
 * This class is for manage all process on library native calls.
 * 
 */
public class SpikeManager {
    
    // Device to write/read    
    private String DEVNAME = "/dev/ptmx";
    
    // Stream to catch output
    private FileInputStream iostream;
    
    // Attach to file (dev)
    private FileDescriptor fd;
    
    // Max length in the buffer output
    public static int BUFFER = 1024;
    
    
    /**
     * Creates a new process for a given command, this retrieves the command
     * output as string, the command needs a string representing the arguments
     * for the command and other string representing the environment vars 
     * (this last is optional). 
     * This functions build a fileDescriptor on C and 
     * use it to retrieve output since writes on devname.
     * 
     * 
     * @param cmd The command to execute
     * @param args The arguments for the command (space separated)
     * @param env The environment vars for the command (':' separated)
     * 
     * 
     */
    public void makePidRef(String cmd, String args, String env) {
        SpikeUtils utils = new SpikeUtils();
        String finalenv = env + "HOME=" + SpikeUtils.HOME_PATH;
        FileDescriptor fd1 = SpikeNative.createProcess(this.getDEVNAME(), 
                cmd, utils.parseCmd(args), utils.parseEnv(finalenv));
        this.iostream = new FileInputStream(fd1); 
        this.fd = fd1;
    }
    
    /**
     * Closes the stream opened to read the command output.
     * 
     */
    public void closePidRef() throws IOException {
        this.getIostream().close();
    }
    
    /**
     * Call native process on C that retrieves a string, it is intended 
     * to check if spikes lib is loaded.
     * 
     * @return The string set in C code
     */
    public String testing() {
        return this.testing();
    }

    /**
     * Get the device name for the FileDescriptor
     * 
     * @return the DEVNAME
     */
    public String getDEVNAME() {
        return DEVNAME;
    }

    /**
     * Set the device name for the FileDescriptor
     * 
     * @param DEVNAME the DEVNAME to set
     */
    public void setDEVNAME(String DEVNAME) {
        this.DEVNAME = DEVNAME;
    }

    /**
     * Get the stream attached to the FileDescriptor
     * 
     * @return the iostream
     */
    public FileInputStream getIostream() {
        return iostream;
    }


    /**
     * Read the bytes from the stream and retrieves as string well-formed.
     * 
     * @param Milliseconds The milliseconds is a value to wait before command is 
     *                     succesfully executed
     * 
     * @return The string containing the command output
     *
     */
    public String retrieveOutput(int Milliseconds) throws IOException, InterruptedException {
        Thread.sleep(Milliseconds);
        byte buffer[] = new byte[SpikeManager.BUFFER];
        this.getIostream().read(buffer);
        String output = new String(buffer);
        return output;
    }

}
