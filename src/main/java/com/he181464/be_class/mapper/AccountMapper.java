package com.he181464.be_class.mapper;

import com.he181464.be_class.dto.AccountDto;
import com.he181464.be_class.entity.Account;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface AccountMapper {

    AccountDto toAccountDto(Account account);

    Account toAccountEntity(AccountDto accountDto);

}
