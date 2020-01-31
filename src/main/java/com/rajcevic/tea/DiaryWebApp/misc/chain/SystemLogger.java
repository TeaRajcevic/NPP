package com.rajcevic.tea.DiaryWebApp.misc.chain;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class SystemLogger {
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    protected static final String logFile = "system.txt";

    protected int level;

    protected SystemLogger nextLogger;

    protected PrintWriter writer;

    protected SystemLogger() throws IOException {
    }

    public void setNextLogger(SystemLogger nextLogger){
        this.nextLogger = nextLogger;
    }

    public void logMessage(int level, String message) throws IOException {
        if(this.level >= level){
            write(message);
        }
        if(nextLogger !=null){
            nextLogger.logMessage(level, message);
        }
    }

    abstract protected void write(String message) throws IOException;

}
