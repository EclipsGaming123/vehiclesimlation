import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * The Car subclass
 */
public class Car extends Vehicle
{
    private GreenfootSound carHorn = new GreenfootSound("car horn.mp3");
    private GreenfootImage[] explosionAnimation = new GreenfootImage[6];
    private int currentImage = 0;
    private int actsPerImage = 0;
    private boolean toExplosion;
    private boolean exploded;
    public Car(VehicleSpawner origin) {
        super(origin); // call the superclass' constructor
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        yOffset = 0;
        
        //initializes images
    }

    public void act()
    {
        checkHitPedestrian();
        drive();
        if (checkEdge() || exploded){
            bulldozerSound.stop();
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
            //checks if the vehicle is crashed
            if (ahead.direction != direction || toExplosion)
            {
                //if vehicle ahead is moving in opposite direction and is a bulldozer
                if (ahead instanceof Bulldozer)
                {
                    ahead.push(this);
                }else
                {
                    explode();
                }
            }
            else{
                //vehicle ahead not null and the car is not crashed
                changeLanes(ahead);
                carHorn.play();
            }
        }
        move (speed * direction);
    }
    
    //changes lanes if vehicle ahead and space on lane beside it
    public void changeLanes(Vehicle ahead)
    {
        //probability of moving up
        double probabilityOfUp = Math.random();
        //probability that the car would not change lanes
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
                //makes car move still after going back so that it does not go onto car infront of it
                speed = 0;
            }else
            {
                speed = ahead.getSpeed();
            }
        }else if (getY() == 306)
        {
            //checks if on lane above center
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
                    speed = 0;
                }
            }else
            {
                //car will cross center
                setLocation(getX(), 360);
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 306);
                    speed = 0;
                    //moves back because the space is taken by a vehicle
                }else
                {
                    speed = 0;
                }
            }
        }else if (getY() == 360)
        {
            //checks if on lane under center
            if (probabilityOfUp > probabilityOfNotCrossing)
            {
                setLocation(getX(), 306);
                //car will cross center
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 360);
                    speed = 0;
                    //moves back because it is touching a vehicle
                }else
                {
                    speed = 0;
                }
            }else
            {
                setLocation(getX(), 414);
                //moves to the fourth lane
                if (isTouching(Vehicle.class))
                {
                    setLocation(getX(), 360);
                    speed = 0;
                }else
                {
                    speed = 0;
                }
            }
        }else if (getY() == 414)
        {
            //car is on bottom lane
            setLocation(getX(), 360);
            if (isTouching(Vehicle.class))
            {
                setLocation(getX(), 414);
                speed = 0;
            }else
            {
                speed = 0;
            }
        }
    }
    /**
     * When a Car hit's a Pedestrian, it should knock it over
     */
    //knocks down pedestrian if it is in front of it
    public void checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)speed + getImage().getWidth()/2, 0, Pedestrian.class);
        if (p != null && p.isAwake()){
            p.knockDown();
        }
    }
    
    

    public void explode()
    {
        //gets vehicles and pedestrians in the blast radius
        
        if (!toExplosion)
        {
            //only runs once during
            getWorld().addObject(new Boom(), getX(), getY());
        }
        
        toExplosion = true;
        
    }
}
