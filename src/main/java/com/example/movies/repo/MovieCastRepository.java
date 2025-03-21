package com.example.movies.repo;

import com.example.movies.entity.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MovieCastRepository extends JpaRepository<MovieCast,Long> {

List<MovieCast> findByMovieId(Long movieId);
}
