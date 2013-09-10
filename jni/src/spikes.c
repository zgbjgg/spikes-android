// -------------------------------------------------------------------
//
//  spikes - Contains interface between Java & C
//
// Copyright (c) 2012-2013, Jorge Garrido <zgbjgg@gmail.com>
// All rights reserved.
//
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// -------------------------------------------------------------------

// System includes
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

// Spike project includes
#include "spike_commons.h"
#include "spike.h"
#include "spike_exec.h"

JNIEXPORT jstring JNICALL Java_com_zgbjgg_spike_SpikeNative_test (JNIEnv *env, jclass _ignore)
{
  return (*env)->NewStringUTF(env, "Hello From C to Java vía JNI");
}

JNIEXPORT jobject Java_com_zgbjgg_spike_SpikeNative_createProcess (JNIEnv *env, jclass _ignore, jstring devicename, 
								   jstring cmd, jobjectArray args, jobjectArray args_env)
{
  // Field for the FileDescriptor, is an int representing the forked process 
  jfieldID field_fd;

  // For FileDescriptor class 
  jmethodID const_fdesc;
  jclass class_fdesc, class_ioex;
  
  // Return object
  jobject objfd;

  // Integer for forked process
  int fd;

  // devname (device), cmd, args, env
  const char *devname;
  const char *command;

  // Find the classes for FileDescriptor & IOException
  class_ioex = (*env)->FindClass(env, "java/io/IOException");
  if (class_ioex == NULL) { 
	LOGI("CANNOT FIND CLASS IOException\n");
	return NULL;
  }
  class_fdesc = (*env)->FindClass(env, "java/io/FileDescriptor");
  if (class_fdesc == NULL) {
	LOGI("CANNOT FIND CLASS FileDescriptor\n");
	return NULL;
  }

  // Recover JNI vars from java & run the command! 
  devname = (*env)->GetStringUTFChars(env, devicename, NULL);
  command = (*env)->GetStringUTFChars(env, cmd, NULL);
  
  jsize size_args = (*env)->GetArrayLength(env, args);  
  char **arguments = NULL;
  int i;
  arguments = (char **)malloc((size_args+1)*sizeof(char *));
  for (i=0; i < size_args; i++) {
      jstring arg = (jstring) (*env)->GetObjectArrayElement( env, args, i);
      arguments[i] = strdup((*env)->GetStringUTFChars(env, arg, NULL));
      // (*env)->ReleaseStringUTFChars(env, env, arg);
  } 
  arguments[size_args] = NULL; 


  jsize size_env = (*env)->GetArrayLength(env, args_env);
  char **environment = NULL;
  environment = (char **)malloc((size_env+1)*sizeof(char *));
  for (i=0; i < size_env; i++) {
      jstring arg = (jstring) (*env)->GetObjectArrayElement( env, args_env, i);      
      environment[i] = strdup((*env)->GetStringUTFChars(env, arg, NULL));
      // (*env)->ReleaseStringUTFChars(env, env, arg);
  } 
  environment[size_env] = NULL;
  
  LOGI("EXECUTING CMD %s\n", command);
  
  fd = run(command, arguments, environment);

  // Free
  (*env)->ReleaseStringUTFChars(env, cmd, command);
  (*env)->ReleaseStringUTFChars(env, devicename, devname);

  if (fd < 0) {
    // Open returned an error. Throw an IOException with the error string
    char buf[1024];
    (*env)->ThrowNew(env, class_ioex, buf);
    LOGI("Throwed IOException\n");
    return NULL;
  }

  // Construct a new FileDescriptor
  const_fdesc = (*env)->GetMethodID(env, class_fdesc, "<init>", "()V");
  if (const_fdesc == NULL) {
	LOGI("CANNOT BUILD OBJECT FOR CLASS FileDescriptor\n");
	return NULL;
  }
  objfd = (*env)->NewObject(env, class_fdesc, const_fdesc);

  // Pack the "fd" field with the file descriptor
  field_fd = (*env)->GetFieldID(env, class_fdesc, "descriptor", "I");
  if (field_fd == NULL) {
	 LOGI("CANNOT SET FIELD ID FOR OBJECT FileDescriptor\n");
	 return NULL;
   }
   (*env)->SetIntField(env, objfd, field_fd, fd);

  // Return the new object
  return objfd;
}
