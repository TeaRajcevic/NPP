package com.rajcevic.tea.DiaryWebApp.web.JUnit;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.data.UserRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.model.User;
import com.rajcevic.tea.DiaryWebApp.web.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testNewImageGetMethod() throws Exception {
        mvc.perform(get("/image/new"))
                .andExpect(view().name("imageUpload"))
                .andExpect(model().attribute("image", any(Image.class)))
                .andExpect(model().attribute("image", hasProperty("title", isEmptyOrNullString())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testEditImageByIdGetMethodExistingImage() throws Exception {
        List<Image> testImages = TestUtils.buildImageList(1);
        Optional<Image> optionalImageMock = testImages.stream().findFirst();

        Mockito.when(imageRepository.findById(1L)).thenReturn(optionalImageMock);

        mvc.perform(get("/image/edit/1"))
                .andExpect(view().name("imageUpload"))
                .andExpect(model().attribute("image", any(Image.class)))
                .andExpect(model().attribute("image", hasProperty("title", is("testTitle1"))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testEditImageByIdGetMethodMissingImage() throws Exception {
        mvc.perform(get("/image/edit/999"))
                .andExpect(view().name("imageUpload"))
                .andExpect(model().attribute("image", any(Image.class)))
                .andExpect(model().attribute("image", hasProperty("title", isEmptyOrNullString())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "userpass", roles = "USER")
    public void testSorryGetMethod() throws Exception {
        mvc.perform(get("/image/sorry"))
                .andExpect(view().name("sorryPage"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "ADMIN")
    public void testNewImagePostMethod() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("imagedata", "image.jpg", "image/jpeg", "test".getBytes());
        User testUser = new User("testUser", "testPassword", "testAccount");
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        buildTestImageParameters(requestParams);

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);

        this.mvc.perform(MockMvcRequestBuilders.multipart("/image/new")
                .file(imageFile)
                .params(requestParams))
                .andExpect(view().name("redirect:new"))
                .andExpect(redirectedUrl("new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "ADMIN")
    public void testNewImagePostMethodNoCredit() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("imagedata", "image.jpg", "image/jpeg", "test".getBytes());
        User testUserNoCredit = new User("testUser", "testPassword", "testAccount");
        testUserNoCredit.setUploadLimit(0);

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUserNoCredit);

        this.mvc.perform(MockMvcRequestBuilders.multipart("/image/new")
                .file(imageFile))
                .andExpect(view().name("redirect:sorry"))
                .andExpect(redirectedUrl("sorry"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", password = "adminpass", roles = "ADMIN")
    public void testEditImageByIdPostMethod() throws Exception {
        List<Image> testOriginalImages = TestUtils.buildImageList(1);
        Optional<Image> optionalOriginalImageMock = testOriginalImages.stream().findFirst();

        Mockito.when(imageRepository.findById(1L)).thenReturn(optionalOriginalImageMock);

        this.mvc.perform(post("/image/edit/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title", "editedMockTitle")
                .param("description", "editedMockDesc")
                .param("tags", "editedMockTags"))
                .andExpect(view().name("redirect:/gallery"))
                .andExpect(redirectedUrl("/gallery"))
                .andExpect(status().is3xxRedirection());
    }

    private void buildTestImageParameters(LinkedMultiValueMap<String, String> requestParams) {
        requestParams.add("title", "testTitle");
        requestParams.add("description", "testDescription");
        requestParams.add("tags", "testTags");
        requestParams.add("sizex", "1000");
        requestParams.add("sizey", "1500");
        requestParams.add("format", "jpg");
        requestParams.add("filter", "blur");
    }
}
