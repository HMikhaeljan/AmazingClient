/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Player;
import amazingsharedproject.User;
import java.awt.Font;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Robin
 */
public class GameOver extends Application{
    private Player player;
    public GameOver(Player player) {
        this.player = player;
    }

    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        Scene scene = new Scene(group, 500, 500, Color.GREENYELLOW);
        Text t = new Text("GAME OVER!");
        Text t2= new Text("You got killed by: " + player.getKilledBy());
        t.setFill(Color.BLACK);
        t2.setFill(Color.BLACK);
        //t.setStroke(Color.BLACK);
        //t2.setStroke(Color.BLACK);
        t.setX(150);
        t2.setX(150);
        t.setY(150);
        t2.setY(200);
        t.setLineSpacing(1);
        group.getChildren().add(t);
        group.getChildren().add(t2);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GAME OVER");
        primaryStage.show();
        
    }
}
