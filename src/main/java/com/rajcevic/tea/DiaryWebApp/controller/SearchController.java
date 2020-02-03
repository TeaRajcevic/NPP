package com.rajcevic.tea.DiaryWebApp.controller;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.patterns.Logger;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.model.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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
    public String processSearch(@RequestParam MultiValueMap body, RedirectAttributes redirectAttributes) {
        Iterable<Image> filtered = new ArrayList<>();
        String target = body.getFirst("target").toString();
        String query = body.getFirst("query").toString();
        switch (target) {
            case "title":
                filtered = imageRepository.findByTitle(query);
                break;
            case "tags":
                filtered = imageRepository.findByTags(query);
                break;
            case "uploader":
                filtered = imageRepository.findByUploader(query);
                break;
        }

        redirectAttributes.addFlashAttribute("values", filtered);
        return "redirect:gallery";
    }
}
