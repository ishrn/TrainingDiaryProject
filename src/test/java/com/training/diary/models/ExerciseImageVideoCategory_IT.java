package com.training.diary.models;

import com.training.diary.models.*;
import lombok.SneakyThrows;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.rowset.serial.SerialBlob;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExerciseImageVideoCategory_IT {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private Long exerciseId;

    private final String filePath="src/test/resources/img.png";
    private final String videoPath="src/test/resources/video.mp4";

    @BeforeAll
    public void setupBeforeAll(){
        TransactionStatus transaction = platformTransactionManager.getTransaction(TransactionDefinition.withDefaults());
        Exercise exercise = Exercise.builder().build();
        entityManager.persist(exercise);
        platformTransactionManager.commit(transaction);
        exerciseId=exercise.getId();
    }

    @AfterAll
    public void setupAfterAll(){
        if (entityManager!=null&&entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @SneakyThrows
    public Image getImage(){
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        Blob blob = new SerialBlob(bytes);
        Image image = new Image(blob);
        return image;
    }

    @SneakyThrows
    public Video getVideo(){
        byte[] bytes = Files.readAllBytes(Paths.get(videoPath));
        Blob blob = new SerialBlob(bytes);
        Video video = new Video(blob);
        return video;
    }

    @Test
    public void should_returnOne_whenImagesSizeAndVideosSizeRequested(){
        Exercise exercise = entityManager.find(Exercise.class, exerciseId);
        Image image = getImage();
        Video video = getVideo();
        exercise.addImage(image);
        exercise.addVideo(video);

        entityManager.persist(exercise);
        entityManager.flush();
        entityManager.clear();

        assertEquals(1,entityManager.find(Exercise.class, exerciseId).getImages().size());
        assertEquals(1,entityManager.find(Exercise.class, exerciseId).getVideos().size());
    }

    @Test
    public void should_returnZero_whenImagesSizeAndVideosSizeRequested(){
        Exercise exercise = entityManager.find(Exercise.class, exerciseId);
        Image image = getImage();
        Video video = getVideo();

        exercise.addImage(image);
        exercise.addVideo(video);

        entityManager.flush();
        Long imageId=image.getId();
        Long videoId=video.getId();
        entityManager.clear();

        exercise = entityManager.find(Exercise.class, exerciseId);
        image=entityManager.find(Image.class, imageId);
        video=entityManager.find(Video.class, videoId);

        exercise.removeImage(image);
        exercise.removeVideo(video);
        entityManager.flush();
        entityManager.clear();

        assertEquals(0,entityManager.find(Exercise.class, exerciseId).getImages().size());
        assertEquals(0,entityManager.find(Exercise.class, exerciseId).getVideos().size());
    }

    @Test
    public void should_returnNull_whenImageAndVideoRequested(){
        Exercise exercise = entityManager.find(Exercise.class, exerciseId);
        Image image = getImage();
        Video video = getVideo();

        exercise.addImage(image);
        exercise.addVideo(video);

        entityManager.flush();
        Long imageId=image.getId();
        Long videoId=video.getId();
        entityManager.clear();

        exercise = entityManager.find(Exercise.class, exerciseId);
        entityManager.remove(exercise);

        assertEquals(null,entityManager.find(Image.class, imageId));
        assertEquals(null,entityManager.find(Video.class, videoId));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void should_throwLazyException_whenImagesAndVideosRequested() {
        assertThrows(LazyInitializationException.class, () -> {
                    entityManager.find(Exercise.class, exerciseId).getImages().size();
                });
        assertThrows(LazyInitializationException.class, () -> {
            entityManager.find(Exercise.class, exerciseId).getVideos().size();
        });
    }

}
