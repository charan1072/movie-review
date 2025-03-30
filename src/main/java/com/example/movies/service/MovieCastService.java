package com.example.movies.service;

import com.example.movies.dto.MovieCastDTO;
import com.example.movies.entity.Movie;
import com.example.movies.entity.MovieCast;
import com.example.movies.repo.MovieCastRepository;
import com.example.movies.repo.MovieRepository;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovieCastService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieCastRepository movieCastRepository;

    public ResponseEntity<Map<String, Object>> addMovieCast(MovieCastDTO movieCastDTO) {

        Optional<Movie> isMovieExists = movieRepository.findById(movieCastDTO.getMovieId());
        Map<String, Object> response = new HashMap<>();
        MovieCast movieCast = new MovieCast();

        if (isMovieExists.isPresent()) {
            movieCast.setMovieId(movieCastDTO.getMovieId());
            movieCast.setCharacterName(movieCastDTO.getCharacterName());
            movieCast.setPersonId(movieCastDTO.getPersonId());
            movieCast.setRoleType(movieCastDTO.getRoleType());

            movieCastRepository.save(movieCast);
        } else {
            response.put("message", "Movie not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("message", "Movie sucessfully added");
        response.put("movie cast", movieCast);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Map<String, Object>> updateMovieCastName(MovieCastDTO movieCastDTO, Long id) {


        Optional<MovieCast> isMovieCastExists = movieCastRepository.findById(movieCastDTO.getId());
        Map<String, Object> response = new HashMap<>();


        if (isMovieCastExists.isEmpty()) {
            response.put("message", "Movie not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        MovieCast movieCast = isMovieCastExists.get();
        movieCast.setCharacterName(movieCastDTO.getCharacterName());
        movieCast.setRoleType(movieCastDTO.getRoleType());
        movieCastRepository.save(movieCast);


        response.put("message", "Character Name updated  successfully");
        response.put("updated cast", movieCast);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<Map<String, Object>> getMovieCast(Long movieId) {


        List<MovieCast> isMovieCastExists = movieCastRepository.findByMovieId(movieId);
        Map<String, Object> response = new HashMap<>();
        if (isMovieCastExists.isEmpty()) {
            response.put("message", "Movie not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("message", "movie cast retrieved successfully");
        response.put("movie cast", isMovieCastExists);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
