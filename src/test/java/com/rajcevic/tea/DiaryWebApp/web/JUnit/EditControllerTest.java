package com.rajcevic.tea.DiaryWebApp.web.JUnit;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.web.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageRepository imageRepository;

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "ADMIN")
    public void testGetMethodForAdministrator() throws Exception {
        List<Image> mockImages = TestUtils.buildImageList(3);
        Mockito.when(imageRepository.findAll()).thenReturn(mockImages);

        mvc.perform(get("/edit"))
                .andExpect(flash().attribute("editing", "True"))
                .andExpect(flash().attribute("values", mockImages))
                .andExpect(view().name("redirect:gallery"))
                .andExpect(redirectedUrl("gallery"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testGetMethodForUser() throws Exception {
        List<Image> mockImages = TestUtils.buildImageList(2);
        Mockito.when(imageRepository.findByUploader("user")).thenReturn(mockImages);

        mvc.perform(get("/edit"))
                .andExpect(flash().attribute("editing", "True"))
                .andExpect(flash().attribute("values", mockImages))
                .andExpect(view().name("redirect:gallery"))
                .andExpect(redirectedUrl("gallery"))
                .andExpect(status().is3xxRedirection());
    }
}
