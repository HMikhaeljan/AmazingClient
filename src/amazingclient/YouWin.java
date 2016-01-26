/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Player;
import amazingsharedproject.User;
import java.awt.Font;
import java.io.File;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Robin
 */
public class YouWin extends Application {

    private Player player;

    public YouWin(Player player) {
        this.player = player;
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {

        StackPane root = new StackPane();
        root.setId("pane");
        Scene scene = new Scene(root, 750, 500);
        scene.getStylesheets().add((new File("css/GameWin.css")).toURI().toURL().toExternalForm());
        Text t = new Text("You made: " + player.getKills() + " kills!");
        t.setStyle("-fx-font: 50 arial;\n"
                + "    -fx-font-size: 32px;\n"
                + "    -fx-font-weight: bold;\n"
                + "    -fx-text-fill: #32cd32;\n"
                + "    -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 0,0,0,1 );");
        t.setFill(Color.LIMEGREEN);

        t.setX(150);
        t.setY(150);
        t.setLineSpacing(1);
        root.getChildren().add(t);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GAME OVER");
        primaryStage.show();
    }
}
