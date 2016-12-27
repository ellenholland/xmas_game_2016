package objects;

import org.newdawn.slick.Animation;

/**
 * @author Daniel J. Holland
 * @version 1.0
 * Created on 12/26/2016.
 */
public class Present {

    private Animation currentAnimation;
    private Animation visibleAnimation;
    private Animation collectedAnimation;
    private boolean isVisible;

    private float xPos;
    private float yPos;

    public Present(Animation visibleAnimation, Animation collectedAnimation){
        this.visibleAnimation = visibleAnimation;
        this.collectedAnimation = collectedAnimation;
        this.currentAnimation = this.visibleAnimation;
        this.isVisible = false;

    }

    public void setXPosition(float xPos){
        this.xPos = xPos;
    }

    public void setYPosition(float yPos){
        this.yPos = yPos;
    }

    public void setPosition(float xPos, float yPos){
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setVisible(){
        isVisible = true;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public float getX(){
        return xPos;
    }

    public float getY(){
        return yPos;
    }

    public void playVisibleAnimation(){
        currentAnimation = visibleAnimation;
    }

    public void playCollectedAnimation(){
        currentAnimation = collectedAnimation;
    }

    public void draw(){
        currentAnimation.draw(xPos, yPos);
    }

}
