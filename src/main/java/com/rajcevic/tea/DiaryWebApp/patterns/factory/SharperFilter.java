package com.rajcevic.tea.DiaryWebApp.patterns.factory;

import com.rajcevic.tea.DiaryWebApp.patterns.Logger;

public class SharperFilter implements Filter {

    Logger LOG = Logger.getInstance();

    @Override
    public String applyFilter() {
        LOG.write("Applied sharper filter");
        return "sharper";
    }
}
