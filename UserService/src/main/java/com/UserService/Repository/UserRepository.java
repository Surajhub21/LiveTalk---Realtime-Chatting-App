package com.UserService.Repository;

import com.UserService.Entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel , String> {

    UserModel getUserByUsername(String username);
    boolean existsByUsername(String username);

}
