package com.matiasbarraza.ordermanagementapi.services;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.ClientRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    Page<ClientResponse> search(String search, Pageable pageable);

    ClientResponse findById(Long id);

    ClientResponse create(ClientRequest request);

    ClientResponse update(Long id, ClientRequest request);

    void delete(Long id);

}
