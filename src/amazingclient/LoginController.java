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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Hovsep
 */
public class LoginController implements Initializable {

    ILogin loginIn;
    public Stage stageStats;
    AmazingClient st = new AmazingClient();

    private ArrayList lobbyChat = new ArrayList();
    private ArrayList gameList = new ArrayList();
    private ArrayList userList = new ArrayList();
    private ArrayList gameLobbyChat = new ArrayList();
    private ArrayList gamePlayers = new ArrayList();
    private ArrayList tempgamePlayers = new ArrayList();
    private ArrayList tempLobbyChat = new ArrayList();

    private ObservableList ObservableLobbyChat;
    private ObservableList ObservableGames;
    private ObservableList<User> ObservableUsers;
    private ObservableList ObservableGameLobbyChat;
    private ObservableList ObservableGameLobbyPlayers;

    String gameName = "";
    StringProperty gameNameProperty;
    int roleID = 1;
    String userName = "";
    StringProperty userNameProperty;

    String chatinfo = "Loading chat...";
    Stage stage;
    Parent root;
    User user;

    //Timers
    Timer timer2 = new Timer("Timer");
    Timer playerTime = new Timer("fill");
    long delay = 0;
    long period = 1000;

    //RMI:
    private static final int port = 1098;
    private Registry registry;
    public IGameManager gameManager;
    //todo PAS DIT AAN
    private static final String ip = "localhost";

    //Login
    @FXML
    public TextField tfBeginUsername;
    @FXML
    public TextField tfBeginPassword = new TextField();
    @FXML
    public Button btBeginLogIn;
    @FXML
    public Button btBeginCreate;

    //Lobby    
    @FXML
    private Label LbLobbyUserName = new Label("Usernameeee");
    @FXML
    private TextArea tfLobbyChat = new TextArea();
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
    private ImageView imgageViewMage = new ImageView();
    @FXML
    private ImageView imgageViewRogue = new ImageView();
    @FXML
    private ImageView imgageViewWarrior = new ImageView();
    @FXML
    private ImageView imgageViewHunter = new ImageView();
    @FXML
    private ListView lvGamePlayer = new ListView();
    @FXML
    private ListView lvGameChat = new ListView();
    @FXML
    private TextArea TaGameChat = new TextArea();
    @FXML
    private Button BtGameSendChat;
    @FXML
    private Button BtGameReady;
    @FXML
    private Button BtGameStartGame;
    @FXML
    private Button btGameLobbyExit;
    @FXML
    private Label LbGameName = new Label("Pannekoek");

    @FXML
    private Label lbGameHunter = new Label();
    @FXML
    private Label lbGameMage = new Label();
    @FXML
    private Label lbGameWarrior = new Label();
    @FXML
    private Label lbGameRogue = new Label();
    //Create User
    @FXML
    private AnchorPane apCreateUser;
    @FXML
    private TextField tfNewUserUsername;
    @FXML
    private TextField tfNewUserPassword;
    @FXML
    private Button btCreateAccount;

    //Options music
    @FXML
    private Button btStartMusic;
    @FXML
    private Button btStopMusic;

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
        stageStats = new Stage();
        LobbySession.statStage = stageStats;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lobbyChat.add(chatinfo);
        gameLobbyChat.add("Loading games...");
        try {
            gameManager = (IGameManager) registry.lookup("GameManager");
        } catch (RemoteException d) {
            System.out.println("Kan registry niet vinden: " + d.getMessage());
        } catch (NotBoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Timer timer = new Timer("Timer");

        timer.scheduleAtFixedRate(new refreshTask(), delay, period);
        playerTime.scheduleAtFixedRate(new refreshPlayerListTask(), delay, period);
        initGameLobbyViews();

        gameNameProperty = new SimpleStringProperty(gameName);
        userNameProperty = new SimpleStringProperty(userName);

    }

    @FXML
    private void startMusic() throws IOException {
        Music.startMusic(1);
        btStartMusic.setVisible(false);
        btStopMusic.setVisible(true);
    }

    @FXML
    private void stopMusic() throws IOException {
        Music.stopMusic();
        Music.stopMusic();
        btStartMusic.setVisible(true);
        btStopMusic.setVisible(false);
    }

    @FXML
    private void initViews() {
        ObservableUsers = FXCollections.observableArrayList(this.userList);
        ObservableGames = FXCollections.observableArrayList(this.gameList);
        ObservableLobbyChat = FXCollections.observableArrayList(this.lobbyChat);

        this.lvLobbyUsers.setItems(this.ObservableUsers);
        this.lvLobbyGames.setItems(this.ObservableGames);
        this.lvLobbyChat.setItems(this.ObservableLobbyChat);
    }

    private void initGameLobbyViews() {

        ObservableGameLobbyChat = FXCollections.observableArrayList(this.gameLobbyChat);
        ObservableGameLobbyPlayers = FXCollections.observableArrayList(this.gamePlayers);

        this.lvGameChat.setItems(this.ObservableGameLobbyChat);
        this.lvGamePlayer.setItems(this.ObservableGameLobbyPlayers);
    }

