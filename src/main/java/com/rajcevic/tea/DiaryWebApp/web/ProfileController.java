package com.rajcevic.tea.DiaryWebApp.web;

import com.rajcevic.tea.DiaryWebApp.DiaryWebAppApplication;
import com.rajcevic.tea.DiaryWebApp.data.UserRepository;
import com.rajcevic.tea.DiaryWebApp.patterns.Logger;
import com.rajcevic.tea.DiaryWebApp.patterns.chain.SystemLogger;
import com.rajcevic.tea.DiaryWebApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;

    Logger LOG = Logger.getInstance();

    SystemLogger logChain = DiaryWebAppApplication.getChainOfLoggers();

    @Autowired
    public ProfileController(UserRepository userRepository) throws IOException {
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public String showGallery(Model model) {
        model.addAttribute("user", userRepository.findByUsername(returnLoggedUser()));
        LOG.logVisit(returnLoggedUser(), "/user");
        return "profilePage";
    }

    @PostMapping("/updatePackage")
    public String processUpdate(@RequestParam(value="account", required=true) String account) throws IOException {
        setNewPackage(account);
        return "redirect:user";
    }

    private void setNewPackage(String account) throws IOException {
        User userFromDb = userRepository.findByUsername(returnLoggedUser());
        String oldPackage = userFromDb.getAccount();
        userFromDb.setAccount(account);
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        userFromDb.setCreatedAt(dt);
        try {
            logChain.logMessage(SystemLogger.INFO, "User: " + returnLoggedUser() + " has changed the account from " + oldPackage + " to: " + userFromDb.getAccount());
            throw new Exception();
        } catch (Exception e) {
            logChain.logMessage(SystemLogger.ERROR, "Unable to save new account info for user: " + returnLoggedUser());
        }
        finally {
            userRepository.save(userFromDb);
        }
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
