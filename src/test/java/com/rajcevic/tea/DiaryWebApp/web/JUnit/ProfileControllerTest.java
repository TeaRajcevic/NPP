package com.rajcevic.tea.DiaryWebApp.web.JUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testProfileGetMethod() throws Exception {
        mvc.perform(get("/profile/user"))
                .andExpect(view().name("profilePage"))
                .andExpect(model().attribute("user", hasProperty("username", is("user"))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testPostMethod() throws Exception {
        this.mvc.perform(post("/profile/updatePackage")
                .param("account", "gold"))
                .andExpect(view().name("redirect:user"))
                .andExpect(redirectedUrl("user"))
                .andExpect(status().is3xxRedirection());
    }

}
