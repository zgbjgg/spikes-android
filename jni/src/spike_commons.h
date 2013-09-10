// -------------------------------------------------------------------
//
//  spike_commons - Contains initialization for libspikes.so library
//
// Copyright (c) 2012-2013, Jorge Garrido <zgbjgg@gmail.com>
// All rights reserved.
//
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// -------------------------------------------------------------------

#include <android/log.h>
#define LOG_TAG "[spike-log]"
#define LOGI(...) do { __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__); } while(0)

//
// This is called by the VM when the shared library is first loaded.
// 

typedef union {
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;


/*
 * On load 
 */
jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;

    LOGI("JNI_OnLoad");

    if ((*vm)->GetEnv(vm, &uenv.venv, JNI_VERSION_1_4) != JNI_OK) {
        goto bail;
    }
    env = uenv.env;

    LOGI("JNI_VERSION %d\n",result);
    result = JNI_VERSION_1_4;

bail:
    LOGI("JNI_VERSION %d\n", result);
    return result;
}


