package com.rajcevic.tea.DiaryWebApp.web.JUnit;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageRepository imageRepository;

    @Test
    public void testGetMethod() throws Exception {
        mvc.perform(get("/search"))
                .andExpect(view().name("searchPage"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostMethod() throws Exception {
        //given
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("target", "tags");
        requestParams.add("query", "castle");
        Image testImage = new Image();
        testImage.setTitle("testTitle");
        List<Image> testImages = new ArrayList<>();
        testImages.add(testImage);

        Mockito.when(imageRepository.findByTags("castle")).thenReturn(testImages);

        //when
        this.mvc.perform(post("/search")
                .header("Accept", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .params(requestParams))
                .andExpect(flash().attribute("values", testImages))
                .andExpect(view().name("redirect:gallery"))
                .andExpect(redirectedUrl("gallery"))
                .andExpect(status().is3xxRedirection());
    }
}
