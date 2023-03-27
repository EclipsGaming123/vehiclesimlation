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
        delayCount = 40;
    }

    /**
     * Act - do whatever the Bus wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {

        drive();
        if (checkEdge()){
            getWorld().removeObject(this);
        }
        
        //delayCount = (delayCount == 0) ? delayCount:delayCount--;
        //checks if delaycount is 0 and if it is not, subtract delayCount by 1
    }

    public void checkHitPedestrian () {
        Pedestrian intersectingPedestrian = (Pedestrian) this.getOneIntersectingObject(Pedestrian.class);
        if (intersectingPedestrian != null && intersectingPedestrian.isAwake())
        {
            speed = 0;
            intersectingPedestrian.setSpeed(0);
            delayCount--;
            if (delayCount == 0)
            {
                getWorld().removeObject(intersectingPedestrian);
                delayCount = 40;
                speed = maxSpeed;
            }
        }
    }    
    // methods to add
    
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
        move (speed * direction);
    }
}

