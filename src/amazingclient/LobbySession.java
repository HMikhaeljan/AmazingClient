/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;
import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.User;
import javafx.stage.Stage;

/**
 *
 * @author Jeroen0606
 */
public final class LobbySession {
    public static User user;
    public static IGame game;
    public static Stage statStage;
    
    private LobbySession() {
    }
}
