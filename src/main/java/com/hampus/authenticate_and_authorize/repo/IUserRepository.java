package com.hampus.authenticate_and_authorize.repo;

import com.hampus.authenticate_and_authorize.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<MyUser, Long>
{
    MyUser findByEmail(String email);
}
