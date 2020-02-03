package com.rajcevic.tea.DiaryWebApp.controller;

import com.rajcevic.tea.DiaryWebApp.DiaryWebAppApplication;
import com.rajcevic.tea.DiaryWebApp.patterns.Logger;
import com.rajcevic.tea.DiaryWebApp.patterns.chain.SystemLogger;
import com.rajcevic.tea.DiaryWebApp.utils.ImageUtils;
import com.rajcevic.tea.DiaryWebApp.utils.UserUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    private Logger LOG = Logger.getInstance();
    private SystemLogger logChain = DiaryWebAppApplication.getChainOfLoggers();
    private final ImageRepository imageRepository;

    @Autowired
    public GalleryController(ImageRepository imageRepository) throws IOException {
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public String showGallery(Model model) {
        List<Image> rawImages;
        List<Image> thumbnails = new ArrayList<>();
        if(model.containsAttribute("values")) {
            rawImages = (List<Image>)model.asMap().get("values");
            createThumbnailList(rawImages, thumbnails);
        }
        else {
            rawImages = (List<Image>)imageRepository.findAll();
            createThumbnailList(rawImages, thumbnails);
        }
        model.addAttribute("thumbnails", thumbnails);
        model.addAttribute("rawImages", rawImages);
        LOG.logVisit(UserUtils.returnLoggedUser(), "/gallery");
        return "imageGallery";
    }

    private void createThumbnailList(List<Image> rawImages, List<Image> thumbnails) {
        rawImages.stream().forEach(image -> {
            try {
                createImageThumbnails(thumbnails, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createImageThumbnails(List<Image> thumbnails, Image img) throws IOException {
        Image temp = new Image();
        temp.setId(img.getId());
        temp.setImagedata(img.getImagedata());
        temp.setFormat(img.getFormat());
        temp.setTitle(img.getTitle());
        thumbnails.add(createThumbnail(temp));
        logChain.logMessage(SystemLogger.DEBUG, "Created thumbnail: " + img.getTitle());
    }

    private Image createThumbnail(Image img) throws IOException {
        byte[] raw = Base64.decodeBase64(img.getImagedata());
        ByteArrayInputStream bis = new ByteArrayInputStream(raw);
        BufferedImage imageForThumbnail = ImageUtils.resizeImage(ImageIO.read(bis), 100, 100);
        return ImageUtils.writeImage(img, imageForThumbnail);
    }
}
