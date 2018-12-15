package com.henri.model;



import javax.persistence.*;

@Entity
@Table(name = "game", schema = "ds")
public class GameEntityDS1 {
    private int gameId;
    private String gamePositions;
    private int activeUsers;
    private int gameSize;
    private UserEntityDS1 userEntityDS1;
    private int gameTheme;
    private int gameMaxPlayers;
    private String gameName;
    private int gameUsers;
    private int userIdOne, userIdTwo, userIdThree, userIdFour;
    private int userIdTurn;
    private int userOneScore, userTwoScore, userThreeScore, userFourScore;
    private int activeServer;
    private int version;

    @Id
    @Column(name = "game_id")
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Basic
    @Column(name = "game_positions")
    public String getGamePositions() {
        return gamePositions;
    }

    public void setGamePositions(String gamePositions) {
        this.gamePositions = gamePositions;
    }

    @Basic
    @Column(name = "game_size")
    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int i) {
        this.gameSize = i;
    }

    @Basic
    @Column(name = "game_name")
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Basic
    @Column(name = "active_users")
    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    @Basic
    @Column(name = "game_theme")
    public int getGameTheme() {
        return gameTheme;
    }

    public void setGameTheme(int i) {
        gameTheme = i;
    }

    @Basic
    @Column(name = "game_max_players")
    public int getGameMaxPlayers() {
        return gameMaxPlayers;
    }

    public void setGameMaxPlayers(int i) {
        gameMaxPlayers = i;
    }

    @Basic
    @Column(name = "game_users")
    public int getGameUsers() {
        return gameUsers;
    }

    public void setGameUsers(int i) {
        this.gameUsers = i;
    }

    @Basic
    @Column(name = "user_id_one")
    public int getUserIdOne() {
        return userIdOne;
    }

    public void setUserIdOne(int i) {
        this.userIdOne = i;
    }

    @Basic
    @Column(name = "user_id_two")
    public int getUserIdTwo() {
        return userIdTwo;
    }

    public void setUserIdTwo(int i) {
        this.userIdTwo = i;
    }

    @Basic
    @Column(name = "user_id_three")
    public int getUserIdThree() {
        return userIdThree;
    }

    public void setUserIdThree(int i) {
        this.userIdThree = i;
    }

    @Basic
    @Column(name = "user_id_four")
    public int getUserIdFour() {
        return userIdFour;
    }

    public void setUserIdFour(int i) {
        this.userIdFour = i;
    }

    @Basic
    @Column(name = "user_id_turn")
    public int getUserIdTurn() {
        return userIdTurn;
    }

    public void setUserIdTurn(int i) {
        this.userIdTurn = i;
    }

    @Basic
    @Column(name = "user_one_score")
    public int getUserOneScore() {
        return userOneScore;
    }

    public void setUserOneScore(int i) {
        this.userOneScore = i;
    }

    @Basic
    @Column(name = "user_two_score")
    public int getUserTwoScore() {
        return userTwoScore;
    }

    public void setUserTwoScore(int i) {
        this.userTwoScore = i;
    }

    @Basic
    @Column(name = "user_three_score")
    public int getUserThreeScore() {
        return userThreeScore;
    }

    public void setUserThreeScore(int i) {
        this.userThreeScore = i;
    }

    @Basic
    @Column(name = "user_four_score")
    public int getUserFourScore() {
        return userFourScore;
    }

    public void setUserFourScore(int i) {
        this.userFourScore = i;
    }

    @Basic
    @Column(name = "active_server")
    public int getActiveServer(){ return this.activeServer;}

    public void setActiveServer(int activeServer) {
        this.activeServer = activeServer;
    }

    @Basic
    @Column(name = "version")
    public int getVersion(){ return this.version;}

    public void setVersion(int version){ this.version = version;}

    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_user_id")
    public UserEntityDS1 getUserEntityDS1() {
        return userEntityDS1;
    }

    public void setUserEntityDS1(UserEntityDS1 s) {
        userEntityDS1 = s;
    }


}
