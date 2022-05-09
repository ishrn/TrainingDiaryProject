package com.training.diary.models;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"id","name","category","description"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;
    @Lob
    private Clob description;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "exercise_id")
    @Setter(AccessLevel.NONE)
    @Where(clause = "DTYPE='Image'")
    private List<ExerciseFile> images;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "exercise_id")
    @Setter(AccessLevel.NONE)
    @Where(clause = "DTYPE='Video'")
    private List<ExerciseFile> videos;


    @OneToMany(mappedBy = "exercise",cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private List<ExerciseWorkoutInformation> exerciseWorkoutInformations;

    @Builder
    public Exercise(String name, Category category, Clob description) {
        exerciseWorkoutInformations=new ArrayList<>();
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public void addVideo(@NotNull Video video){
        if (videos==null){
            videos=new ArrayList<>();
        }
        videos.add(video);
    }

    public void removeVideo(@NotNull Video video){
        if (videos==null){
            return;
        }

        videos.remove(video);
    }

    public void addImage(@NotNull Image image){
        if (images==null){
            images=new ArrayList<>();
        }

        images.add(image);
    }

    public void removeImage(@NotNull Image image){
        if (images==null){
            return;
        }

        images.remove(image);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Exercise))
            return false;

        Exercise other = (Exercise) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
