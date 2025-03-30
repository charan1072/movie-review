package com.example.movies.repo;

import com.example.movies.entity.Movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findAllBy(Pageable pageable);
}
