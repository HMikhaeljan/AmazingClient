/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
/**
 *
 * @author Hovsep
 */
public class AmazingClient extends Application {

    LoginController log;
    int count = 0;

    @Override
    public void start(Stage stage) throws Exception {
        log = new LoginController();
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        Music.startMusic(1);
        //this line removes the borders.
        //stage.initStyle(StageStyle.UNDECORATED);

        scene.getStylesheets().add((new File("css/Login.css")).toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws RemoteException, IOException {
        Music.stopMusic();
        if (LobbySession.user != null) {
            log.loginIn.removeFromOnline(LobbySession.user);
        }
        /*if (LobbySession.game != null) {
         log.gameManager.removeLobby(LobbySession.game.getGameID());
         }*/
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
