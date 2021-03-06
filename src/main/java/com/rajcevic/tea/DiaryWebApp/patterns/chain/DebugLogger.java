package com.rajcevic.tea.DiaryWebApp.patterns.chain;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DebugLogger extends SystemLogger {
    public DebugLogger(int level) throws IOException {
        this.level = level;
        FileWriter fw = new FileWriter(logFile);
        writer = new PrintWriter(fw, true);
    }

    @Override
    protected void write(String message) {
        writer.println("DEBUG: " + message);
    }
}
