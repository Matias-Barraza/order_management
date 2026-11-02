package com.matiasbarraza.ordermanagementapi.mappers;

import com.matiasbarraza.ordermanagementapi.domain.dtos.request.UserEntityRequest;
import com.matiasbarraza.ordermanagementapi.domain.dtos.response.UserEntityResponse;
import com.matiasbarraza.ordermanagementapi.domain.entities.Role;
import com.matiasbarraza.ordermanagementapi.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    @Mapping(target = "roles", expression = "java(roleNames(userEntity))")
    UserEntityResponse toResponse(UserEntity userEntity);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(UserEntityRequest request);

    default Set<String> roleNames(UserEntity userEntity) {
        return userEntity.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserEntityRequest request, @MappingTarget UserEntity user);

}
