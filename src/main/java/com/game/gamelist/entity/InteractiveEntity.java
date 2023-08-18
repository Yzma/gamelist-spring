package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Entity(name = "interactive_entities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
public abstract class InteractiveEntity {
    @Id
    @GeneratedValue
    private Long id;



    @OneToMany(mappedBy = "interactiveEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("interactiveEntity")
    private final List<LikeEntity> likes = new ArrayList<>();

    protected Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public List<LikeEntity> getLikes() {
        return likes;
    }
}