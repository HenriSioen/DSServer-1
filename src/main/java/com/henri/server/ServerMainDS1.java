package com.henri.server;


import com.henri.RMIInterface;
import com.henri.dao.GameRepositoryDS1;
import com.henri.dao.SessionIdentifierRepositoryDS1;
import com.henri.dao.UserEntityRepositoryDS1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class which initializes the RMI connections between multiple database servers
 * */
public class ServerMainDS1 {

    public static RMIInterface serverImplDS2;
    public static RMIInterface serverImplDS3;

    /**
     * Function which starts the sever and creates the RMI registry
     * @param userEntityRepositoryDS1 The autowired Springboot repository for the user
     * @param sessionIdentifierRepositoryDS1 The autowired Springboot repository for the session identifier
     * @param gameRepositoryDS1 The autowired Springboot repository for the game
     * @param ownPort  The port on which the database server should initialize
     * */
    public void startServer(UserEntityRepositoryDS1 userEntityRepositoryDS1, SessionIdentifierRepositoryDS1 sessionIdentifierRepositoryDS1, GameRepositoryDS1 gameRepositoryDS1, int ownPort) {

        try {
            //create on own port
            Registry registry = LocateRegistry.createRegistry(ownPort);

            //create a new service

            registry.rebind("ServerImplService", new ServerImplDS1(userEntityRepositoryDS1, sessionIdentifierRepositoryDS1, gameRepositoryDS1));


        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            //Create Server RMI
            Registry registry = LocateRegistry.createRegistry(1800);

            //create service
            registry.rebind("ServerRMI", new RMIImpl(userEntityRepositoryDS1, sessionIdentifierRepositoryDS1, gameRepositoryDS1));
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    /**
     * Function which connects to other database server RMI registries
     * */
    public void connect() {
        try {

            // fire to localhost on port 1
            Registry registryServer1 = LocateRegistry.getRegistry("localhost", 1801);

            // search Dispatcher
            serverImplDS2 = (RMIInterface) registryServer1.lookup("ServerRMI");

            System.out.println("Second server connection SUCCESS");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // fire to localhost on port 2
            Registry registryDispatcherServer = LocateRegistry.getRegistry("localhost", 1802);

            // search Dispatcher
            serverImplDS3 = (RMIInterface) registryDispatcherServer.lookup("ServerRMI");


        } catch (Exception e) {
            System.out.println("Third server connection FAILED, maybe the server is not active?");
        }
        System.out.println("System is ready");
    }

}
