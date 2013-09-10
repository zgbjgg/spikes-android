// -------------------------------------------------------------------
//
//  spike_exec - Run any os android command and redirect to dev (ptmx)
//
// Copyright (c) 2012-2013, Jorge Garrido <zgbjgg@gmail.com>
// All rights reserved.
//
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// -------------------------------------------------------------------

#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <errno.h>
#include <fcntl.h>
#include <termios.h>
#include <signal.h>

int run (const char* cmd, char *const args[], char *const env[]) {
  int pts;
  int ptm;
  char* devname;

  ptm = open("/dev/ptmx", O_RDWR); // | O_NOCTTY);
  if(ptm < 0){
  	return -1;
  }
  
  fcntl(ptm, F_SETFD, FD_CLOEXEC);

  if(grantpt(ptm) || unlockpt(ptm) || ((devname = (char*) ptsname(ptm)) == 0)){
        return -1;
  }

  pid_t pid = fork();

  if(pid < 0) {
        return -1;
  }

  if(pid == 0){
        close(ptm);

        int pts;

        setsid();

        pts = open(devname, O_RDWR);
        if(pts < 0) exit(-1);

        dup2(pts, 0);
        dup2(pts, 1);
        dup2(pts, 2);

	for (; *env; ++env) {
        	putenv(*env);
        }

        execv(cmd, args);
        exit(-1);
  } else {
        return ptm;
  }
}
