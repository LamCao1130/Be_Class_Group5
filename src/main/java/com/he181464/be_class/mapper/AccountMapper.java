package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.AccountRequestDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {

    @Mapping(source = "role.name", target = "roleName")
    AccountResponseDto toDTO(Account account);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "facebookAccountId", ignore = true)
    @Mapping(target = "googleAccountId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "roleId",ignore = true)

    Account toEntity(AccountRequestDto accountRequestDto);

    @Mapping(target = "password",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "roleId",ignore = true)
    void updateEntityFromDTO(AccountRequestDto accountRequestDto, @MappingTarget Account account);
}
