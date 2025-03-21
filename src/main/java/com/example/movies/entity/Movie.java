package com.example.movies.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Movie{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false, length = 255)
    private String genre;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false, length = 255)
    private String language;

    @Column(name = "poster_url", length = 2048)
    private String posterUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 2, scale = 1)
    private BigDecimal score;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


}
