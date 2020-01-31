package com.rajcevic.tea.DiaryWebApp.misc.adapter;

import com.rajcevic.tea.DiaryWebApp.misc.Logger;

public class ImageChanger implements ImageColorChanger {

    ImageChangerAdapter adapter;

    Logger LOG = Logger.getInstance();

    @Override
    public void changeColor(String imageFormat, String imageTitle) {
        if(imageFormat.equalsIgnoreCase("jpeg")){
            LOG.write("Changing color on the image: " + imageTitle);
        }

        //adapter providing other functionalities to change
        else if(imageFormat.equalsIgnoreCase("png")){
            adapter = new ImageChangerAdapter(imageFormat);
            adapter.changeColor(imageFormat, imageTitle);

        }

        else{
            LOG.write("Unknown format!");
        }
    }
}
