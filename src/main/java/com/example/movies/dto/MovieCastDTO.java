package com.example.movies.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovieCastDTO {


    private Long id;


    @NotNull(message = "Movie id cannot be null")
    private Long movieId;


    @NotNull(message = "Person id cannot be null")
    private Long personId;

    @NotBlank(message = "character name cannot be blank")
    private String characterName;


    @NotBlank(message = "Role type cannot be blank")
    private String roleType;

    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;
}
