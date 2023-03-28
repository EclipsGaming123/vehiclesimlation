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

    public Bulldozer(VehicleSpawner origin)
    {
        super(origin);
        maxSpeed = 1.5;
        speed = maxSpeed;
    }
    
    public void act()
    {
        drive();
        checkHitPedestrian();
        if (checkEdge()){
            getWorld().removeObject(this);
        }
    }
    
    public void checkHitPedestrian()
    {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        if (p != null && !p.isAwake())
        {
            getWorld().removeObject(p);
        }
    }
    
    public void push(Vehicle other)
    {
        other.maxSpeed = maxSpeed;
        other.direction = direction;
        other.pushed = true;
    }
}
