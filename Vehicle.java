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
    protected boolean crashed = false;
    protected boolean pushed = false;
    private GreenfootSound hitSound = new GreenfootSound("sounds/car hit.mp3");
    protected GreenfootSound bulldozerSound = new GreenfootSound("sounds/bulldozer.mp3");
    private boolean hitSoundPlayed = false;
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
        checkHitPedestrian();
        Bulldozer bulldozer = (Bulldozer)getOneIntersectingObject(Bulldozer.class);
        if (bulldozer != null)
        {
            bulldozer.push(this);
        }
        move (speed * direction);
    }

    /**
     * Accessor methods
     */
    public double getSpeed(){
        return speed;
    }
    
    public void push(Vehicle vehicle)
    {
        System.out.println("a vehicle push method is being called");
        return;
    }
    
    
    /*even though I only call the push when object is intance of bulldozer
    *a push method is needed in the vehicle class
    */
}
