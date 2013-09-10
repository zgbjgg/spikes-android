/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zgbjgg.spike;

import java.io.FileDescriptor;
import java.io.FileInputStream;

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
    
    /*
     * Create a native process on the os system
     * 
     */
    public native static FileDescriptor createProcess(String devname, String cmd,
            String[] args, String[] env);
    
    
    /*
     * Tests if library is load correctly.
     * 
     */
    public native static String test(); 
    
}
