package com.rajcevic.tea.DiaryWebApp.patterns.prototype;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;

import java.util.Hashtable;

public class ImageCache {
    private static Hashtable<String, Image> shapeMap  = new Hashtable<String, Image>();

    public static Image getImage(String imageId) {
        Image cachedImage = shapeMap.get(imageId);
        return (Image) cachedImage.clone();
    }

    public static void loadCache(ImageRepository repository) {
        for (Image image : repository.findAll()) {
            shapeMap.put(image.getId().toString(), image);
        }
    }
}
