package com.henri.server;

import com.henri.CallbackAppServerInterface;
import com.henri.DSServer1;
import com.henri.dao.GameRepositoryDS1;
import com.henri.dao.SessionIdentifierRepositoryDS1;
import com.henri.dao.UserEntityRepositoryDS1;
import com.henri.model.GameEntityDS1;
import com.henri.model.SessionIdentifierEntityDS1;
import com.henri.model.UserEntityDS1;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

@Component("ServerImplDS1")
public class ServerImplDS1 extends UnicastRemoteObject implements InterfaceServerDS1 {


    private Set<String> userSet = new HashSet<>();
    private Map<String, CallbackAppServerInterface> clientList;


    //DataSource dataSource;


    UserEntityRepositoryDS1 userEntityRepositoryDS1;

    SessionIdentifierRepositoryDS1 sessionIdentifierRepositoryDS1;

    GameRepositoryDS1 gameRepositoryDS1;


    public ServerImplDS1(UserEntityRepositoryDS1 userEntityRepositoryDS1, SessionIdentifierRepositoryDS1 sessionIdentifierRepositoryDS1, GameRepositoryDS1 gameRepositoryDS1) throws RemoteException {
        clientList = new HashMap<>();
        this.userEntityRepositoryDS1 = userEntityRepositoryDS1;
        this.sessionIdentifierRepositoryDS1 = sessionIdentifierRepositoryDS1;
        this.gameRepositoryDS1 = gameRepositoryDS1;
        System.out.println("Repositories are set");

    }

    /**
     * @inheritDoc
     * */
    @Override
    public synchronized void registerForCallback(
            CallbackAppServerInterface callbackClientObject, String username)
            throws RemoteException {
        // store the callback object into the Map (only possible after the setup has been executed)
        if ((userSet.contains(username))) {
            clientList.put(username, callbackClientObject);
            System.out.println("Registered new client ");
            //doCallbacks();
        } // end if
    }

