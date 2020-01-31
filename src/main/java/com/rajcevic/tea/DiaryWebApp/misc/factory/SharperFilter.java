package com.rajcevic.tea.DiaryWebApp.misc.factory;

import com.rajcevic.tea.DiaryWebApp.misc.Logger;

public class SharperFilter implements Filter {

    Logger LOG = Logger.getInstance();

    @Override
    public String applyFilter() {
        LOG.write("Applied sharper filter");
        return "sharper";
    }
}
