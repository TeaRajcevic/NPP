package com.rajcevic.tea.DiaryWebApp.misc.decorator;

import com.rajcevic.tea.DiaryWebApp.misc.Logger;
import com.rajcevic.tea.DiaryWebApp.misc.factory.Filter;

public class GaussianBlurDecorator extends FilterDecorator {

    Logger LOG = Logger.getInstance();

    public GaussianBlurDecorator(Filter decoratedFilter) {
        super(decoratedFilter);
    }

    @Override
    public String applyFilter() {
        addGaussianBlur(decoratedFilter);
        return decoratedFilter.applyFilter();
    }

    private void addGaussianBlur(Filter decoratedFilter){
        LOG.write("Applying gaussian blur");
    }
}
