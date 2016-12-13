package com.epam.dlab.process;
/*
Copyright 2016 EPAM Systems, Inc.
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import com.aegisql.conveyor.Expireable;
import com.aegisql.conveyor.Testing;
import com.aegisql.conveyor.TimeoutAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.epam.dlab.process.ProcessStatus.*;

public class ProcessInfoBuilder implements Supplier<ProcessInfo>, Testing, Expireable, TimeoutAction {

    private final static Logger LOG = LoggerFactory.getLogger(ProcessInfoBuilder.class);

    private final ProcessId processId;
    private final long startTimeStamp = System.currentTimeMillis();
    private ProcessStatus status = CREATED;
    private final StringBuilder stdOut = new StringBuilder();
    private final StringBuilder stdErr = new StringBuilder();
    private int exitCode = -1;
    private String command = "N/A";
    private Collection<ProcessInfo> rejected = null;

    private boolean finished     = false;
    private boolean stdOutClosed = false;
    private boolean stdErrClosed = false;

    private Process p = null;
    private long expirationTime = Long.MAX_VALUE;
    private CompletableFuture<ProcessInfo> future;

    public ProcessInfoBuilder(ProcessId processId) {
        this.processId = processId;
    }

    public static void schedule(ProcessInfoBuilder b, String command) {
        b.status = SCHEDULED;
        b.command = command;
    }

    public static void start(ProcessInfoBuilder b, String command) {
        if( b.status == CREATED ) {
            b.status = LAUNCHING;
            b.command = command;
            b.launch();
        } else {
            if(b.rejected == null) {
                b.rejected = new LinkedList<>();
            }
            long timeStamp = System.currentTimeMillis();
            b.rejected.add(new ProcessInfo(
                    b.processId,
                    REJECTED,
                    command,
                    "",
                    "rejected duplicated command",
                    REJECTED.ordinal(),
                    timeStamp,
                    timeStamp,null));
        }
    }

    public static void failed(ProcessInfoBuilder b, Object dummy) {
        b.status       = FAILED;
        b.setReady();
    }

    public static void stop(ProcessInfoBuilder b, Object dummy) {
        if(b.p != null) {
            b.p.destroy();
        }
        if(b.status != LAUNCHING && b.status != RUNNING) {
            b.setReady();
        }
        b.status = STOPPED;
    }

    public static void kill(ProcessInfoBuilder b, Object dummy) {
        if(b.p != null) {
            b.p.destroyForcibly();
        }
        if(b.status != LAUNCHING && b.status != RUNNING) {
            b.setReady();
        }
        b.status = KILLED;
    }

    public static void finish(ProcessInfoBuilder b, Integer exitCode) {
        if(b.status != STOPPED && b.status != KILLED ) {
            b.status = FINISHED;
        }
        b.exitCode = exitCode;
        b.finished = true;
    }

    public static void stdOut(ProcessInfoBuilder b, Object msg) {
        if (msg == null) {
            b.stdOutClosed = true;
        } else {
            b.stdOut.append(msg).append("\n");
        }
    }

    public static void stdErr(ProcessInfoBuilder b, Object msg) {
        if (msg == null) {
            b.stdErrClosed = true;
        } else {
            b.stdErr.append(msg).append("\n");
        }
    }

    private void launch() {
        DlabProcess.getInstance().getUsersExecutorService(processId.getUser()).submit(()->{
            status = SCHEDULED;
            DlabProcess.getInstance().getExecutorService().execute(()->{
                try {
                    p = new ProcessBuilder(command.split("\\s+")).start();
                    InputStream stdOutStream = p.getInputStream();
                    DlabProcess.getInstance().getExecutorService().execute(()->{
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stdOutStream));
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                DlabProcess.getInstance().toStdOut(processId,line);
                            }
                            DlabProcess.getInstance().toStdOut(processId,null);
                        } catch (IOException e) {
                            DlabProcess.getInstance().toStdErr(processId,"Failed process STDOUT reader",e);
                            DlabProcess.getInstance().failed(processId);
                        }
                    });
                    InputStream stdErrStream = p.getErrorStream();
                    DlabProcess.getInstance().getExecutorService().execute(()->{
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stdErrStream));
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                DlabProcess.getInstance().toStdErr(processId,line);
                            }
                            DlabProcess.getInstance().toStdErr(processId,null);
                        } catch (IOException e) {
                            DlabProcess.getInstance().toStdErr(processId,"Failed process STDERR reader",e);
                            DlabProcess.getInstance().failed(processId);
                        }
                    });
                    status = RUNNING;
                    int exit = p.waitFor();
                    DlabProcess.getInstance().finish(processId,exit);
                } catch (IOException e) {
                    DlabProcess.getInstance().toStdErr(processId,"Command launch failed. "+get().getCommand(),e);
                    DlabProcess.getInstance().failed(processId);
                } catch (InterruptedException e) {
                    DlabProcess.getInstance().toStdErr(processId,"Command interrupted. "+get().getCommand(),e);
                    DlabProcess.getInstance().failed(processId);
                }
            });
            try {
                future.get();
            } catch (Exception e) {

            }
        });
    }

    @Override
    public ProcessInfo get() {
        return new ProcessInfo(
                processId,
                status,
                command,
                stdOut.toString(),
                stdErr.toString(),
                exitCode,
                startTimeStamp, System.currentTimeMillis(),rejected );
    }

    @Override
    public boolean test() {
        return finished && stdOutClosed && stdErrClosed;
    }

    private void setReady() {
        finished     = true;
        stdOutClosed = true;
        stdErrClosed = true;
    }

    @Override
    public long getExpirationTime() {
        return expirationTime;
    }

    public static void future(ProcessInfoBuilder b, CompletableFuture<ProcessInfo> future) {
        b.future = future;
    }

    @Override
    public void onTimeout() {
        kill(this,"KILL");
    }
}
