package com.training.diary.models;

import lombok.*;

import javax.persistence.Entity;
import java.sql.Blob;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video extends ExerciseFile{

    public Video(Blob file) {
        super(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Video))
            return false;

        Video other = (Video) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
