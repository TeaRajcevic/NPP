package com.rajcevic.tea.DiaryWebApp.data;

import com.rajcevic.tea.DiaryWebApp.model.User;

public interface UserRepositoryCustom {
    void saveUserWithAuthorities(User user);
}