    /**
     * @inheritDoc
     * */
    @Override
    public String setupMessage(String username, String password) throws RemoteException {
        UserEntityDS1 user = userEntityRepositoryDS1.findUserEntityByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return String.valueOf(user.getUserId());
            } else {
                return "-1";
            }
        } else {
            return "-1";
        }


    }

    /**
     * @inheritDoc
     * */
    @Override
    public boolean checkUsername(String username) throws RemoteException {
        UserEntityDS1 userEntityDS1 = userEntityRepositoryDS1.findUserEntityByUsername(username);
        if (userEntityDS1 == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @inheritDoc
     * */
    @Override
    public void registerUser(String username, String password) throws RemoteException {
        UserEntityDS1 userEntityDS1 = new UserEntityDS1();
        userEntityDS1.setUsername(username);
        userEntityDS1.setPassword(password);
        userEntityRepositoryDS1.save(userEntityDS1);
    }

    /**
     * @inheritDoc
     * */
    @Override
    public String acquireSessionId(String username) throws RemoteException {
        //find user
        UserEntityDS1 userEntityDS1 = userEntityRepositoryDS1.findUserEntityByUsername(username);
        //initialize id generator
        IdGeneratorDS1 idGenerator = new IdGeneratorDS1();
        //generate id
        String sessionId = idGenerator.generateId(60);
        //create new session object
        SessionIdentifierEntityDS1 s = new SessionIdentifierEntityDS1();
        //set identifier to object
        s.setSessionIdentifier(sessionId);
        s.setCancellationTime((System.currentTimeMillis() + 86400000));
        sessionIdentifierRepositoryDS1.save(s);
        //set object to user
        userEntityDS1.setSessionIdentifierEntity(s);
        //save user
        userEntityRepositoryDS1.save(userEntityDS1);

        StringBuilder sb = new StringBuilder();
        sb.append(s.getSessionIdentifierId());
        sb.append(",");
        sb.append(sessionId);

        //return the sessionId
        return sb.toString();
    }

    /**
     * @inheritDoc
     * */
    @Override
    public void createGame(ArrayList<String> gamePositions, String username) throws RemoteException {
        // get number of players, game theme and positions
        String numberOfPlayers = gamePositions.get(0);
        String gameTheme = gamePositions.get(1);
        String gameName = gamePositions.get(2);
        ArrayList<String> positions = new ArrayList<>(gamePositions.subList(3, gamePositions.size()));

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < positions.size(); i++) {
            sb.append(positions.get(i));
            if (i != positions.size() - 1)
                sb.append(",");
        }

        UserEntityDS1 userEntityDS1 = userEntityRepositoryDS1.findUserEntityByUsername(username);
        GameEntityDS1 gameEntityDS1 = new GameEntityDS1();
        gameEntityDS1.setUserEntityDS1(userEntityDS1);
        gameEntityDS1.setUserIdOne(userEntityDS1.getUserId());
        gameEntityDS1.setUserIdTurn(userEntityDS1.getUserId());
        gameEntityDS1.setGameSize(positions.size());
        gameEntityDS1.setGamePositions(sb.toString());
        gameEntityDS1.setGameTheme(Integer.parseInt(gameTheme));
        gameEntityDS1.setGameMaxPlayers(Integer.parseInt(numberOfPlayers));
        gameEntityDS1.setGameName(gameName);
        gameEntityDS1.setGameUsers(1);
        gameEntityDS1.setVersion(gameEntityDS1.getVersion() + 1);
        ServerMainDS1.serverImplDS2.implementNewGame(createGameConfig(gameEntityDS1));
        gameRepositoryDS1.save(gameEntityDS1);
    }

    /**
     * @inheritDoc
     * */
    @Override
    public ArrayList<String> requestGames(String username) {

        UserEntityDS1 userEntityDS1 = userEntityRepositoryDS1.findUserEntityByUsername(username);
        ArrayList<GameEntityDS1> games = gameRepositoryDS1.findGameEntityByUserId(userEntityDS1.getUserId());

        return createReturnGameArray(games);


    }

    /**
     * @inheritDoc
     * */
    @Override
    public ArrayList<String> requestAllGames(String username) {
        UserEntityDS1 userEntityDS1 = userEntityRepositoryDS1.findUserEntityByUsername(username);
        ArrayList<GameEntityDS1> games = gameRepositoryDS1.findAllGames(userEntityDS1.getUserId());

        return createReturnGameArray(games);
    }


    public ArrayList<String> returnGameConfig(GameEntityDS1 gameEntityDS1) {
        ArrayList<String> gameConfig = new ArrayList<>();
        gameConfig.add(String.valueOf(gameEntityDS1.getGameId()));         //game ID
        gameConfig.add(gameEntityDS1.getGameName());                       //Game name
        gameConfig.add(String.valueOf(gameEntityDS1.getGameUsers()));      //number of users in the game
        gameConfig.add(String.valueOf(gameEntityDS1.getGameMaxPlayers())); //game max players allowed
        gameConfig.add(String.valueOf(gameEntityDS1.getGameTheme()));      //game theme
        gameConfig.add(gameEntityDS1.getGamePositions());

        return gameConfig;
    }

    /**
     * @inheritDoc
     * */
    @Override
    public ArrayList<String> requestGameWinner(int gameId) throws RemoteException {


        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(gameId);
        int scoreUserOne = (gameEntityDS1.getUserOneScore());
        int scoreUserTwo = (gameEntityDS1.getUserTwoScore());
        int scoreUserThree = (gameEntityDS1.getUserThreeScore());
        int scoreUserFour = (gameEntityDS1.getUserFourScore());
        ArrayList<Integer> scores = new ArrayList<>();
        scores.add(scoreUserOne);
        scores.add(scoreUserTwo);
        scores.add(scoreUserThree);
        scores.add(scoreUserFour);

        ArrayList<Integer> players = new ArrayList<>();
        players.add(gameEntityDS1.getUserIdOne());
        players.add(gameEntityDS1.getUserIdTwo());
        players.add(gameEntityDS1.getUserIdThree());
        players.add(gameEntityDS1.getUserIdFour());

        ArrayList<UserEntityDS1> winners = findMaxScore(scores, players);

        ArrayList<String> winnerNames = new ArrayList<>();
        for (int i = 0; i < winners.size(); i++) {
            winners.get(i).setScore(winners.get(i).getScore() + 1);
            userEntityRepositoryDS1.save(winners.get(i));
            winnerNames.add(winners.get(i).getUsername());
        }
        return winnerNames;
    }

    /**
     * @inheritDoc
     * */
    @Override
    public ArrayList<String> requestTopPlayers() throws RemoteException {
        ArrayList<String> winners = new ArrayList<>();
        ArrayList<UserEntityDS1> users = (ArrayList<UserEntityDS1>) userEntityRepositoryDS1.findAll();
        Collections.sort(users, new UserComparatorDS1());
        int size = (users.size() < 10 ? users.size() : 10);
        for (int i = 0; i < size; i++) {
            winners.add(users.get(i).getUsername());
            winners.add(String.valueOf(users.get(i).getScore()));
        }
        return winners;
    }

    /**
     * Function which for a given list of players and scores returns the max score and the player to which this score belongs to
     * @param  scores List with all scores
     * @param players List with all players
     * */
    public ArrayList<UserEntityDS1> findMaxScore(ArrayList<Integer> scores, ArrayList<Integer> players) {
        ArrayList<UserEntityDS1> winners = new ArrayList<>();

        int max = 0;
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) > max) {
                winners.clear();
                max = scores.get(i);
                winners.add(userEntityRepositoryDS1.findUserEntityByUserId(players.get(i)));
            } else if (scores.get(i) == max) {
                winners.add(userEntityRepositoryDS1.findUserEntityByUserId(players.get(i)));
            }
        }
        for (UserEntityDS1 userEntityDS1 : winners) {
            userEntityDS1.setScore(max);
        }
        return winners;
    }

    /**
     * @inheritDoc
     * */
    @Override
    public boolean checkSessionIdentifier(int sessionId, String sessionIdentifier) throws RemoteException {
        SessionIdentifierEntityDS1 sessionidentifierEntityDS1 = sessionIdentifierRepositoryDS1.findSessionIdentifierById(sessionId);
        long cancellationTime = sessionidentifierEntityDS1.getCancellationTime();
        long currentTime = System.currentTimeMillis();
        if (sessionidentifierEntityDS1.getSessionIdentifier().equals(sessionIdentifier)) {
            if (currentTime - cancellationTime > 3600000) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Function which returns an array with the basic configuration of multiple games
     * @param games List which contains all game objects of which the basic implementation is requested
     * */
    public ArrayList<String> createReturnGameArray(ArrayList<GameEntityDS1> games) {
        ArrayList<String> returnGames = new ArrayList<>();
        for (GameEntityDS1 g : games) {
            returnGames.add(String.valueOf(g.getGameId()));         //game ID
            returnGames.add(g.getGameName());                       //game name
            returnGames.add(String.valueOf(g.getGameUsers()));      //number of users in the game
            returnGames.add(String.valueOf(g.getGameMaxPlayers())); //game max players allowed
            returnGames.add(String.valueOf(g.getGameTheme()));      //game theme
            returnGames.add(String.valueOf(g.getGameSize()));
            //returnGames.add(g.getActiveUsers()); //is comma separated string
        }
        return returnGames;
    }

    /**
     * @inheritDoc
     * */
    @Override
    public int checkGameOnOtherServer(int gameId) throws RemoteException{
        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(gameId);
        if(gameEntityDS1.getActiveServer() != 0) return gameEntityDS1.getActiveServer();
        else return -1;
    }

    /**
     * @inheritDoc
     * */
    @Override
    public ArrayList<String> enterNewGame(int gameId, int appPort) throws RemoteException{

        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(gameId);
        //check if newer versions of the game are available
        int versionOnSecondServer = ServerMainDS1.serverImplDS2.checkGameVersion(gameId, gameEntityDS1.getVersion());
        // int versionOnThirdServer = ServerMainDS1.serverImplDS3.checkGameVersion(gameId, gameEntityDS1.getVersion());
        int newestVersion;
        if(versionOnSecondServer > gameEntityDS1.getVersion() /*&& versionOnSecondServer > versionOnThirdServer*/){
            ArrayList<String> gameConfig = ServerMainDS1.serverImplDS2.getGameConfig(gameId);
            RMIImpl.ImplementGame(gameConfig, gameEntityDS1, userEntityRepositoryDS1);
            gameEntityDS1.setVersion(versionOnSecondServer);
        }

        /*else if(versionOnThirdServer > gameEntityDS1.getVersion() && versionOnSecondServer > versionOnThirdServer)
            ServerMainDS1.serverImplDS3.getGameConfig(gameId);*/
        gameEntityDS1.setActiveServer(appPort);
        ServerMainDS1.serverImplDS2.updateGameServerPort(appPort,gameId);
        //ServerMainDS1.serverImplDS3.updateGameServerPort(appPort,gameId);
        gameRepositoryDS1.save(gameEntityDS1);

        return createGameConfig(gameEntityDS1);


    }

    /**
     * Function which creates an entire game configuration from a given game object
     * */
    public ArrayList<String> createGameConfig(GameEntityDS1 gameEntityDS1){
        //get game object and send all info back to the app server
        return RMIImpl.createGameConfig(gameEntityDS1);

    }

    /**
     * @inheritDoc
     * */
    @Override
    public void updateEntireGame(ArrayList<String> gameConfig) throws RemoteException{
        GameEntityDS1 gameEntityDS1 = gameRepositoryDS1.findGameEntityByGameId(Integer.parseInt(gameConfig.get(0)));

        RMIImpl.ImplementGame(gameConfig, gameEntityDS1, userEntityRepositoryDS1);
        gameEntityDS1.setVersion(gameEntityDS1.getVersion() + 1);

        ServerMainDS1.serverImplDS2.updateGameServerPort( gameEntityDS1.getActiveServer(),gameEntityDS1.getGameId());
        // ServerMainDS1.serverImplDS3.updateGameServerPort(gameEntityDS1.getGameId(), gameEntityDS1.getActiveServer());
        gameRepositoryDS1.save(gameEntityDS1);
    }



}
