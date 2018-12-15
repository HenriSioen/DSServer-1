package com.henri.model;


import javax.persistence.*;

@Entity
@Table(name = "user", schema = "ds")
public class UserEntityDS1 {
    private int userId, score;
    private String username;
    private String password;
    private SessionIdentifierEntityDS1 sessionidentifierEntityDS1;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "score")
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @OneToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_identifier_session_identifier_Id")
    public SessionIdentifierEntityDS1 getSessionIdentifierEntity() {
        return sessionidentifierEntityDS1;
    }

    public void setSessionIdentifierEntity(SessionIdentifierEntityDS1 s) {
        sessionidentifierEntityDS1 = s;
    }


}
