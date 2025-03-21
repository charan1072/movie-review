package com.example.movies.service;

import com.example.movies.dto.MovieDTO;
import com.example.movies.entity.Movie;

import com.example.movies.exceptions.CustomServiceException;
import com.example.movies.repo.MovieRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    public Movie createMovie(MovieDTO movieDTO) {
        try {
            Movie movie = new Movie();
            movie.setTitle(movieDTO.getTitle());
            movie.setReleaseDate(movieDTO.getReleaseDate());
            movie.setGenre(movieDTO.getGenre());
            movie.setDuration(movieDTO.getDuration());
            movie.setLanguage(movieDTO.getLanguage());
            movie.setPosterUrl(movieDTO.getPosterUrl());
            movie.setDescription(movieDTO.getDescription());
            movie.setScore(movieDTO.getScore());
            movie.setAdminId(movieDTO.getAdminId());
            movie.setIsDeleted(false);
            return  movieRepository.save(movie);
        }catch (Exception e){
            throw new CustomServiceException("Movie could not be created due to an error");
        }
    }
    public ResponseEntity<Movie> getMoviebyId(Long id){
            Optional<Movie> movie=movieRepository.findById(id);
            if(movie.isPresent()){
                return new ResponseEntity(movie.get(), HttpStatus.OK);
            }
            return new ResponseEntity("Movie with the id"+id+"not found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Page<Movie>> getAllMovies(int page,int size){

        Pageable pageable= PageRequest.of(page,size);
        Page<Movie> movies=movieRepository.findAll(pageable);
        if(movies.hasContent()){
            return new ResponseEntity(movies, HttpStatus.OK);
        }
        return new ResponseEntity("movies not found", HttpStatus.OK);
    }

    public ResponseEntity<?> updateMovie(MovieDTO movieDTO,Long id) {
           Optional<Movie> isExists=movieRepository.findById(id);
            if(isExists.isPresent()) {
                Movie movie = isExists.get();
                movie.setTitle(movieDTO.getTitle());
                movie.setReleaseDate(movieDTO.getReleaseDate());
                movie.setGenre(movieDTO.getGenre());
                movie.setDuration(movieDTO.getDuration());
                movie.setLanguage(movieDTO.getLanguage());
                movie.setPosterUrl(movieDTO.getPosterUrl());
                movie.setDescription(movieDTO.getDescription());
                movie.setScore(movieDTO.getScore());
                movie.setAdminId(movieDTO.getAdminId());
                movie.setIsDeleted(movieDTO.getIsDeleted());
                 movieRepository.save(movie);
                 return   ResponseEntity.ok(movie);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie not found");
    }

    public ResponseEntity<String> deleteMovie(Long id){
        Optional<Movie> movie=movieRepository.findById(id);
        if(movie.isPresent()){
           movieRepository.deleteById(id);
           return ResponseEntity.status(HttpStatus.OK).body("Movie deleted successfully");
        }
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("movie not found");
    }

    public ResponseEntity<?> parseCSVFile(MultipartFile file) throws IOException {
        List<MovieDTO> movies = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()  // Automatically detects headers from the first row
                    .setSkipHeaderRecord(true)  // Skips the first record since it's a header
                    .build();
            CSVParser csvParser = new CSVParser(reader, csvFormat);
            for (CSVRecord record : csvParser) {
                System.out.println("Processing Record: " + record.toString());
                try {
                    MovieDTO movieDTO = validateMovie(record);
                    movies.add(movieDTO);
                } catch (IllegalArgumentException e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                }
            }
        }catch (Exception e) {
        throw new RuntimeException("failed to parse csv file:"+e.getMessage());
        }
        if (movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("pLease upload a valid csv file");
        }
            List<Movie> movieEntities = new ArrayList<>();
            for (MovieDTO dto : movies) {
                System.out.println("Saving Movie: " + dto.getTitle() + ", Admin ID: " + dto.getAdminId());
                Movie movie = new Movie();
                movie.setTitle(dto.getTitle());
                movie.setReleaseDate(dto.getReleaseDate());
                movie.setGenre(dto.getGenre());
                movie.setDuration(dto.getDuration());
                movie.setLanguage(dto.getLanguage());
                movie.setPosterUrl(dto.getPosterUrl());
                movie.setDescription(dto.getDescription());
                movie.setScore(dto.getScore());
                movie.setAdminId(dto.getAdminId());
                movie.setIsDeleted(dto.getIsDeleted());
                movie.setDeletedAt(dto.getDeletedAt());
                movieEntities.add(movie);
            }
            movieRepository.saveAll(movieEntities);
        Map<String, Object> response=new HashMap<>();
        response.put("message", "Upload completed");
        response.put("uploaded_movies", movieEntities);
        response.put("errors", errors);

        return ResponseEntity.ok(response);
    }


    public MovieDTO validateMovie(CSVRecord record) {
        MovieDTO movie = new MovieDTO();

        String title = record.get("Title");
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        movie.setTitle(title);

        String releaseDateStr = record.get("ReleaseDate");
        if (releaseDateStr == null || releaseDateStr.isBlank()) {
            throw new IllegalArgumentException("Release Date is required.");
        }
        try {
            movie.setReleaseDate(LocalDate.parse(releaseDateStr));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Release Date format, expected YYYY-MM-DD");
        }

        String genre = record.get("Genre");
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre is required");
        }
        movie.setGenre(genre);

        String durationStr = record.get("Duration");
        try {
            int duration = Integer.parseInt(durationStr);
            if (duration < 1) {
                throw new IllegalArgumentException("Duration must be at least 1 minute");
            }
            movie.setDuration(duration);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Duration must be a valid integer");
        }

        String language = record.get("Language");
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("Language is required");
        }
        movie.setLanguage(language);

        String scoreStr = record.get("Score");
        if (scoreStr != null && !scoreStr.isBlank()) {
            try {
                BigDecimal score = new BigDecimal(scoreStr);
                if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.TEN) > 0) {
                    throw new IllegalArgumentException("Score must be between 0.0 and 10.0");
                }
                movie.setScore(score);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Score must be a valid number");
            }
        }

        String adminIdStr = record.get("AdminId");
        if (adminIdStr == null || adminIdStr.isBlank()) {
            throw new IllegalArgumentException("Admin ID is required");
        }
        movie.setAdminId(Long.parseLong(adminIdStr));

        String posterUrl = record.get("PosterUrl");
        movie.setPosterUrl(posterUrl);

        String description = record.get("Description");
        movie.setDescription(description);

        String isDeletedStr = record.get("IsDeleted");
        movie.setIsDeleted(isDeletedStr != null && isDeletedStr.equalsIgnoreCase("true"));

        String deletedAtStr = record.get("DeletedAt");
        if (deletedAtStr != null && !deletedAtStr.isBlank()) {
            movie.setDeletedAt(LocalDateTime.parse(deletedAtStr));
        } else {
            movie.setDeletedAt(null);
        }

        return movie;
    }
    }
