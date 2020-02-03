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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GalleryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageRepository imageRepository;

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "ADMIN")
    public void testGetMethodForSelectedImages() throws Exception {
        List<Image> mockImageList = TestUtils.buildImageList(2);
        mvc.perform(get("/gallery")
                .flashAttr("values", mockImageList))
                .andExpect(model().attribute("thumbnails", hasSize(2)))
                .andExpect(model().attribute("rawImages", hasSize(2)))
                .andExpect(view().name("imageGallery"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "ADMIN")
    public void testGetMethodForAllImages() throws Exception {
        List<Image> mockImageList = TestUtils.buildImageList(5);
        Mockito.when(imageRepository.findAll()).thenReturn(mockImageList);

        mvc.perform(get("/gallery"))
                .andExpect(model().attribute("thumbnails", hasSize(5)))
                .andExpect(model().attribute("rawImages", hasSize(5)))
                .andExpect(view().name("imageGallery"))
                .andExpect(status().isOk());
    }



}
