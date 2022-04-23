package com.training.diary.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = "id")
@NoArgsConstructor
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private Clob commentary;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "information_id")
    @Setter(AccessLevel.NONE)
    private List<State> states;

    public void addState(@NotNull State state){
        if (states==null)
        {
            states=new ArrayList<>();
        }

        states.add(state);
    }

    public void removeState(@NotNull State state){
        if (states==null)
        {
            return;
        }

        states.remove(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Information))
            return false;

        Information other = (Information) o;

        return id != null &&
                id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
