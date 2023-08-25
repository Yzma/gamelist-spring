package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(length = 2000)
    @JsonProperty("summary")
    private String description;

    @Column(name = "`imageURL`")
    @JsonProperty("cover")
    private String imageURL;

    @Column(name = "`releaseDate`")
    @JsonProperty("first_release_date")
    private LocalDateTime releaseDate;

    @Column(name = "avg_score")
    private double avgScore;

    @Column(name = "total_rating")
    @JsonProperty("total_rating_count")
    private int totalRating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "`bannerURL`")
    @JsonProperty("screenshots")
    private String bannerURL;
//
//    @ManyToMany(mappedBy = "games", cascade = CascadeType.ALL)
//    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_genres",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(mappedBy = "games")
    private Set<Platform> platforms = new HashSet<>();

    @ManyToMany(mappedBy = "games")
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Column(name = "user_games")
    private Set<UserGame> userGames;

    public void addGenre(Genre genre) {
        genres.add(genre);
        genre.getGames().add(this);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getGames().remove(this);
    }

    public void addPlatform(Platform platform) {
        platforms.add(platform);
        platform.getGames().add(this);
    }
    public void removePlatform(Platform platform) {
        platforms.remove(platform);
        platform.getGames().remove(this);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getGames().add(this);
    }
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getGames().remove(this);
    }

}
