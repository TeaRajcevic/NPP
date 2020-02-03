package com.rajcevic.tea.DiaryWebApp.patterns.adapter;

public class ImageChangerAdapter implements ImageColorChanger {

    ImageColorAndTransparencyChanger transparencyChanger;

    public ImageChangerAdapter(String format){

        if(format.equalsIgnoreCase("png") ){
            transparencyChanger = new TransparencyChanger();
        }
    }

    @Override
    public void changeColor(String imageFormat, String imageTitle) {
        if (imageFormat.equalsIgnoreCase("png")) {
            transparencyChanger.changeTransparency(imageTitle);
        }
    }
}
