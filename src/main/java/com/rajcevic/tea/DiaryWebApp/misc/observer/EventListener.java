package com.rajcevic.tea.DiaryWebApp.misc.observer;

import java.io.File;

public interface EventListener {
        void update(String eventType, File file);
}
