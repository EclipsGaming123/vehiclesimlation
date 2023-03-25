import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Bus subclass
 */
public class Bus extends Vehicle
{
    private int delayCount;
    public Bus(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        
        //Set up values for Bus
        maxSpeed = 1.5 + ((Math.random() * 10)/5);
        speed = maxSpeed;
        // because the Bus graphic is tall, offset it a up (this may result in some collision check issues)
        yOffset = 15;
    }

    /**
     * Act - do whatever the Bus wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (paused())
        {
            delayCount--;
            return;
        }else
        {
            drive();
        }
        
        
        if (checkEdge()){
            getWorld().removeObject(this);
        }
    }

    public void checkHitPedestrian () {
        return;
    }    
    // methods to add
    public boolean paused()
    {
        return delayCount > 0;
    }

    public void setDelay(int actCount)
    {
        delayCount = actCount;
    }
    
    public void drive()
    {
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2 + 4), 0, Vehicle.class);
        
        if (ahead == null)
        {
            speed = maxSpeed;
        } else {
            speed = ahead.getSpeed();
        }
        Pedestrian intersectingPedestrian = (Pedestrian) this.getOneIntersectingObject(Pedestrian.class);
        if (intersectingPedestrian != null && intersectingPedestrian.isAwake())
        {
            setDelay(50);
            speed = 0;
            getWorld().removeObject(intersectingPedestrian);
            return;
        }
        checkHitPedestrian();
        move (speed * direction);
    } 
    //bus keeps driving even after a civilian touches it ;-; plz change :_(
}
