/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amazingclient;

import java.util.List;

/**
 *
 * @author Jeroen0606
 */
public interface IGame {
    public Maze getGrid();
    
    public List<IPlayer> getPlayers();
    
    public List<IAbility> getAbilities();
}
