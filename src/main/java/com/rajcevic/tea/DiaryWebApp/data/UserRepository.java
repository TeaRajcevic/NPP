package com.rajcevic.tea.DiaryWebApp.data;

import com.rajcevic.tea.DiaryWebApp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom{
    User findByUsername(String username);
}

