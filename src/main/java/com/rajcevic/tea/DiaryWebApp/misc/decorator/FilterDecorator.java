package com.rajcevic.tea.DiaryWebApp.misc.decorator;

import com.rajcevic.tea.DiaryWebApp.misc.factory.Filter;

public  abstract class FilterDecorator implements Filter {
    protected Filter decoratedFilter;

    public FilterDecorator(Filter decoratedFilter){
        this.decoratedFilter = decoratedFilter;
    }

    public String applyFilter() {
        return decoratedFilter.applyFilter();
    }
}
