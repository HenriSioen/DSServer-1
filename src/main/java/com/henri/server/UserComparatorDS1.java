package com.henri.server;



import com.henri.model.UserEntityDS1;

import java.util.Comparator;

/**
 * Comparator class which compares users based on score
 * */
public class UserComparatorDS1 implements Comparator<UserEntityDS1> {

    @Override
    public int compare(UserEntityDS1 firstPlayer, UserEntityDS1 secondPlayer) {
        return (firstPlayer.getScore() - secondPlayer.getScore());
    }
}
