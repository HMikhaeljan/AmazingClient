/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import amazingsharedproject.Interfaces.IGame;
import amazingsharedproject.Interfaces.IGameManager;
import amazingsharedproject.Interfaces.ILogin;
import amazingsharedproject.Player;
import amazingsharedproject.User;
import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Hovsep
 */
public class LoginController implements Initializable {

    ILogin loginIn;
    AmazingClient st = new AmazingClient();

    private ArrayList lobbyChat = new ArrayList();
    private ArrayList gameList = new ArrayList();
    private ArrayList userList = new ArrayList();
    private ArrayList gameLobbyChat = new ArrayList();
    private ArrayList gamePlayers = new ArrayList();

    private ObservableList ObservableLobbyChat;
    private ObservableList ObservableGames;
    private ObservableList<User> ObservableUsers;
    private ObservableList ObservableGameLobbyChat;
    private ObservableList ObservableGameLobbyPlayers;

    String gameName = "fsggs";
    StringProperty gameNameProperty;

    String userName = "ahffd";
    StringProperty userNameProperty;

    String chatinfo = "Welkom bij Amazing chat";
    Stage stage;
    Parent root;
    User user;

    //RMI:
    private static final int port = 1099;
    private static final String bindName = "Test";
    private Registry registry;
    private IGameManager gameManager;
    //todo PAS DIT AAN
    private static final String ip = "192.168.15.1";

    //Login
    @FXML
    private TextField tfBeginUsername;
    @FXML
    private TextField tfBeginPassword;
    @FXML
    private Button btBeginLogIn;

    //Lobby    
    @FXML
    private Label LbLobbyUserName = new Label("Usernameeee");

    private TextArea tfLobbyChat;
    @FXML
    private ListView lvLobbyUsers = new ListView();
    @FXML
    private Button btLobbyCreateGame;
    @FXML
    private Button BtLobbyRefresh;
    @FXML
    private ListView lvLobbyGames = new ListView();
    @FXML
    private ListView lvLobbyChat = new ListView();

    //Create game
    @FXML
    private TextField tfCreateGameName;

    //GameLobby
    @FXML
    private ListView lvGamePlayer = new ListView();
    @FXML
    private ListView lvGameChat = new ListView();
    @FXML
    private TextArea TaGameChat;
    @FXML
    private Button BtGameSendChat;
    @FXML
    private Button BtGameReady;
    @FXML
    private Button BtGameStartGame;
    @FXML
    private Label LbGameName = new Label("Pannekoek");

    //Create User
    @FXML
    private AnchorPane apCreateUser;
    @FXML
    private TextField tfNewUserUsername;
    @FXML
    private TextField tfNewUserPassword;
    @FXML
    private Button btCreateAccount;

