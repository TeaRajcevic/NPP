package com.rajcevic.tea.DiaryWebApp.patterns.factory;

import com.rajcevic.tea.DiaryWebApp.patterns.Logger;

public class BlurFilter implements Filter {
    Logger LOG = Logger.getInstance();

    @Override
    public String applyFilter() {
        LOG.write("Applied blur filter");
        return "blur";
    }
}
