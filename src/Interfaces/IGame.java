/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import amazingclient.Block;
import java.util.List;

/**
 *
 * @author Jeroen0606
 */
public interface IGame {
    public Block[][] getGrid();
    
    public List<IPlayer> getPlayers();
    
    public List<IAbility> getAbilities();
}
