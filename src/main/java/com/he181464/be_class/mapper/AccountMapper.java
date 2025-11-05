package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {

    @Mapping(source = "role.name", target = "roleName")
    AccountResponseDto toDTO(Account account);

    AccountDto toAccountDto(Account account);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "facebookAccountId", ignore = true)
    @Mapping(target = "googleAccountId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "roleId",ignore = true)
    Account toAccountEntity(AccountDto accountDto);

    Account toEntity(AccountDto accountDto);

    @Mapping(target = "password",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "roleId",ignore = true)
    void updateEntityFromDTO(AccountDto accountDto, @MappingTarget Account account);
}
