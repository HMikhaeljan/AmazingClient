/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingclient.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Kasper
 */
public class AMazeIng extends Application {

    static int spritesize = 16;    

    public Image imgCharacter;
    public Node nodCharacter;

    public Image imgEnemy;
    public Node nodEnemy;

    public Node sppp;

    private String key;

    public Group group;

    public ArrayList<Node> nodes;

    public Scene scene;
    //public EnemyController eController;

    //Moving checks
    public List<Node> solidBlocks;

    public Node playerPos;

    public Boolean collision;

    public Node tempNode;

    //enemie moving checks
    public Node enemyPos;
    public Boolean enemyCollision;
    public Node tempNodeEnemy;

    int leftCount = 0;
    int rightCount = 0;
    int upCount = 0;
    int downCount = 0;

    //Player Abilities

    
    //PlayerController
    @Override
    public void start(Stage primaryStage) {
        solidBlocks = new ArrayList<Node>();

        Image imgWall = Sprite.LoadSprite("Resources/WallSprite.jpg", 16, 16);
        Node nodWall = new ImageView(imgWall);
        Group groupWall = new Group(nodWall);

        //Block[][] mazegrid =;

        //Create list of all the block images
        ArrayList<Image> images = new ArrayList<Image>();
        nodes = new ArrayList<Node>();

        for (int y = 0; y < testmaze.getGridSize(); y++) {
            for (int x = 0; x < testmaze.getGridSize(); x++) {
                switch (mazegrid[y][x]) {
                    case Block.SOLID:
                        Image sol = Sprite.LoadSprite("Resources/WallSprite.jpg", 16, 16);
                        images.add(sol);
                        Node wpos = new ImageView(sol);
                        wpos.relocate(x * spritesize, y * spritesize);
                        nodes.add(wpos);
                        solidBlocks.add(wpos);
                        break;
                    case Block.OPEN:
                        Image ope = Sprite.LoadSprite("Resources/FloorSprite.jpg", 16, 16);
                        images.add(ope);
                        Node opos = new ImageView(ope);
                        opos.relocate(x * spritesize, y * spritesize);
                        nodes.add(opos);
                        break;
                    case Block.SPAWNPOINT:
                        Image spp = Sprite.LoadSprite("Resources/SpawnPoint.jpg", 16, 16);
                        images.add(spp);
                        Node sppp = new ImageView(spp);
                        sppp.relocate(x * spritesize, y * spritesize);
                        nodes.add(sppp);
                        spawnPoints.add(sppp);
                        break;
                    case Block.EDGE:
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
        scene = new Scene(group, testmaze.getGridSize() * spritesize, testmaze.getGridSize() * spritesize, Color.DARKSALMON);
        primaryStage.setTitle("Pathfinding");
        primaryStage.setScene(scene);
        primaryStage.show();        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
