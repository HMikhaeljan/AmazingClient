/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Player;
import amazingsharedproject.User;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Robin
 */
public class StatsBar extends Application {

    private User user;
    private IGame game;
    
    Text name;
    Text health;
    Text kills;
    
    public StatsBar(User u, IGame g) {
        user=u;
        game= g;
    }
    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        Scene scene = new Scene(group, 200, 100, Color.BLACK);
        
        name = new Text("Username: " + user.getName());
        health = new Text("Health: XXX/XXX");
        kills = new Text("Kills: 0");
        
        List<Text> texts = new ArrayList<>();
        texts.add(name);
        texts.add(health);
        texts.add(kills);
        
        int yOff = 0;
        for(Text t: texts) {
            t.setFill(Color.YELLOW);
            t.setX(20);
            t.setY(yOff+=20);
            group.getChildren().add(t);
        }
        primaryStage.setTitle("Stats:");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Timer refresh = new Timer();
        refresh.scheduleAtFixedRate(new Refresh(), 1000, 500);
        //Scene scene = new Scene(primaryStage);
        
    }
    
    private class Refresh extends TimerTask {
        @Override
        public void run() {
            try {
                Player p = game.getPlayer(user.getUserID(), "");
                Platform.runLater(new Runnable() {
                       @Override
                       public void run() {
                           health.setText("Health: " + p.getHitpoints() + "/" + p.getBaseHitpoints());
                           kills.setText("Kills: " + p.getKills());
                       }
                });
            } catch (RemoteException ex) {
                Logger.getLogger(StatsBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    
}
