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

import java.io.FileDescriptor;

/**
 *
 * @author george
 * 
 * This class is intended for load library and use native functions on it.
 * 
 * Command to create interface (.h) :
 *          javah -classpath ../../bin/classes -o Spike.h com.zgbjgg.spike.SpikeNative
 * 
 */
public class SpikeNative {    
    /*
     * It loads the lib ( Unix .so, Win .dll)
     */
    static {
        System.loadLibrary("spikes");
    }
    
    /**
     * Create a native process on the os system.
     * 
     * @param devname The device where writes command output
     * @param cmd The command to be executed
     * @param args The arguments for the command
     * @param env The environment vars for the command
     * 
     * @return A FileDescriptor object with the process referenced
     */
    public native static FileDescriptor createProcess(String devname, String cmd,
            String[] args, String[] env);
    
    
    /**
     * Tests if library is load correctly.
     * 
     * @return The string message from C code
     */
    public native static String test(); 
    
}
