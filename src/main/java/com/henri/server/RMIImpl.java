package com.henri.server;

import com.henri.RMIInterface;
import com.henri.dao.GameRepositoryDS1;
import com.henri.dao.SessionIdentifierRepositoryDS1;
import com.henri.dao.UserEntityRepositoryDS1;
import com.henri.model.GameEntityDS1;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

@Component("RMIImpl")
public class RMIImpl extends UnicastRemoteObject implements RMIInterface {

    UserEntityRepositoryDS1 userEntityRepositoryDS1;

    SessionIdentifierRepositoryDS1 sessionIdentifierRepositoryDS1;

    GameRepositoryDS1 gameRepositoryDS1;

    public RMIImpl(UserEntityRepositoryDS1 userEntityRepositoryDS1, SessionIdentifierRepositoryDS1 sessionIdentifierRepositoryDS1, GameRepositoryDS1 gameRepositoryDS1) throws RemoteException {
        this.userEntityRepositoryDS1 = userEntityRepositoryDS1;
        this.sessionIdentifierRepositoryDS1 = sessionIdentifierRepositoryDS1;
        this.gameRepositoryDS1 = gameRepositoryDS1;
    }

    /**
     * @inheritDoc
     * */
    @Override
    public void updateGameServerPort(int port, int gameID){
        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(gameID);
        gameEntityDS1.setActiveServer(port);
        gameRepositoryDS1.save(gameEntityDS1);
    }

    /**
     * @inheritDoc
     * */
    @Override
    public void implementNewGame(ArrayList<String> gameConfig){
        GameEntityDS1 gameEntityDS1 = new GameEntityDS1();
        gameEntityDS1.setGameId(Integer.parseInt(gameConfig.get(0)));
        ImplementGame(gameConfig, gameEntityDS1, userEntityRepositoryDS1);
        gameRepositoryDS1.save(gameEntityDS1);
    }

    /**
     * @inheritDoc
     * */
    @Override
    public int checkGameVersion(int gameId, int currentVersion) {
        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(gameId);
        if(currentVersion >= gameEntityDS1.getVersion()) return currentVersion;
        else return gameEntityDS1.getVersion();

    }

    /**
     * @inheritDoc
     * */
    @Override
    public ArrayList<String> getGameConfig(int gameId){
        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(gameId);
        ArrayList<String> gameConfig = createGameConfig(gameEntityDS1);
        gameConfig.add(String.valueOf(gameEntityDS1.getVersion()));

        return gameConfig;
    }

    /**
     * Function which creates an game configuration in terms of an arrayList with all properties of the game from a given game object
     * @param gameEntityDS1 The game object of which the configuration is requested
     * */
    static ArrayList<String> createGameConfig(GameEntityDS1 gameEntityDS1) {
        ArrayList<String> gameConfig = new ArrayList<>();
        gameConfig.add(String.valueOf(gameEntityDS1.getGameId()));
        gameConfig.add(gameEntityDS1.getGamePositions());
        gameConfig.add(String.valueOf(gameEntityDS1.getActiveUsers()));
        gameConfig.add(String.valueOf(gameEntityDS1.getGameSize()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserEntityDS1().getUserId()));
        gameConfig.add(String.valueOf(gameEntityDS1.getGameTheme()));
        gameConfig.add(String.valueOf(gameEntityDS1.getGameMaxPlayers()));
        gameConfig.add(gameEntityDS1.getGameName());
        gameConfig.add(String.valueOf(gameEntityDS1.getGameUsers()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserIdOne()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserIdTwo()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserIdThree()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserIdFour()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserIdTurn()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserOneScore()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserTwoScore()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserThreeScore()));
        gameConfig.add(String.valueOf(gameEntityDS1.getUserFourScore()));
        gameConfig.add(String.valueOf(gameEntityDS1.getActiveServer()));

        return  gameConfig;
    }

    /**
     * Function which from a given game configuration creates and saves the new implementation of the already existing game
     * @param  gameConfig The game configuration as an arrayList
     * @param gameEntityDS1 The game object of the game which is the old version from the database
     * @param userEntityRepositoryDS1 The user object of the user which created the game
     * */
    static void ImplementGame(ArrayList<String> gameConfig, GameEntityDS1 gameEntityDS1, UserEntityRepositoryDS1 userEntityRepositoryDS1) {
        gameEntityDS1.setGamePositions(gameConfig.get(1));
        gameEntityDS1.setActiveUsers(Integer.parseInt(gameConfig.get(2)));
        gameEntityDS1.setGameSize(Integer.parseInt(gameConfig.get(3)));
        gameEntityDS1.setUserEntityDS1(userEntityRepositoryDS1.findUserEntityByUserId(Integer.parseInt(gameConfig.get(4))));
        gameEntityDS1.setGameTheme(Integer.parseInt(gameConfig.get(5)));
        gameEntityDS1.setGameMaxPlayers(Integer.parseInt(gameConfig.get(6)));
        gameEntityDS1.setGameName(gameConfig.get(7));
        gameEntityDS1.setGameUsers(Integer.parseInt(gameConfig.get(8)));
        gameEntityDS1.setUserIdOne(Integer.parseInt(gameConfig.get(9)));
        gameEntityDS1.setUserIdTwo(Integer.parseInt(gameConfig.get(10)));
        gameEntityDS1.setUserIdThree(Integer.parseInt(gameConfig.get(11)));
        gameEntityDS1.setUserIdFour(Integer.parseInt(gameConfig.get(12)));
        gameEntityDS1.setUserIdTurn(Integer.parseInt(gameConfig.get(13)));
        gameEntityDS1.setUserOneScore(Integer.parseInt(gameConfig.get(14)));
        gameEntityDS1.setUserTwoScore(Integer.parseInt(gameConfig.get(15)));
        gameEntityDS1.setUserThreeScore(Integer.parseInt(gameConfig.get(16)));
        gameEntityDS1.setUserFourScore(Integer.parseInt(gameConfig.get(17)));
        gameEntityDS1.setActiveServer(Integer.parseInt(gameConfig.get(18)));
    }


}
