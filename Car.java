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
        move (speed * direction);
        if (checkEdge()){
            getWorld().removeObject(this);
        }
    }
    
    public void drive() 
    {
        Vehicle ahead = (Vehicle) getOneObjectAtOffset (direction * (int)(speed + getImage().getWidth()/2) + 4, 0, Vehicle.class);
        if (ahead == null)
        {
            speed = maxSpeed;
            //moves at intended max speed if there is no vehicle ahead
        }else {
            
            //car is going faster than vehicle ahead of it, so it's changing lanes
            //checks if the vehicle is crashed and if it is, it spawns bulldozer
            if (crash(ahead) || pushed)
            {
                //spawn bulldozer on the lane
            }
            else{
                //vehicle ahead not null and the car is not crashed
                changeLanes(ahead);
            }
        }
    }
    
    public void changeLanes(Vehicle ahead)
    {
        double probabilityOfUp = Math.random();
        double probabilityOfNotCrossing = 0.85;
        if (getY() == 252)
        {
            //moves car to the right
            setLocation(getX(), 306);
            /*check if touching vehicle after moving
            * and sends back if touching a vehicle on other lane 
            */
            if (isTouching(Vehicle.class))
            {
                setLocation(getX(), 252);
                speed = 0;
            }else
            {
                speed = ahead.getSpeed();
            }
        }else if (getY() == 306)
        {
            //checks if on left center
            if (probabilityOfUp <= probabilityOfNotCrossing)
            {
                //checks the probability of crossing and if it is below probabilityOfNotCrossing, it will not cross
                setLocation(getX(), 252);
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 306);
                    speed = 0;
                }else
                {
                    speed = ahead.getSpeed();
                }
            }else
            {
                setLocation(getX(), 360);
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 306);
                    speed = 0;
                }else
                {
                    speed = ahead.getSpeed();
                }
            }
        }else if (getY() == 360)
        {
            if (probabilityOfUp > probabilityOfNotCrossing)
            {
                setLocation(getX(), 306);
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 360);
                    speed = 0;
                }else
                {
                    speed = ahead.getSpeed();
                }
            }else
            {
                setLocation(getX(), 414);
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 360);
                    speed = 0;
                }else
                {
                    speed = ahead.getSpeed();
                }
            }
        }else if (getY() == 414)
        {
            setLocation(getX(), 360);
            if (isTouching(Vehicle.class))
            {
                setLocation(getX(), 414);
                speed = 0;
            }else
            {
                speed = ahead.getSpeed();
            }
        }
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
