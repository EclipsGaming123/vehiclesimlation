import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

/**
 * The boom effect when a vehicle collides with another for too long and causes an explosion
 * class credit: Affan Bin Hasnat
 */
public class Boom extends Effect
{
    
    private int currentImage = 0;
    private int actsPerImage = 0;
    private boolean toExplosion;
    private boolean exploded;
    ArrayList<Vehicle> explodedVehicles;
    ArrayList<Pedestrian> pedestrianCasualties;
    ArrayList<Vehicle> allVehicles;
    boolean spedUp = false;
    SimpleTimer timer;
    public Boom(){
        super("images/explosion", 6);
        GreenfootSound explosion = new GreenfootSound("sounds/explosion.mp3");
        explosion.play();
    }
    //to check what Vehicles the Boom effect is touch
    //will remove all that it's touching
    private void blastRadius(int radius){
        explodedVehicles = new ArrayList<Vehicle>(getObjectsInRange(radius,Vehicle.class));
        pedestrianCasualties = new ArrayList<Pedestrian>(getObjectsInRange(radius,Pedestrian.class));
        for(Vehicle v : explodedVehicles){
            getWorld().removeObject(v);
        }
        for(Pedestrian p : pedestrianCasualties){
            p.knockDown();
        }
    }
    public void act()
    {
        blastRadius(150);
        allVehicles = new ArrayList<Vehicle>(getWorld().getObjects(Vehicle.class));
        if (!spedUp)
        {
            for (Vehicle vehicle: allVehicles)
            {
                vehicle.speedUp();
            }
            spedUp = true;
        }
        if (currentImage == 6)
        {
            exploded = true;
            getWorld().removeObject(this);
        }else
        {
            //makes the frames per second slower so it looks more like an explosion
            //act method runs 5 times before image changes
            if (actsPerImage % 5 == 0)
            {
                setImage(explosionAnimation[currentImage]);
                currentImage++;
            }
            actsPerImage++;
        }
    }
}
