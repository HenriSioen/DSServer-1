package com.henri.dao;



import com.henri.model.GameEntityDS1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository(value = "GameRepository")
public interface GameRepositoryDS1 extends CrudRepository<GameEntityDS1, Integer> {

    @Query("select g from GameEntityDS1 g where g.userEntityDS1.userId = :userId or g.userIdOne = :userId or g.userIdTwo = :userId or g.userIdThree = :userId or g.userIdFour = :userId")
    ArrayList<GameEntityDS1> findGameEntityByUserId(@Param("userId") int userId);

    @Query("select g from GameEntityDS1 g where g.userEntityDS1.userId <> :userId and g.userIdOne <> :userId and g.userIdTwo <> :userId and g.userIdThree <> :userId and g.userIdFour <> :userId")
    ArrayList<GameEntityDS1> findAllGames(@Param("userId") int userId);

    @Query("select g from GameEntityDS1  g where g.gameId = :gameId")
    GameEntityDS1 findGameEntityByGameId(@Param("gameId") int gameId);
}
