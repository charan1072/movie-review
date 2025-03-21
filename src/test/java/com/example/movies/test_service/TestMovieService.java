package com.example.movies.test_service;

import com.example.movies.dto.MovieDTO;
import com.example.movies.entity.Movie;
import com.example.movies.repo.MovieRepository;
import com.example.movies.service.MovieService;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class TestMovieService {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private MovieDTO movieDTO;
    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        movieDTO = new MovieDTO();
        movieDTO.setTitle("Inception");
        movieDTO.setReleaseDate(LocalDate.of(2010, 7, 16));
        movieDTO.setGenre("Sci-Fi");
        movieDTO.setDuration(148);
        movieDTO.setLanguage("English");
        movieDTO.setScore(new BigDecimal("8.8"));
        movieDTO.setAdminId(1L);
        movieDTO.setIsDeleted(false);

        movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setGenre(movieDTO.getGenre());
        movie.setDuration(movieDTO.getDuration());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setScore(movieDTO.getScore());
        movie.setAdminId(movieDTO.getAdminId());
        movie.setIsDeleted(false);
    }


    @Test
    void testCreateMovie_Success() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie createdMovie = movieService.createMovie(movieDTO);

        assertNotNull(createdMovie);
        assertEquals("Inception", createdMovie.getTitle());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testGetMovieById_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        ResponseEntity<Movie> response = movieService.getMoviebyId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Inception", response.getBody().getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Movie> response = movieService.getMoviebyId(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateMovie_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        ResponseEntity<?> response = movieService.updateMovie(movieDTO, 1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }


    @Test
    void testUpdateMovie_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = movieService.updateMovie(movieDTO, 1L);

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void testDeleteMovie_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        ResponseEntity<String> response = movieService.deleteMovie(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(movieRepository, times(1)).deleteById(1L);
    }


    @Test
    void testDeleteMovie_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = movieService.deleteMovie(1L);

        assertEquals(404, response.getStatusCodeValue());
    }




    @Test
    void testParseCSVFile_Invalid_NoHeader() throws IOException {
        String csvData = "Inception,2010-07-16,Sci-Fi,148,English,8.8,1";
        MockMultipartFile file = new MockMultipartFile("file", "movies.csv", "text/csv", csvData.getBytes());

        ResponseEntity<?> response = movieService.parseCSVFile(file);

        assertEquals(400, response.getStatusCodeValue());
        verify(movieRepository, never()).saveAll(anyList());
    }


    @Test
    void testParseCSVFile_Valid() throws Exception {

        String csvContent = "Title,ReleaseDate,Genre,Duration,Language,Score,AdminId,PosterUrl,Description,IsDeleted,DeletedAt\n" +
                "Inception,2010-07-16,Sci-Fi,148,English,8.8,1,http://example.com/inception.jpg,Great movie,false,\n";


        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "movies.csv",
                "text/csv",
                csvContent.getBytes()
        );


        ResponseEntity<?> response = movieService.parseCSVFile(mockFile);

    
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Upload completed", responseBody.get("message"));
        assertTrue(responseBody.containsKey("uploaded_movies"));
        assertTrue(responseBody.containsKey("errors"));

    
        verify(movieRepository, times(1)).saveAll(anyList());
    }
    @Test
    void testParseCSVFile_InvalidFormat() throws IOException {
        String csvData = "Title,ReleaseDate,Genre,Duration,Language,Score,AdminId\n" +
                "Inception,July-16-2010,Sci-Fi,148,English,8.8,1";
        MockMultipartFile file = new MockMultipartFile("file", "movies.csv", "text/csv", csvData.getBytes());

        ResponseEntity<?> response = movieService.parseCSVFile(file);

        assertEquals(400, response.getStatusCodeValue());
        verify(movieRepository, never()).saveAll(anyList());
    }

 
    @Test
    void testValidateMovie_Valid() {
        CSVRecord record = mock(CSVRecord.class);
        when(record.get("Title")).thenReturn("Inception");
        when(record.get("ReleaseDate")).thenReturn("2010-07-16");
        when(record.get("Genre")).thenReturn("Sci-Fi");
        when(record.get("Duration")).thenReturn("148");
        when(record.get("Language")).thenReturn("English");
        when(record.get("Score")).thenReturn("8.8");
        when(record.get("AdminId")).thenReturn("1");

        MovieDTO movieDTO = movieService.validateMovie(record);

        assertNotNull(movieDTO);
        assertEquals("Inception", movieDTO.getTitle());
    }


    @Test
    void testValidateMovie_MissingTitle() {
        CSVRecord record = mock(CSVRecord.class);
        when(record.get("Title")).thenReturn("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            movieService.validateMovie(record);
        });

        assertEquals("Title is required", exception.getMessage());
    }
}
