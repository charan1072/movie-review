package com.example.movies.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ADMIN-SERVICE")
public interface AdminServiceClient {

    @GetMapping(path = "/admin/{id}")
    Boolean isAdminValid(@PathVariable("id") Long adminId);

    @GetMapping(path = "/admin/verify")
    Long authenticateAdminAndFetchId(@RequestParam String username, @RequestParam String email, @RequestParam String password);

}
