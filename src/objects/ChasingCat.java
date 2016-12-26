package objects;

import org.newdawn.slick.Animation;

/**
 * @author Daniel J. Holland
 * @version 1.0
 *          Created on 12/25/2016.
 */
public class ChasingCat extends Character{

    private boolean isAlive;

    public ChasingCat(Animation up, Animation down, Animation left, Animation right) {
        super(up, down, left, right);
        isAlive = false;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void kill(){
        isAlive = false;
    }

    public void setAlive(){
        isAlive = true;
    }

}
