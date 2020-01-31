package com.rajcevic.tea.DiaryWebApp.misc.adapter;

import com.rajcevic.tea.DiaryWebApp.misc.Logger;

public class TransparencyChanger implements ImageColorAndTransparencyChanger {

    Logger LOG = Logger.getInstance();

    @Override
    public void changeTransparency(String imageName) {
        LOG.write("Changing transparency on " + imageName);
    }
}
