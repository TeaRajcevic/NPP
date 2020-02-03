package com.rajcevic.tea.DiaryWebApp.web;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.patterns.Logger;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.model.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final ImageRepository imageRepository;
    Logger LOG = Logger.getInstance();

    @Autowired
    public SearchController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public String searchForm() {
        LOG.logVisit("Anonymous", "/register");
        return "searchPage";
    }

    @PostMapping
    public String processSearch(@Valid @RequestBody SearchForm form, Model model, RedirectAttributes redirectAttributes) {
        Iterable<Image> filtered = new ArrayList<>();
        String searchby = form.getTarget();
        switch (searchby) {
            case "title":
                filtered = imageRepository.findByTitle(form.getQuery());
                break;
            case "tags":
                filtered = imageRepository.findByTags(form.getQuery());
                break;
            case "uploader":
                filtered = imageRepository.findByUploader(form.getQuery());
                break;
        }

        redirectAttributes.addFlashAttribute("values", filtered);
        return "redirect:gallery";
    }
}
