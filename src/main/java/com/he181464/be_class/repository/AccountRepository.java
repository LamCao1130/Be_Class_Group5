package com.he181464.be_class.repository;

import com.he181464.be_class.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    List<Account> findAllByStatusTrue();

    Optional<Account> findById(Long accountId);


    @Query("select a from Account a where a.role.id = :roleId")
    Page<Account> findByRole(@Param("roleId") Integer roleId, Pageable pageable);

    boolean existsAccountByEmail(String email);

    boolean existsAccountByPhoneNumber(String phoneNumber);
}