    public LoginController() {
        try {
            registry = LocateRegistry.getRegistry(ip, port);
        } catch (RemoteException e) {
            System.out.println("Kan registry niet vinden: " + e.getMessage());
        }

        try {
            loginIn = (ILogin) registry.lookup("UserManager");
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lobbyChat.add(chatinfo);
        gameLobbyChat.add("Game Lobby");

        try {
            gameManager = (IGameManager) registry.lookup("GameManager");
        } catch (RemoteException d) {
            System.out.println("Kan registry niet vinden: " + d.getMessage());
        } catch (NotBoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Timer timer = new Timer("Timer");

        long delay = 0;
        long period = 5000;
        timer.scheduleAtFixedRate(new refreshTask(), delay, period);

        gameNameProperty = new SimpleStringProperty(gameName);
        userNameProperty = new SimpleStringProperty(userName);

    }

    @FXML
    private void initViews() {
        ObservableUsers = FXCollections.observableArrayList(this.userList);
        ObservableGames = FXCollections.observableArrayList(this.gameList);
        ObservableLobbyChat = FXCollections.observableArrayList(this.lobbyChat);
        ObservableGameLobbyChat = FXCollections.observableArrayList(this.gameLobbyChat);
        ObservableGameLobbyPlayers = ObservableGameLobbyChat = FXCollections.observableArrayList(this.gamePlayers);

        this.lvLobbyUsers.setItems(this.ObservableUsers);
        this.lvLobbyGames.setItems(this.ObservableGames);
        this.lvLobbyChat.setItems(this.ObservableLobbyChat);
        this.lvGameChat.setItems(this.ObservableGameLobbyChat);
        this.lvGamePlayer.setItems(this.ObservableGameLobbyPlayers);
    }

    public void refreshList() throws SQLException, RemoteException {
        fillUserList();
        fillGameList();
        fillPlayerList();
        initViews();

    }

    private void fillUserList() throws RemoteException {
        userList.clear();
        if (loginIn.getOnlineUsers() != null) {
            for (User onlineUser : loginIn.getOnlineUsers()) {
                userList.add(onlineUser.getName());
            }
        } else {
            System.out.println("no users online");
        }
    }

    private void fillPlayerList() throws RemoteException {
        gamePlayers.clear();
        if (gameManager.getGames() != null) {
            for (IGame g : gameManager.getGames()) {
                for (Player s : g.getPlayers()) {
                    gamePlayers.add(s);
                }
            }
        } else {
            System.out.println("no users online");
        }
    }

    private void fillGameList() throws RemoteException {
        gameList.clear();

        if (gameManager.getGames() != null) {
            for (IGame g : gameManager.getGames()) {
                gameList.add(g.getGameName());
            }
        } else {
            System.out.println("no users online");
        }
    }

    private class refreshTask extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    try {
                        refreshList();
                    } catch (SQLException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }

    }

    /*
     /Check of de naam in de database gelijk is aan de textveld tfBeginUsername
     */
    @FXML
    public void Login(Event evt) throws IOException, SQLException, NotBoundException {
        if (loginIn.Login(tfBeginUsername.getText(), tfBeginPassword.getText()) != null) {
            user = loginIn.Login(tfBeginUsername.getText(), tfBeginPassword.getText());
            LobbySession.user = user;
            userName = user.getName();
            LbLobbyUserName.textProperty().bind(userNameProperty);
            loginIn.addToOnline(LobbySession.user);
            stage = (Stage) btBeginLogIn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please enter correct name and password");

            alert.showAndWait();
        }

    }

    @FXML
    public void CreateLobby(Event evt) throws IOException {
        stage = (Stage) btLobbyCreateGame.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("CreateGame.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void joinGame() throws RemoteException, IOException {
        for (IGame g : gameManager.getGames()) {
            System.out.println("Sleectinmodel: " + lvLobbyGames.getSelectionModel());
            System.out.println(g.getGameName());
            if (g.getGameName().equals(lvLobbyGames.getSelectionModel().getSelectedItem())) {
                gameManager.joinLobby(g.getGameID(), LobbySession.user.getUserID());

                stage = (Stage) btLobbyCreateGame.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("GameLobby.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                System.out.print(gameName);
                initViews();

            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Game Not Found");

                alert.showAndWait();
            }
        }
    }

    //Create a new game
//    @FXML
//    public void CreateGame(Event evt) throws IOException {
//        if ((tfCreateGameName.getText().equals(""))) {
//            Alert alert = new Alert(AlertType.INFORMATION);
//            alert.setTitle("Information Dialog");
//            alert.setHeaderText("Please fill in game name");
//            alert.showAndWait();
//        } else {
//            System.out.println("Why does this not work");
//            fakegames.add(tfCreateGameName.getText());
//            fakegames.add("WTF");
//            initViews();
//            //stage.close();
//            AmazingClient a = new AmazingClient();
//            //stage = (Stage) btBeginLogIn.getScene().getWindow();
//            Stage stageAMazeIng = new Stage();
//            a.start(stageAMazeIng);
//            stageAMazeIng.show();
//
////            stage.show();
//            //placeholder code//going back to the lobby // todo !!!!!!!!!!!!!!!!!!!!
//            /*stage = (Stage) btCreateCreateGame.getScene().getWindow();
//             root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
//             Scene scene = new Scene(root);
//             stage.setScene(scene);
//             stage.show();*/            //placeholder end
//        }
//    }
//    Create a new game
    @FXML
    public void CreateGame(Event evt) throws IOException, NotBoundException {
        if ((tfCreateGameName.getText().equals(""))) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please fill in game name");
            alert.showAndWait();
        } else {
            ///TODO

            LobbySession.game = gameManager.newLobby(LobbySession.user.getUserID());
            LobbySession.game.setGameName(tfCreateGameName.getText());

            LbGameName.setText(LobbySession.game.getGameName());

            stage = (Stage) tfCreateGameName.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GameLobby.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.print(gameName);
            initViews();

            //            stage.show();
            //placeholder code//going back to the lobby // todo !!!!!!!!!!!!!!!!!!!!
            /*stage = (Stage) btCreateCreateGame.getScene().getWindow();
             root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();*/            //placeholder end
        }
    }

    @FXML
    public void startGame() throws NotBoundException, RemoteException {
        AMazeIng amazeing = new AMazeIng();
        //stage = (Stage) btBeginLogIn.getScene().getWindow();
        Stage stageAMazeIng = new Stage();
        amazeing.start(stageAMazeIng);
        stageAMazeIng.show();
    }

    @FXML
    public void switchToCreateUser(Event evt) throws IOException {
        stage = (Stage) btBeginLogIn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("CreateUser.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void newUser(Event evt) throws SQLException, RemoteException {
        loginIn.registerUser(tfNewUserUsername.getText(), tfNewUserPassword.getText());
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(tfNewUserUsername.getText() + " your account has been succesfully created");
        alert.showAndWait();
    }

    //Send a message to the chat. Adding it to the fakechat.
    @FXML
    public void sendLobbyMessage(Event evt) {
        if (!"".equals(tfLobbyChat.getText())) {
            lobbyChat.add(tfLobbyChat.getText());
            initViews();
            tfLobbyChat.clear();
        }
    }

    @FXML
    public void backToLoginScreen() throws IOException {
        stage = (Stage) btCreateAccount.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void sendGameLobbyMessage(Event evt) {
        if (!"".equals(TaGameChat.getText())) {
            gameLobbyChat.add(TaGameChat.getText());
            initViews();
            TaGameChat.clear();
        }
    }
}