    public void refreshList() throws SQLException, RemoteException {
        fillUserList();
        fillGameList();
        initViews();
        loadLobbyChat();

    }

    private void fillUserList() throws RemoteException {
        userList.clear();
        if (loginIn.getOnlineUsers() != null) {
            for (User onlineUser : loginIn.getOnlineUsers()) {
                userList.add(onlineUser.getName());
            }
        }
    }

    private void fillPlayerList() throws RemoteException {
        tempgamePlayers.clear();
        if (gameManager.getGames().size() >= 1) {
            if (LobbySession.game != null) {
                for (IGame g : gameManager.getGames()) {
                    if (LobbySession.game.getGameID() == g.getGameID()) {
                        if (g.getGameID() == LobbySession.game.getGameID()) {
                            for (Player s : g.getPlayers()) {
                                for (User u : loginIn.getOnlineUsers()) {
                                    if (s.getUserID() == u.getUserID()) {
                                        tempgamePlayers.add(u.getName());
                                        //System.out.println("Players in playerlist::" + tempgamePlayers.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        gamePlayers = tempgamePlayers;
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
                    } catch (SQLException | RemoteException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }

    }

    private class refreshPlayerListTask extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        fillPlayerList();
                        initGameLobbyViews();
                        loadGameLobbyChat();
                    } catch (RemoteException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }

    }

    private class launchGame extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (LobbySession.game.allPlayersReady()) {
                            timer2.cancel();
                            Music.stopMusic();
                            startGame();
                            Music.startMusic(2);
                            System.out.println("Starting game");
                        }
                    } catch (RemoteException | NotBoundException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
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
    public void Login() throws IOException, SQLException, NotBoundException {
        try {
            //check if the user is already online.
            for (User usr : loginIn.getOnlineUsers()) {
                if (usr.getName().equals(tfBeginUsername.getText())) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("User is already online");
                    alert.showAndWait();

                } else if (loginIn.Login(tfBeginUsername.getText(), tfBeginPassword.getText()) != null) {
                    user = loginIn.Login(tfBeginUsername.getText(), tfBeginPassword.getText());
                    LobbySession.user = user;
                    userName = user.getName();
                    LbLobbyUserName.textProperty().bind(userNameProperty);
                    loginIn.addToOnline(LobbySession.user);
                    stage = (Stage) btBeginLogIn.getScene().getWindow();
                    root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add((new File("css/Lobby.css")).toURI().toURL().toExternalForm());
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();

                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter correct name and password");
                    alert.showAndWait();
                }
            }
            if (loginIn.getOnlineUsers().isEmpty()) {
                user = loginIn.Login(tfBeginUsername.getText(), tfBeginPassword.getText());
                LobbySession.user = user;
                userName = user.getName();
                LbLobbyUserName.textProperty().bind(userNameProperty);
                loginIn.addToOnline(LobbySession.user);
                stage = (Stage) btBeginLogIn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add((new File("css/Lobby.css")).toURI().toURL().toExternalForm());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

            }
        } catch (Exception ex) {
            System.out.println("Not working: " + ex);
        }
    }

    @FXML
    public void CreateLobby(Event evt) throws IOException {
        stage = (Stage) btLobbyCreateGame.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("CreateGame.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add((new File("css/CreateGame.css")).toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void joinGame() throws RemoteException, IOException {

        for (IGame g : gameManager.getGames()) {

            if (g.getGameName().equals(lvLobbyGames.getSelectionModel().getSelectedItem())) {
                LobbySession.game = gameManager.joinLobby(g.getGameID(), LobbySession.user.getUserID());
                LobbySession.game.getPlayer(LobbySession.user.getUserID(), LobbySession.user.getName());
                timer2.scheduleAtFixedRate(new launchGame(), 0, 1000);
                LobbySession.game.addToGameChat("Player " + LobbySession.user.getName() + " has joined the game.");
                stage = (Stage) btLobbyCreateGame.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("GameLobby.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add((new File("css/GameLobby.css")).toURI().toURL().toExternalForm());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                initGameLobbyViews();

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
            LobbySession.game.getPlayer(LobbySession.user.getUserID(), LobbySession.user.getName());
            LobbySession.game.setGameName(tfCreateGameName.getText());

            //todo
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        LbGameName.setText(LobbySession.game.getGameName());
                    } catch (RemoteException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            timer2.scheduleAtFixedRate(new launchGame(), 0, 1000);
            stage = (Stage) tfCreateGameName.getScene().getWindow();

            root = FXMLLoader.load(getClass().getResource("GameLobby.fxml"));
            Scene scene = new Scene(root);

            scene.getStylesheets().add((new File("css/GameLobby.css")).toURI().toURL().toExternalForm());
            stage.setScene(scene);

            stage.setResizable(false);
            stage.show();

            initGameLobbyViews();

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
    public void startGame() throws NotBoundException, RemoteException, MalformedURLException {
        AMazeIng amazeing = new AMazeIng();
        //stage = (Stage) btBeginLogIn.getScene().getWindow();
        Stage stageAMazeIng = new Stage();
        amazeing.start(stageAMazeIng);

        
        StatsBar statsBar = new StatsBar(LobbySession.user, LobbySession.game);
        statsBar.start(stageStats);
        stageAMazeIng.show();
    }

    @FXML
    public void switchToCreateUser(Event evt) throws IOException {
        stage = (Stage) btBeginLogIn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("CreateUser.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add((new File("css/CreateUser.css")).toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void newUser(Event evt) throws SQLException, RemoteException {
        if (tfNewUserUsername.getText().equals("") || tfNewUserPassword.getText().equals("")) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please fill in username and password");
            alert.showAndWait();
        } else {
            loginIn.registerUser(tfNewUserUsername.getText(), tfNewUserPassword.getText());

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(tfNewUserUsername.getText() + " your account has been succesfully created");
            alert.showAndWait();
        }
    }

    @FXML
    public void backToLoginScreen() throws IOException {
        stage = (Stage) btCreateAccount.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add((new File("css/Login.css")).toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void backToLobby() throws IOException {
        stage = (Stage) btGameLobbyExit.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add((new File("css/Lobby.css")).toURI().toURL().toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setRole() throws RemoteException {
        LobbySession.game.changeRole(LobbySession.user.getUserID(), roleID);
    }

    @FXML
    public void setRoleMage() throws RemoteException {
        lbGameHunter.setTextFill(Color.web("#32cd32"));
        lbGameWarrior.setTextFill(Color.web("#32cd32"));
        lbGameMage.setTextFill(Color.web("#FFFFFF"));
        lbGameRogue.setTextFill(Color.web("#32cd32"));
        roleID = 1;
        setRole();
    }

    @FXML
    public void setRoleRogue() throws RemoteException {
        lbGameHunter.setTextFill(Color.web("#32cd32"));
        lbGameWarrior.setTextFill(Color.web("#32cd32"));
        lbGameMage.setTextFill(Color.web("#32cd32"));
        lbGameRogue.setTextFill(Color.web("#FFFFFF"));
        roleID = 0;
        setRole();
    }

    @FXML
    public void setRoleWarrior() throws RemoteException {
        lbGameHunter.setTextFill(Color.web("#32cd32"));
        lbGameWarrior.setTextFill(Color.web("#FFFFFF"));
        lbGameMage.setTextFill(Color.web("#32cd32"));
        lbGameRogue.setTextFill(Color.web("#32cd32"));

        roleID = 2;
        setRole();
    }

    @FXML
    public void setRoleHunter() throws RemoteException {

        lbGameHunter.setTextFill(Color.web("#FFFFFF"));
        lbGameWarrior.setTextFill(Color.web("#32cd32"));
        lbGameMage.setTextFill(Color.web("#32cd32"));
        lbGameRogue.setTextFill(Color.web("#32cd32"));
        roleID = 3;
        setRole();
    }

    private void loadGameLobbyChat() throws RemoteException {
        gameLobbyChat.clear();
        if (LobbySession.game != null) {
            if (LobbySession.game.loadGameChat().size() >= 1) {
                for (String s : LobbySession.game.loadGameChat()) {
                    gameLobbyChat.add(s);
                }
            }
        }
    }

    private void loadLobbyChat() throws RemoteException {
        lobbyChat.clear();
        if (gameManager.loadChat().size() >= 1) {
            for (String s : gameManager.loadChat()) {
                lobbyChat.add(s);
            }
        }
    }

    //Send a message to the chat. Adding it to the lobbychat.
    @FXML
    public void sendLobbyMessage(Event evt) throws RemoteException {

        if (!"".equals(tfLobbyChat.getText())) {
            gameManager.addToChat(LobbySession.user.getName() + ": " + tfLobbyChat.getText());
            initViews();
            tfLobbyChat.clear();
            loadLobbyChat();
        }
    }

    @FXML
    public void sendGameLobbyMessage(Event evt) throws RemoteException {
        if (!"".equals(TaGameChat.getText())) {
            LobbySession.game.addToGameChat(LobbySession.user.getName() + ": " + TaGameChat.getText());
            initGameLobbyViews();
            TaGameChat.clear();
            loadGameLobbyChat();
        }
    }

    @FXML
    public void setGameReady() throws RemoteException {
        LobbySession.game.setReady(LobbySession.game.getPlayer(LobbySession.user.getUserID(), LobbySession.user.getName()).getID(), true);
        LobbySession.game.addToGameChat("Player " + LobbySession.user.getName() + " is ready!");
        BtGameReady.setDisable(true);
    }
}
