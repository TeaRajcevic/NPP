package com.rajcevic.tea.DiaryWebApp.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.model.form.SearchForm;
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

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageRepository imageRepository;

    @Test
    public void testSearch() throws Exception {

        //Perform GET method test
        mvc.perform(get("/search")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        SearchForm testForm = new SearchForm();
        testForm.setTarget("tags");
        testForm.setQuery("castle");

        Image testImage = new Image();
        testImage.setTitle("testTitle");
        List<Image> testImages = new ArrayList<>();
        testImages.add(testImage);

        Mockito.when(imageRepository.findByTags("castle")).thenReturn(testImages);

        //Perform POST method test with redirection
        this.mvc.perform(post("/search")
                .header("Accept", "application/json")
                .content(convertObjectToJsonBytes(testForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andDo(MockMvcResultHandlers.print());
    }

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}
