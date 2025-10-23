package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.AccountResponseDto;
import com.he181464.be_class.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {
    @Mapping(source = "role.name",target = "roleName")
    AccountResponseDto toDTO(Account account);

    Account toEntity(AccountResponseDto accountResponseDto);
}
