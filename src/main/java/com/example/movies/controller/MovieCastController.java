package com.example.movies.controller;


import ch.qos.logback.core.util.StringUtil;
import com.example.movies.dto.MovieCastDTO;
import com.example.movies.dto.MovieDTO;
import com.example.movies.entity.Movie;
import com.example.movies.service.MovieCastService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/movies")
public class MovieCastController {

    @Autowired
    private MovieCastService movieCastService;

    @PostMapping("/{id}/cast")
    public ResponseEntity<?> addMovieCast(@Valid @RequestBody MovieCastDTO movieCastDTO,@PathVariable Long id){

        if(id==null|| id!=movieCastDTO.getMovieId()){
            return ResponseEntity.ok("Please send a valid movie id.");
        }
        return movieCastService.addMovieCast(movieCastDTO);
    }

    @PostMapping("/{id}/update_cast")
    public ResponseEntity<?> updateMovieCastName( @Valid @RequestBody MovieCastDTO movieCastDTO,@PathVariable Long id){

        if(movieCastDTO.getId()==null){
            return ResponseEntity.ok("Movie cast Id cannot be null.");
        }
        if(id==null || id!=movieCastDTO.getMovieId()){
            return ResponseEntity.ok("Please send a valid movie id.");
        }
        return movieCastService.updateMovieCastName(movieCastDTO,id);
    }


    @GetMapping("/{id}/cast")
    public ResponseEntity<?> getMovieCast(@PathVariable Long id, HttpServletRequest request){

        System.out.println("testing http session"+ request.getRemoteHost()+request.getSession().getId());
        HttpSession s=request.getSession();

        System.out.println("testing http csrf"+ request.getAttribute("_csrf"));
        if(id==null ){
            return ResponseEntity.ok("Please send a valid movie id.");
        }
        return movieCastService.getMovieCast(id);
    }

    

}
