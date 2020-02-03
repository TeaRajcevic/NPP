package com.rajcevic.tea.DiaryWebApp.web;

import com.rajcevic.tea.DiaryWebApp.data.ImageRepository;
import com.rajcevic.tea.DiaryWebApp.data.UserRepository;
import com.rajcevic.tea.DiaryWebApp.model.Image;
import com.rajcevic.tea.DiaryWebApp.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@DataJpaTest
public class IntegrationTest {

    private Image testImage1;
    private Image testImage2;
    private Image testImage3;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    public void prepareImagesForTest() {
        testImage1 = new Image();
        testImage1.setTitle("Castle");
        testImage1.setImagedata("ImageData");
        testImage1.setFormat("JPG");
        testImage1.setDescription("Description");
        testImage1.setTags("architecture");
        testImage1.setUploader("Admin");
        testImage1.setSizex("600");
        testImage1.setSizey("400");

        entityManager.persist(testImage1);

        testImage2 = new Image();
        testImage2.setTitle("Palace");
        testImage2.setImagedata("ImageData");
        testImage2.setFormat("PNG");
        testImage2.setDescription("Description");
        testImage2.setTags("architecture");
        testImage2.setUploader("Random User");
        testImage2.setSizex("2560");
        testImage2.setSizey("1440");

        entityManager.persist(testImage2);

        testImage3 = new Image();
        testImage3.setTitle("Tree");
        testImage3.setImagedata("ImageData");
        testImage3.setFormat("PNG");
        testImage3.setDescription("Description");
        testImage3.setTags("nature");
        testImage3.setUploader("Random User");
        testImage3.setSizex("640");
        testImage3.setSizey("480");

        entityManager.persist(testImage3);

        entityManager.flush();
    }

    @Test
    public void whenFindByName_thenReturnUser() {
        // given
        User test = new User("ime", "prezime", "free");
        entityManager.persist(test);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(test.getUsername());

        // then
        assertThat(found.getUsername())
                .isEqualTo(test.getUsername());
    }

    @Test
    public void whenFindByTitle_thenReturnImage() {
        // given
        prepareImagesForTest();

        // when
        List<Image> found = imageRepository.findByTags("architecture");

        // then
        assertThat(found.size()).isEqualTo(2);
        assertThat(found.get(0).getTitle())
                .isEqualTo("Castle");

        assertThat(found.get(1).getTitle())
                .isEqualTo("Palace");

    }

    @Test
    public void whenFindByTags_thenReturnImage() {
        // given
        prepareImagesForTest();

        // when
        List<Image> found = imageRepository.findByTitle("Castle");

        // then
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getTags())
                .isEqualTo("architecture");


    }

    @Test
    public void whenFindByUploader_thenReturnImage() {
        // given
        prepareImagesForTest();
        User test = new User("Random User", "prezime", "free");
        entityManager.persist(test);
        entityManager.flush();

        // when
        List<Image> found = imageRepository.findByUploader("Random User");

        // then
        assertThat(found.size()).isEqualTo(2);
        assertThat(found.get(0).getUploader())
                .isEqualTo("Random User");
    }
}
