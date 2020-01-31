package com.rajcevic.tea.DiaryWebApp.misc.observer;

import com.rajcevic.tea.DiaryWebApp.misc.Logger;

import java.io.File;

public class LogOpenListener implements EventListener {
    private File log;
    Logger LOG = Logger.getInstance();

    public LogOpenListener(String fileName) {
        this.log = new File(fileName);
    }

    @Override
    public void update(String action, File file) {
        LOG.write("BACKING UP PHOTO");
    }
}
