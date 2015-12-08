/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import amazingclient.Block;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Jeroen0606
 */
public interface IGame extends Remote{

    public jdk.nashorn.internal.ir.Block[][] getGrid() throws RemoteException;
    
    //todo
    /*
    public List<IPlayer> getPlayers() throws RemoteException;
    */
}
