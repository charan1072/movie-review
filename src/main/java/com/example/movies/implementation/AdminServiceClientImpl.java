package com.example.movies.implementation;

import com.example.movies.client.AdminServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Qualifier("adminServiceClientImpl")
public class AdminServiceClientImpl implements AdminServiceClient {


    @Override
    public Boolean isAdminValid(Long adminId) {
        return adminId != null;
    }

    @Override
    public Long authenticateAdminAndFetchId(String username, String email, String password) {
        if (username != null || email != null)
            return 1L;
        return 0L;
    }
}
