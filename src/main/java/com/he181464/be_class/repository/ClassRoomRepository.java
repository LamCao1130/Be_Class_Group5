package com.he181464.be_class.repository;

import com.he181464.be_class.entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    boolean existsByCode(String code);

}
