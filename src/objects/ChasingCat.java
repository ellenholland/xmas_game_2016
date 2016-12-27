package objects;

import org.newdawn.slick.Animation;

/**
 * @author Daniel J. Holland
 * @version 1.0
 * Created on 12/25/2016.
 */
public class ChasingCat extends Character{

    private boolean isAlive;
    private boolean isDying;
    private Animation dyingAnim;

    public ChasingCat(Animation up, Animation down, Animation left, Animation right, Animation dyingAnim) {
        super(up, down, left, right);
        this.dyingAnim = dyingAnim;
        this.dyingAnim.setLooping(false);
        isAlive = false;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public boolean isDying(){
        return isDying;
    }

    public void kill(){
        isDying = true;
        this.setAnimation(dyingAnim);
    }

    public void setAlive(){
        this.setAnimation(AnimationDirection.DOWN);
        isAlive = true;
    }

    @Override
    public void update(int delta){
        if (isDying() && this.getCurrentAnimation().isStopped()){
            isDying = false;
            isAlive = false;
            this.dyingAnim.restart();
        }
        else {
            super.update(delta);
        }
    }

}
