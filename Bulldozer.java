import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class bulldozer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bulldozer extends Vehicle
{
    /**
     * Act - do whatever the bulldozer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    //bulldozer constructor
    public Bulldozer(VehicleSpawner origin)
    {
        super(origin);
        maxSpeed = 1.5;
        speed = maxSpeed;
    }
    
    public void act()
    {
        //drives forward
        drive();
        //checks hit pedestrian
        checkHitPedestrian();
        if (checkEdge()){
            getWorld().removeObject(this);
        }
    }
    
    //removes touching pedestrian
    public void checkHitPedestrian()
    {
        Pedestrian p = (Pedestrian)getOneIntersectingObject(Pedestrian.class);
        if (p != null && !p.isAwake())
        {
            //if touching pedestrian, remove pedestrian
            getWorld().removeObject(p);
        }
    }
    
    //pushes vehicle
    public void push(Vehicle vehicle)
    {
        vehicle.maxSpeed = speed;
        vehicle.direction = direction;
        vehicle.pushed = true;
    }
}
