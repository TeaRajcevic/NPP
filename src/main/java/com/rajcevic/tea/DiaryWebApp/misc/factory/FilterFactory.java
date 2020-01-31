package com.rajcevic.tea.DiaryWebApp.misc.factory;

import com.rajcevic.tea.DiaryWebApp.misc.decorator.GaussianBlurDecorator;

public class FilterFactory {
    public Filter getFilter(String filterType){
        if(filterType == null){
            return null;
        }
        if(filterType.equalsIgnoreCase("blur")){
            return new BlurFilter();

        } else if(filterType.equalsIgnoreCase("sharper")) {
            return new SharperFilter();
        }
        else if(filterType.equalsIgnoreCase("gaussian")) {
            return new GaussianBlurDecorator(new BlurFilter());
        }
        return null;
    }
}
