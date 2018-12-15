package com.henri;


import com.henri.dao.GameRepositoryDS1;
import com.henri.dao.SessionIdentifierRepositoryDS1;
import com.henri.dao.UserEntityRepositoryDS1;
import com.henri.server.ServerMainDS1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Scanner;

/**
 * Class which initializes the Springboot application
 * */
@SpringBootApplication
@EnableJpaRepositories
@Configuration
@ComponentScan(basePackages = {"com.henri.dao", "com.henri.model", "com.henri.server", "com.henri"})
public class DSServer1 implements CommandLineRunner {

    @Autowired
    UserEntityRepositoryDS1 userEntityRepositoryDS1;

    @Autowired
    SessionIdentifierRepositoryDS1 sessionIdentifierRepositoryDS1;

    @Autowired
    GameRepositoryDS1 gameRepositoryDS1;

    public static void main(String[] args) {
        SpringApplication.run(DSServer1.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting server");
        ServerMainDS1 main = new ServerMainDS1();
        main.startServer(userEntityRepositoryDS1, sessionIdentifierRepositoryDS1, gameRepositoryDS1, 1114);
        System.out.println("Try connect to other servers? (y/n)");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().equals("y"))
            main.connect();
        //main.startServer(userEntityRepositoryDS1, sessionIdentifierRepositoryDS1, gameRepositoryDS1,1115,1116,1114);
        //main.startServer(userEntityRepositoryDS1, sessionIdentifierRepositoryDS1, gameRepositoryDS1,1116,1114,1115);
        else{
            System.out.println("No connections attempted");
        }

    }


}

