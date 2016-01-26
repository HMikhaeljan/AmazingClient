/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import amazingsharedproject.Interfaces.*;
import amazingsharedproject.Stat;
import amazingsharedproject.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kasper
 */
public class LogInTest {

    ILogin logIn;
    User user;

    private Registry registry;
    private String ip = "localhost";
    private int port = 1098;


    public LogInTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            registry = LocateRegistry.getRegistry(ip, port);
        } catch (RemoteException e) {
            System.out.println("Kan registry niet vinden: " + e.getMessage());
        }

        try {
            logIn = (ILogin) registry.lookup("UserManager");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            logIn.registerUser("Peter", "reteP");
        } catch (RemoteException ex) {
            Logger.getLogger(LogInTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void logInCorrectlyTest() throws RemoteException {
        //Correct LogIn
        boolean tempBool = false;
        user = logIn.Login("Peter", "reteP");
        logIn.addToOnline(user);
        for (User usr : logIn.getOnlineUsers()) {
            System.out.println("Users: " + usr);
            if (usr.getName().equals("Peter")) {
                tempBool = true;
            }
        }
        assertTrue(tempBool);

        logIn.removeFromOnline(user);
    }

    @Test
    public void logInWrongPasswordTest() throws RemoteException {
        //Incorrect Password
        boolean tempBool = false;
        user = logIn.Login("Peter", "peter");
        try {
            logIn.addToOnline(user);
        } catch (NullPointerException ex) {
            for (User usr : logIn.getOnlineUsers()) {
                if (usr.getName().equals("Peter")) {
                    tempBool = true;
                }
            }
        }
        assertFalse(tempBool);
    }

    @Test
    public void logInUserDoesntExistTest() throws RemoteException {
        //User doesn't exist
        boolean tempBool = false;
        user = logIn.Login("Jonathan", "reteP");
        try {
            logIn.addToOnline(user);
        } catch (NullPointerException ex) {
            for (User usr : logIn.getOnlineUsers()) {
                if (usr.getName().equals("Jonathan")) {
                    tempBool = true;
                }
            }
        }
        assertFalse(tempBool);
    }

    @Test
    public void logInUserNameEmptyTest() throws RemoteException {
        //User name empty
        boolean tempBool = false;
        user = logIn.Login("", "reteP");
        try {
            logIn.addToOnline(user);
        } catch (NullPointerException ex) {
            for (User usr : logIn.getOnlineUsers()) {
                if (usr.getName().equals("")) {
                    tempBool = true;
                }
            }
        }
        assertFalse(tempBool);
    }

    @Test
    public void logInPasswordEmptyTest() throws RemoteException {
        //Password empty
        boolean tempBool = false;
        user = logIn.Login("Peter", "");
        try {
            logIn.addToOnline(user);
        } catch (NullPointerException ex) {
            for (User usr : logIn.getOnlineUsers()) {
                if (usr.getName().equals("Peter")) {
                    tempBool = true;
                }
            }
        }
        assertFalse(tempBool);
    }
}
