package com.henri.dao;

import com.henri.model.UserEntityDS1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "UserEntityRepository")
public interface UserEntityRepositoryDS1 extends CrudRepository<UserEntityDS1, Integer> {

    //Custom queries
    /*@Query("select u from UserEntityDS2 u")
    List<UserEntityDS2> findAllUsers();*/

    @Query("select u from UserEntityDS1  u where u.username = :username")
    UserEntityDS1 findUserEntityByUsername(@Param("username") String username);

    @Query("select u from UserEntityDS1  u where  u.userId = :userId")
    UserEntityDS1 findUserEntityByUserId(@Param("userId") int userId);

}
