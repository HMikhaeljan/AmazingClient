/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Interfaces.IGameManager;
import amazingsharedproject.Interfaces.ILogin;
import amazingsharedproject.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kasper
 */
public class CreateLobbyTest {

    ILogin logIn;
    User user;

    private Registry registry;
    private String ip = "localhost";
    private int port = 1098;

    public IGameManager gameManager;

    public CreateLobbyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws RemoteException {
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
        System.out.println("Registry: " + registry);
        System.out.println("logIn: " + logIn);

        try {
            gameManager = (IGameManager) registry.lookup("GameManager");
        } catch (RemoteException d) {
            System.out.println("Kan registry niet vinden: " + d.getMessage());
        } catch (NotBoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        user = logIn.Login("Peter", "reteP");
        LobbySession.user = user;
        logIn.addToOnline(LobbySession.user);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void createGameCorrectlyTest() throws RemoteException {
        for (int i = 0; i < 1; i++) {
            LobbySession.game = gameManager.newLobby(LobbySession.user.getUserID());
            LobbySession.game.getPlayer(LobbySession.user.getUserID(), LobbySession.user.getName());
            LobbySession.game.setGameName("Game"+i+" gemaakt door " + user.getName());
        }
        assertNotNull(LobbySession.game);
        logIn.removeFromOnline(user);
    }
}
