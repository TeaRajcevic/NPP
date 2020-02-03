package com.rajcevic.tea.DiaryWebApp.controller;

import com.rajcevic.tea.DiaryWebApp.DiaryWebAppApplication;
import com.rajcevic.tea.DiaryWebApp.patterns.Logger;
import com.rajcevic.tea.DiaryWebApp.patterns.chain.SystemLogger;
import org.apache.tomcat.util.codec.binary.Base64;
import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

    private final ImageRepository imageRepository;

    @Autowired
    public GalleryController(ImageRepository imageRepository) throws IOException {
        this.imageRepository = imageRepository;
    }

    Logger LOG = Logger.getInstance();

    SystemLogger logChain = DiaryWebAppApplication.getChainOfLoggers();

    @GetMapping
    public String showGallery(Model model) throws IOException {
        Iterable<Image> rawImages = new ArrayList<>();
        List<Image> thumbnails = new ArrayList<>();
        if(model.containsAttribute("values")) {
            rawImages = (List<Image>)model.asMap().get("values");

            for(Image img : rawImages) {
                createImageThumbnails(thumbnails, img);
            }
        }
        else {
            rawImages = imageRepository.findAll();

            for(Image img : imageRepository.findAll()) {
                createImageThumbnails(thumbnails, img);
            }
        }

        model.addAttribute("thumbnails", thumbnails);
        model.addAttribute("rawImages", rawImages);
        LOG.logVisit(returnLoggedUser(), "/gallery");
        return "imageGallery";
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
        BufferedImage bufferedImage = ImageIO.read(bis);
        BufferedImage resized =resizeImage(bufferedImage, 100, 100);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resized, img.getFormat().toLowerCase(), baos);
        byte[] imageInByte=baos.toByteArray();
        img.setImagedata(Base64.encodeBase64String(imageInByte));
        return img;
    }

    public static BufferedImage resizeImage(final BufferedImage image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    private String returnLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
