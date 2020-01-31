package com.rajcevic.tea.DiaryWebApp.web;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.misc.Logger;
import com.rajcevic.tea.DiaryWebApp.misc.adapter.ImageChanger;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/edit")
public class EditController {
    private final ImageRepository imageRepository;
    Logger LOG = Logger.getInstance();

    @Autowired
    public EditController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public String editForm(RedirectAttributes redirectAttributes) {
        LOG.logVisit(returnLoggedUser(),"/edit");
        String user = returnLoggedUser();
        if(user.equals("admin")) {
            redirectAttributes.addFlashAttribute("values", imageRepository.findAll());
        }
        else {
            Iterable<Image> userImages = imageRepository.findByUploader(returnLoggedUser());
            redirectAttributes.addFlashAttribute("values", userImages);
        }
        redirectAttributes.addFlashAttribute("editing", "True");



        return "redirect:gallery";
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
