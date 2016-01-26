/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import static amazingclient.AMazeIng.spritesize;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Player;
import amazingsharedproject.User;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Robin
 */
public class StatsBar extends Application {

    private User user;
    private IGame game;
    ArrayList msgs = new ArrayList();
    ObservableList Observablemsgs;
    Text name;
    Text health;
    Text kills;

    ListView lvMessages;

    public StatsBar(User u, IGame g) {
        user = u;
        game = g;
        lvMessages = new ListView();

    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException, RemoteException {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 600);
        primaryStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 400);
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        
        StackPane root = new StackPane();
        root.setId("pane");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add((new File("css/GameStats.css")).toURI().toURL().toExternalForm());

        //add listview
        StackPane.setMargin(lvMessages, new Insets(150, 95, 25, 80));
        root.getChildren().add(lvMessages);
 

        name = new Text("Username: " + user.getName());
        health = new Text("Health: XXX/XXX");
        kills = new Text("Kills: 0");

        List<Text> texts = new ArrayList<>();
        texts.add(name);
        texts.add(health);
        texts.add(kills);

        int yOff = 60;
        for (Text t : texts) {
            t.setFill(Color.LIMEGREEN);
            root.getChildren().add(t);
            yOff += 20;
            StackPane.setMargin(t, new Insets(yOff, 000, 200, 0));
        }

        primaryStage.setTitle("Stats:");
        primaryStage.setScene(scene);
        primaryStage.show();

        Timer refresh = new Timer();
        refresh.scheduleAtFixedRate(new Refresh(), 1000, 500);
        Timer refreshStatss = new Timer();
        refreshStatss.scheduleAtFixedRate(new RefreshStats(), 1000, 500);
        //Scene scene = new Scene(primaryStage);

    }

    public void initStats() {
        Observablemsgs = FXCollections.observableArrayList(this.msgs);

        this.lvMessages.setItems(this.Observablemsgs);
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

    private class RefreshStats extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //add messages to arraylist.
                    msgs.clear();
                    try {
                        for (String s : LobbySession.game.getGameState().getMessages()) {
                            msgs.add(s);
                        }
                        initStats();
                    } catch (RemoteException ex) {
                        Logger.getLogger(StatsBar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

    }

}
