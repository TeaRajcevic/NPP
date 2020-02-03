package com.rajcevic.tea.DiaryWebApp.controller;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;

import com.rajcevic.tea.DiaryWebApp.data.UserRepository;
import com.rajcevic.tea.DiaryWebApp.patterns.observer.Editor;
import com.rajcevic.tea.DiaryWebApp.patterns.observer.EmailNotificationListener;
import com.rajcevic.tea.DiaryWebApp.patterns.observer.LogOpenListener;
import com.rajcevic.tea.DiaryWebApp.patterns.Logger;
import com.rajcevic.tea.DiaryWebApp.patterns.adapter.ImageChanger;
import com.rajcevic.tea.DiaryWebApp.patterns.factory.Filter;
import com.rajcevic.tea.DiaryWebApp.patterns.factory.FilterFactory;
import com.rajcevic.tea.DiaryWebApp.patterns.prototype.ImageCache;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    Logger LOG = Logger.getInstance();

    Editor editor = new Editor();

    FilterFactory filterFactory = new FilterFactory();

    ImageChanger colorChanger = new ImageChanger();

    @Autowired
    public ImageController(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;

        editor.events.subscribe("open", new LogOpenListener("activity.txt"));
        editor.events.subscribe("save", new EmailNotificationListener("backup@gmail.com"));
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("image", new Image());
        LOG.logVisit(returnLoggedUser(), "/new");
        return "imageUpload";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(Model model, RedirectAttributes redirectAttributes, @PathVariable Long id) {
        Image original = imageRepository.findById(id).orElse(new Image());
        model.addAttribute("image", original);
        LOG.logVisit(returnLoggedUser(), "/edit");
        return "imageUpload";
    }

    @GetMapping("/sorry")
    public String showSorry(Model model) {
        LOG.logVisit(returnLoggedUser(), "/sorry");
        return "sorryPage";
    }

    @PostMapping("new")
    public String processForm(@Valid @ModelAttribute Image image, BindingResult bindingResult, @RequestParam(value="imagedata", required=true) Part file) {
        if(hasNoMoreUploads() == true) {
            return "redirect:sorry";
        }
        if(file != null) {
            try
            {
                InputStream inputStream = file.getInputStream();
                BufferedImage resized = processImage(inputStream, image.getSizex(), image.getSizey());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(resized, image.getFormat().toLowerCase(), baos );
                byte[] imageInByte=baos.toByteArray();
                image.setImagedata(Base64.encodeBase64String(imageInByte));

            }catch(IOException ex) {
            }
        }

        image.setUploader(returnLoggedUser());
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        image.setCreatedAt(dt);

        LOG.logUpload(returnLoggedUser(), image.getTitle());

        Filter filter = filterFactory.getFilter(image.getFilter());
        image.setFilter(filter.applyFilter());

        imageRepository.save(image);
        reduceUpload();


        try {
            editor.openFile("activity.txt");
            editor.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:new";
    }

    @PostMapping("/edit/{id}")
    public String processForm2(@Valid @ModelAttribute Image image, BindingResult bindingResult) {
        Image original = imageRepository.findById(image.getId()).orElse(new Image());
        original.setDescription(image.getDescription());
        original.setTitle(image.getTitle());
        original.setTags(image.getTags());

        try {
            //prototype
            ImageCache.loadCache(imageRepository);

            //adapter
            if(original.getFormat().equals("png")) {
                colorChanger.changeColor("png", original.getTitle());
            }
            else {
                colorChanger.changeColor("jpeg", original.getTitle());

            }

            imageRepository.save(original);
            LOG.logEdit(returnLoggedUser(), original.getTitle());

            throw new Exception();
        } catch (Exception e) {
            Image clonedImage = ImageCache.getImage(image.getId().toString());
            imageRepository.save(clonedImage);
            LOG.write("EDIT Failed - Saving backup clone to image repository");
        }

        return "redirect:/gallery";
    }

    public BufferedImage processImage(InputStream file, String sizeX, String sizeY) throws IOException {
        BufferedImage image = resizeImage(ImageIO.read(file), Integer.parseInt(sizeX), Integer.parseInt(sizeY));
        return image;
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

    void reduceUpload() {
        User userFromDb = userRepository.findByUsername(returnLoggedUser());
        userFromDb.setUploadLimit(userFromDb.getUploadLimit() - 1);
        userRepository.save(userFromDb);
    }

    private boolean hasNoMoreUploads() {
        User userFromDb = userRepository.findByUsername(returnLoggedUser());
        if(userFromDb.getUploadLimit() <= 0) {
            return true;
        }
        return false;
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
