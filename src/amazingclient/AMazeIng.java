/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Block;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Interfaces.IGameManager;
import amazingsharedproject.Player;
import amazingsharedproject.Sprite;
import amazingsharedproject.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Kasper
 */
public class AMazeIng extends Application {

    static int spritesize = 16;

    public Node sppp;
    
    public List<Text> msgs;

    public Group group;

    private ArrayList<Image> images;
    
    public ArrayList<Node> nodes;

    private IGameManager gamemanager;

    private IGame game;

    public Scene scene;

    private List<KeyCode> keys;

    private AnimationTimer controlsTimer;

    //PlayerController
    private Block[][] mazegrid;

    
    //Players:
    private List<Player> players;
    private List<Node> playerNodes;

    //RMI:
    private static final int port = 1098;
    private static final String bindName = "Test";
    private Registry registry;
    //todo PAS DIT AAN
    private static final String ip = "localhost";

    public AMazeIng() {
    }

    @Override
    public void start(Stage primaryStage) throws NotBoundException, RemoteException {

        game = LobbySession.game;

        keys = new ArrayList<KeyCode>();

        mazegrid = game.getGrid();

        msgs = new ArrayList<>();
        //TODO FIX THIS JEROEN!!!
        //mazegrid = game.getGrid();
        Image imgWall = Sprite.LoadSprite("Resources/WallSprite.jpg", 16, 16);
        Node nodWall = new ImageView(imgWall);
        Group groupWall = new Group(nodWall);
        //Create list of all the block images
        images = new ArrayList<Image>();
        nodes = new ArrayList<Node>();
        drawMaze();

        //game.getPlayer(LobbySession.user.getUserID());

        players = new ArrayList<>();
        playerNodes = new ArrayList<>();

        Image pimg = Sprite.LoadSprite("Resources/Mage-UP.png", 16, 16);
        GameState gs = game.getGameState();
        for(Player p : gs.getPlayers()) {
            Node playerNode = new ImageView(pimg);
            playerNodes.add(playerNode);
            playerNode.setLayoutX((p.getX()));
            playerNode.setLayoutY((p.getY()));
            playerNode.toFront();
            nodes.add(playerNode);
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
                    case LEFT:
                        if (!keys.contains(KeyCode.LEFT)) {
                            keys.add(KeyCode.LEFT);
                        }
                        break;
                    case RIGHT:
                        if (!keys.contains(KeyCode.RIGHT)) {
                            keys.add(KeyCode.RIGHT);
                        }
                        break;
                    case UP:
                        if (!keys.contains(KeyCode.UP)) {
                            keys.add(KeyCode.UP);
                        }
                        break;
                    case DOWN:
                        if (!keys.contains(KeyCode.DOWN)) {
                            keys.add(KeyCode.DOWN);
                        }
                        break;
                    case DIGIT1:
                        if (!keys.contains(KeyCode.DIGIT1)) {
                            keys.add(KeyCode.DIGIT1);
                        }
                        break;
                    case DIGIT2:
                        if (!keys.contains(KeyCode.DIGIT2)) {
                            keys.add(KeyCode.DIGIT2);
                        }
                        break;
                    case DIGIT3:
                        if (!keys.contains(KeyCode.DIGIT3)) {
                            keys.add(KeyCode.DIGIT3);
                        }
                        break;
                    case DIGIT4:
                        if (!keys.contains(KeyCode.DIGIT4)) {
                            keys.add(KeyCode.DIGIT4);
                        }
                        break;
                }
            }

        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case LEFT:
                        keys.remove(KeyCode.LEFT);
                        break;
                    case RIGHT:
                        keys.remove(KeyCode.RIGHT);
                        break;
                    case UP:
                        keys.remove(KeyCode.UP);
                        break;
                    case DOWN:
                        keys.remove(KeyCode.DOWN);
                        break;
                    case DIGIT1:
                        keys.remove(KeyCode.DIGIT1);
                        break;
                    case DIGIT2:
                        keys.remove(KeyCode.DIGIT2);
                        break;
                    case DIGIT3:
                        keys.remove(KeyCode.DIGIT3);
                        break;
                    case DIGIT4:
                        keys.remove(KeyCode.DIGIT4);
                        break;
                }
            }
        });

        controlsTimer = new playerAnim();
        controlsTimer.start();
    }
    
    private void drawMaze() {
        System.out.println("Mazegrid: " + mazegrid.length);
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
    }

    private ArrayList<Node> abilNodes = new ArrayList<Node>();

    private class playerAnim extends AnimationTimer {

        GameState gs;

        @Override
        public void handle(long now) {
            try {
                if (keys.size() > 0) {
                    game.handleInput(game.getPlayer(LobbySession.user.getUserID(), LobbySession.user.getName()).getID(), keys);
                }

                gs = game.getGameState();
                
                int idx= 0;
                for(Node n: playerNodes) {
                    if(gs.getPlayers().get(idx) != null) {
                        Player p = gs.getPlayers().get(idx);
                        n.relocate(p.getX(), p.getY());
                        n.setRotate(getRot(p.getDirection()));
                        //System.out.println("Node edited");
                        idx++;
                    }
                }

                for (Node n : abilNodes) {
                    group.getChildren().remove(n);
                }
                abilNodes.clear();

                for (Used u : gs.getAbilities()) {
                    Node n = new ImageView(Ability.getImage(0+(4*LobbySession.game.getPlayer(LobbySession.user.getUserID(), LobbySession.user.getName()).getPlayerRoleID())));
                    n.relocate(u.getX(), u.getY());
                    n.setRotate(getRot(u.getDirection()));
                    abilNodes.add(n);
                }

                for (Node n : abilNodes) {
                    group.getChildren().add(n);
                }
                
                
                int tpos= game.getGrid().length * spritesize-100;
                //TEXT:
                for(Text t: msgs) {
                    group.getChildren().remove(t);
                }
                msgs.clear();
                for(String s : gs.getMessages()) {
                    Text t = new Text(s);
                    t.setFill(Color.GREENYELLOW);
                    t.setX(20);
                    t.setY(tpos);
                    tpos-=15;
                    msgs.add(t);
                }
                
                for(Text t : msgs) {
                    group.getChildren().add(t);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(AMazeIng.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println("X: " + gs.getPlayers().get(0).getX() + "Y: " + gs.getPlayers().get(0).getY());

        }

        private double getRot(Direction d) {
            switch (d) {
                case UP:
                    return 0;
                case DOWN:
                    return 180;
                case RIGHT:
                    return 90;
                case LEFT:
                    return 270;
            }
            return 0;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
