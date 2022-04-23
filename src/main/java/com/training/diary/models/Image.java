package com.training.diary.models;

import lombok.*;

import javax.persistence.Entity;
import java.sql.Blob;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends ExerciseFile{

    public Image(Blob file) {
        super(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Image))
            return false;

        Image other = (Image) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
