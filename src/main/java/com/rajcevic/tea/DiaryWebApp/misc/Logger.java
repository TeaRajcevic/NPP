package com.rajcevic.tea.DiaryWebApp.misc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private final String logFile = "activity.txt";
    private PrintWriter writer;

    private static Logger LOGGER = null;

    private Logger() {
        try {
            FileWriter fw = new FileWriter(logFile);
            writer = new PrintWriter(fw, true);
        } catch (IOException e) {}
    }
    public void logUpload (String user, String title) {
        writer.println(user + " has uploaded " + title);
    }

    public void logEdit (String user, String title) {
        writer.println(user + " has edited " + title);
    }

    public void logVisit (String user, String address) {
        writer.println(user + " has visited " + address);
    }

    public void write (String info) {
        writer.println(info);
    }

    public static synchronized Logger getInstance(){
        if(LOGGER == null)
            LOGGER = new Logger();
        return LOGGER;
    }
}
