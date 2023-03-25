import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Car subclass
 */
public class Car extends Vehicle
{
    
    public Car(VehicleSpawner origin) {
        super(origin); // call the superclass' constructor
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        yOffset = 0;
    }

    public void act()
    {
        drive(); 
        checkHitPedestrian();
        if (checkEdge()){
            getWorld().removeObject(this);
        }

    }
    
    
    public void drive() 
    {
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2 + 4), 0, Vehicle.class);
        if (ahead == null)
        {
            speed = maxSpeed;
        }else {
            double probabilityOfUp = Math.random();
            Vehicle downVehicleBack = (Vehicle) getOneObjectAtOffset (-getImage().getWidth()/2, 54, Vehicle.class);
            Vehicle downVehicleFront = (Vehicle) getOneObjectAtOffset (getImage().getWidth()/2, 54, Vehicle.class);
            Vehicle upVehicleBack = (Vehicle) getOneObjectAtOffset (-getImage().getWidth()/2, -54, Vehicle.class);
            Vehicle upVehicleFront = (Vehicle) getOneObjectAtOffset (getImage().getWidth()/2, -54, Vehicle.class);
            double probabilityOfNotCrossing = 0.85;
            double probabilityOfCrossing = 1.0 - probabilityOfNotCrossing;
            //car is going faster than vehicle ahead of it, so it's changing lanes
            if (crash(ahead))
            {
                //spawn bulldozer
            }
            else{
                if (getY() == 252)
                {
                    //checks if on right center
                    setLocation(getX(), 306);
                    if (isTouching(Vehicle.class))
                    {
                        setLocation(getX(), 252);
                    }else
                    {
                        speed = ahead.getSpeed();
                    }
                }else if (getY() == 306)
                {
                    //checks if on left center
                    if (probabilityOfUp <= probabilityOfNotCrossing)
                    {
                        setLocation(getX(), 252);
                        if (isTouching(Vehicle.class))
                        {
                            setLocation(getX(), 306);
                        }else
                        {
                            speed = ahead.getSpeed();
                        }
                    }else
                    {
                        setLocation(getX(), getY() + 54);
                        if (isTouching(Vehicle.class))
                        {
                            setLocation(getX(), getY() - 54);
                        }else
                        {
                            speed = ahead.getSpeed();
                        }
                    }
                }else if (getY() == 360)
                {
                    if (probabilityOfUp <= probabilityOfCrossing)
                    {
                        setLocation(getX(), getY() - 54);
                        if (isTouching(Vehicle.class))
                        {
                            setLocation(getX(), getY() + 54);
                        }else
                        {
                            speed = ahead.getSpeed();
                        }
                    }else if(probabilityOfUp > probabilityOfCrossing)
                    {
                        if (isTouching(Vehicle.class))
                        {
                            setLocation(getX(), getY() + 54);
                        }else
                        {
                            speed = ahead.getSpeed();
                        }
                    }
                }else if (getY() == 414)
                {
                    setLocation(getX(), getY() - 54);
                    if (isTouching(Vehicle.class))
                    {
                        setLocation(getX(), getY() + 54);
                    }else
                    {
                        speed = ahead.getSpeed();
                    }
                }
                //changes lane
                //there is some mistake or sumthing
            }
        }
        move (speed * direction);
    }
    
    /**
     * When a Car hit's a Pedestrian, it should knock it over
     */
    public void checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        
        if (p != null){
            p.knockDown();
        }
    }
}
