package com.example.movies.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MovieDTO {


    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

   @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

   @NotBlank(message = "Genre is required")
    private String genre;

   @NotNull(message = "Duration must be provided")
   @Min(value =1,message = "Duration must be atleast 1 minute")
    private Integer duration;

   @NotBlank(message = "Language is required")
    private String language;

    private String posterUrl;

    private String description;

    @Min(value = 0,message = "score must be atleast 0.0")
    @Max(value = 10,message = "score must not exceed 10")
    private BigDecimal score;

    @NotNull(message = "Admin id should not be null")
    private Long adminId;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;
}
