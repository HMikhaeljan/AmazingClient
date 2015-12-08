/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Block;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Interfaces.IGameManager;
import amazingsharedproject.Sprite;
import amazingsharedproject.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Kasper
 */
public class AMazeIng extends Application {

    static int spritesize = 16;

    public Node sppp;

    public Group group;

    public ArrayList<Node> nodes;
    
    private IGameManager gamemanager;
    
    private IGame game;

    public Scene scene;
    

    //PlayerController
    private Block[][] mazegrid;
    
    private User user;
    
    //RMI:
    private static final int port = 1099;
    private static final String bindName = "Test";
    private Registry registry;
    //todo PAS DIT AAN
    private static final String ip = "169.254.174.77";
    
    public AMazeIng () {
    }
    

    @Override
    public void start(Stage primaryStage) throws NotBoundException, RemoteException {
        try {
            registry = LocateRegistry.getRegistry(ip, port);
        } catch (RemoteException e) {
            System.out.println("Kan registry niet vinden: " + e.getMessage());
        }
        
        try {
            gamemanager = (IGameManager)registry.lookup("GameManager");
        } catch (RemoteException d) {
            System.out.println("Kan registry niet vinden: " + d.getMessage());
        }
        System.out.println(gamemanager);
        System.out.println(gamemanager.newLobby(LobbySession.user.getUserID()));
        
        game = gamemanager.newLobby(LobbySession.user.getUserID());
        
        //TODO FIX THIS JEROEN!!!
        //mazegrid = game.getGrid();
        
        Image imgWall = Sprite.LoadSprite("Resources/WallSprite.jpg", 16, 16);
        Node nodWall = new ImageView(imgWall);
        Group groupWall = new Group(nodWall);        

        //Create list of all the block images
        ArrayList<Image> images = new ArrayList<Image>();
        nodes = new ArrayList<Node>();

        for (int y = 0; y < mazegrid.length; y++) {
            for (int x = 0; x < mazegrid.length; x++) {
                switch (mazegrid[y][x]) {
                    case SOLID:
                        Image sol = Sprite.LoadSprite("Resources/WallSprite.jpg", 16, 16);
                        images.add(sol);
                        Node wpos = new ImageView(sol);
                        wpos.relocate(x * spritesize, y * spritesize);
                        nodes.add(wpos);
                        break;
                    case OPEN:
                        Image ope = Sprite.LoadSprite("Resources/FloorSprite.jpg", 16, 16);
                        images.add(ope);
                        Node opos = new ImageView(ope);
                        opos.relocate(x * spritesize, y * spritesize);
                        nodes.add(opos);
                        break;
                    case SPAWNPOINT:
                        Image spp = Sprite.LoadSprite("Resources/SpawnPoint.jpg", 16, 16);
                        images.add(spp);
                        Node sppp = new ImageView(spp);
                        sppp.relocate(x * spritesize, y * spritesize);
                        nodes.add(sppp);
                        break;
                    case EDGE:
                        Image edg = Sprite.LoadSprite("Resources/MapEdge.jpg", 16, 16);
                        images.add(edg);
                        Node edgp = new ImageView(edg);
                        edgp.relocate(x * spritesize, y * spritesize);
                        nodes.add(edgp);
                        break;
                }
            }
        }

        group = new Group(nodes);
        scene = new Scene(group, mazegrid.length * spritesize, mazegrid.length * spritesize, Color.DARKSALMON);
        primaryStage.setTitle("Pathfinding");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                 switch (event.getCode()) {
                     case LEFT: break;
                     case RIGHT: break;
                     case UP: break;
                     case DOWN: break;
                     case DIGIT1: break;
                     case DIGIT2: break;
                     case DIGIT3: break;
                     case DIGIT4: break;            
                 }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
