package com.matiasbarraza.ordermanagementapi.services;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UserEntityRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UserEntityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEntityService {

    Page<UserEntityResponse> search(String search, Pageable pageable);

    UserEntityResponse findById(Long id);

    UserEntityResponse create(UserEntityRequest request);

    UserEntityResponse update(Long id, UserEntityRequest request);

    void delete(Long id);

}
