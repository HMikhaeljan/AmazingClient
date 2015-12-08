/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jeroen0606
 */
public interface ILobbyController extends Remote {

    public void startGame() throws RemoteException;

    public int getUserID() throws RemoteException;

    public boolean getReady() throws RemoteException;
}
