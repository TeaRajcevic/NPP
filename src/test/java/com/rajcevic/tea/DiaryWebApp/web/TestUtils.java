package com.rajcevic.tea.DiaryWebApp.web;

import com.rajcevic.tea.DiaryWebApp.model.Image;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static Image createTestImage(int index) {
        Image image = new Image();
        image.setTitle("testTitle" + index);
        image.setDescription("testDescription");
        image.setFilter("testFilter");
        image.setFormat("png");
        image.setTags("testTag" + index);
        image.setSizex("1000");
        image.setSizey("2000");
        image.setImagedata("testImageData");
        return image;
    }

    public static List<Image> buildImageList(int number) {
        List<Image> mockImages = new ArrayList<>();
        for(int i = 1; i <= number; i++) {
            Image mockImage = TestUtils.createTestImage(i);
            mockImages.add(mockImage);
        }
        return mockImages;
    }
}
