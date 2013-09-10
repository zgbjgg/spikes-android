/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zgbjgg.spike;

import android.content.SharedPreferences;
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
    
        
    private String DEVNAME = "/dev/ptmx";
    private FileInputStream iostream;
    private FileDescriptor fd;
    
    // Max length in the buffer output
    public static int BUFFER = 1024;
    
    /*
     * API: create new process.
     * 
     */
    public void makePidRef(String cmd, String[] args, String[] env) {
        FileDescriptor fd1 = SpikeNative.createProcess(this.getDEVNAME(), cmd, args, env);
        this.setIostream(new FileInputStream(fd1)); 
        this.setFd(fd1);
    }
    
    public void closePidRef() throws IOException {
        this.getIostream().close();
    }
    
    public String testing() {
        return this.testing();
    }

    /**
     * @return the DEVNAME
     */
    public String getDEVNAME() {
        return DEVNAME;
    }

    /**
     * @param DEVNAME the DEVNAME to set
     */
    public void setDEVNAME(String DEVNAME) {
        this.DEVNAME = DEVNAME;
    }

    /**
     * @return the iostream
     */
    public FileInputStream getIostream() {
        return iostream;
    }

    /**
     * @param iostream the istream to set
     */
    public void setIostream(FileInputStream iostream) {
        this.iostream = iostream;
    }

    /*
     * Retrieves the string buffered
     *
     */
    public String retrieveOutput() throws IOException, InterruptedException {
        Thread.sleep(6000);
        byte buffer[] = new byte[SpikeManager.BUFFER];
        this.getIostream().read(buffer);
        String output = new String(buffer);
        return output;
    }

    public String setHome(MainActivity main) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(main);
        // the HOME dir needs to be set here since it comes from Context
        SharedPreferences.Editor editor = mPrefs.edit();
        String defValue = main.getDir("HOME", MainActivity.MODE_PRIVATE).getAbsolutePath();
        String homePath = mPrefs.getString("home_path", defValue);
        editor.putString("home_path", homePath);
        editor.commit();
        return defValue;
    }

    /**
     * @return the fd
     */
    public FileDescriptor getFd() {
        return fd;
    }

    /**
     * @param fd the fd to set
     */
    public void setFd(FileDescriptor fd1) {
        this.fd = fd1;
    }
}
