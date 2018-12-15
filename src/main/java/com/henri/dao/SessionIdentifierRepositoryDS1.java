package com.henri.dao;

import com.henri.model.SessionIdentifierEntityDS1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "SessionIdentifierRepository")
public interface SessionIdentifierRepositoryDS1 extends CrudRepository<SessionIdentifierEntityDS1, Integer> {

    //Custom queries
    /*@Query("select u from UserEntityDS2 u")
    List<UserEntityDS2> findAllUsers();*/

    @Query("select s from SessionIdentifierEntityDS1  s where s.sessionIdentifierId = :sessionId")
    SessionIdentifierEntityDS1 findSessionIdentifierById(@Param("sessionId") int sessionId);


}
