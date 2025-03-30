package com.example.movies.controller;

import com.example.movies.aspect.annotations.SetRequestAttributes;
import com.example.movies.dto.MovieDTO;
import com.example.movies.entity.Movie;

import com.example.movies.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private MovieService movieService;


    @SetRequestAttributes
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMovie(@Valid @RequestBody MovieDTO movieDto) {
        Map<String, Object> response = new HashMap<>();
        Movie savedMovie = movieService.createMovie(movieDto);
        response.put("message", "Movie created successfully");
        response.put("movie", savedMovie);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @SetRequestAttributes
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMoviebyId(@PathVariable Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpSession http = request.getSession();
        System.out.println("testing sessions" + http.getServletContext());
        System.out.println("tetsing authentication");
        return movieService.getMoviebyId(id);
    }

    @SetRequestAttributes
    @GetMapping("/paginated")
    public ResponseEntity<Page<Movie>> getAllMovies(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "1") int size) {
        System.out.println("testing service class" + movieService.getClass().getName() + "test" + movieService.toString());
        return movieService.getAllMovies(page, size);
    }

    @SetRequestAttributes
    @PostMapping("{id}")
    public ResponseEntity<?> updateMovie(@Valid @RequestBody MovieDTO movieDto, @PathVariable Long id1) {
        return movieService.updateMovie(movieDto, id1);
    }

    @SetRequestAttributes
    @PostMapping("/deletemovie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {


        return movieService.deleteMovie(id);
    }

    @SetRequestAttributes
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("the uploaded file is empty");
        }
        if (!file.getContentType().equals("text/csv") && !file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Please upload a valid csv");
        }
        return movieService.parseCSVFile(file);
    }

}