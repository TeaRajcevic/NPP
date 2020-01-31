package com.rajcevic.tea.DiaryWebApp.misc.observer;

import com.rajcevic.tea.DiaryWebApp.misc.Logger;

import java.io.File;

public class EmailNotificationListener implements EventListener {
    private String email;

    Logger LOG = Logger.getInstance();

    public EmailNotificationListener(String email) {
        this.email = email;
    }

    @Override
    public void update(String eventType, File file) {
        LOG.write("Email to " + email + ": BACKUP DONE ON FILE. Saved to: " + file.getName());
    }
}