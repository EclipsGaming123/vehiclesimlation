import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Pedestrian that tries to walk across the street
 */
public class Pedestrian extends SuperSmoothMover
{
    protected double speed;
    protected double maxSpeed;
    protected int direction; // direction is always -1 or 1, for moving down or up, respectively
    protected boolean awake;
    public Pedestrian(int direction) {
        this.direction = direction;
        this.awake = true;
    }
    
    /**
     * Act - do whatever the Pedestrian wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void knockDown () {
        speed = 0;
        setRotation (90);
        awake = false;
    }
    
    public void healMe () {
        speed = maxSpeed;
        setRotation (0);
        awake = true;
    }
    
    public boolean isAwake () {
        return awake;
    }
    
    public void move(int direction, double speed)
    {
        // If there is a v
        if (awake){
            if (getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(this.direction * this.speed)), Vehicle.class) == null){
                setLocation (getX(), getY() + (int)(speed*direction));
            }
            if (direction == -1 && getY() < 100){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > 550){
                getWorld().removeObject(this);
            }
        }
    }
}
