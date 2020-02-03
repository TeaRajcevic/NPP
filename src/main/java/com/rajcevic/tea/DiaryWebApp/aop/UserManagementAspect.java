package com.rajcevic.tea.DiaryWebApp.aop;

import com.rajcevic.tea.DiaryWebApp.data.UserRepository;
import com.rajcevic.tea.DiaryWebApp.model.User;
import com.rajcevic.tea.DiaryWebApp.utils.UserUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserManagementAspect {
    @Autowired
    UserRepository userRepository;

    @AfterReturning("execution(String com.rajcevic.tea.DiaryWebApp.controller.ImageController.processForm(..)))")
    public void reduceUploadForUpload()
    {
        User userFromDb = userRepository.findByUsername(UserUtils.returnLoggedUser());
        userFromDb.setUploadLimit(userFromDb.getUploadLimit() - 1);
        userRepository.save(userFromDb);
    }
}
