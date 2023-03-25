import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This is the superclass for Vehicles.
 * 
 */
public abstract class Vehicle extends SuperSmoothMover
{
    protected double maxSpeed;
    protected double speed;
    protected int direction; // 1 = right, -1 = left
    protected boolean moving;
    protected int yOffset;
    protected VehicleSpawner origin;
    
    protected abstract void checkHitPedestrian ();

    public Vehicle (VehicleSpawner origin) {
        this.origin = origin;
        moving = true;
        
        if (origin.facesRightward()){
            direction = 1;
            
        } else {
            direction = -1;
            getImage().mirrorHorizontally();
        }
    }
    
    public void addedToWorld (World w){
        setLocation (origin.getX() - (direction * 100), origin.getY() - yOffset);
    }

    /**
     * A method used by all Vehicles to check if they are at the edge.
     * 
     * Note that this World is set to unbounded (The World's super class is (int, int, int, FALSE) which means
     * that objects should not be stopped from leaving the World. However, this introduces a challenge as there
     * is the potential for objects to disappear off-screen but still be fully acting and thus wasting resources
     * and affecting the simulation even though they are not visible.
     */
    protected boolean checkEdge() {
        if (direction == 1)
        { // if moving right, check 200 pixels to the right (above max X)
            if (getX() > getWorld().getWidth() + 200){
                return true;
            }
        } 
        else 
        { // if moving left, check 200 pixels to the left (negative values)
            if (getX() < -200){
                return true;
            }
        }
        return false;
    }

    /**
     * Method that deals with movement. Speed can be set by individual subclasses in their constructors
     */
    public void drive() 
    {
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2 + 4), 0, Vehicle.class);
        if (ahead == null || ahead.getSpeed() > speed)
        {
            speed = maxSpeed;
        }else {
            speed = ahead.getSpeed();
        }
        move (speed * direction);
    }   

    /**
     * An accessor that can be used to get this Vehicle's speed. Used, for example, when a vehicle wants to see
     * if a faster vehicle is ahead in the lane.
     */
    public double getSpeed(){
        return speed;
    }
    
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }
    
    public int getDirection()
    {
        return direction;
    }
    
    public boolean crash(Vehicle other)
    {
        if (other != null && !(direction == other.direction))
        {
            speed = 0;
            other.setSpeed(0);
            return true;
        }
        return false;
    }
}
