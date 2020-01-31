package com.rajcevic.tea.DiaryWebApp.data;

import com.rajcevic.tea.DiaryWebApp.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    List<Image> findByTitle(String title);
    List<Image> findByTags(String tags);
    List<Image> findByUploader(String uploader);
}
