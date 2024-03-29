import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Ambulance subclass
 */
public class Ambulance extends Vehicle
{
    GreenfootSound ambulanceSound = new GreenfootSound("sounds/ambulance.mp3");
    public Ambulance(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        
        maxSpeed = 2.5;
        speed = maxSpeed;
    }

    /**
     * Act - do whatever the Ambulance wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        drive();
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2 + 4), 0, Vehicle.class);
        if (ahead == null)
        {
            speed = maxSpeed;
        } else {
            speed = ahead.getSpeed();
        }
        checkHitPedestrian();
        if (checkEdge()){
            ambulanceSound.stop();
            getWorld().removeObject(this);
        }
    }

    //heals pedestrians from the dead
    public void checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneIntersectingObject(Pedestrian.class);
        if (p != null && !p.isAwake())
        {
            p.healMe();
            ambulanceSound.play();
        }
    }
    
    
}
