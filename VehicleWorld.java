import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Citations: 
 * car horn: https://www.zapsplat.com/music/car-horn-beep-short/
 * site: https://www.zapsplat.com
 * 
 * ambulance sound: https://www.youtube.com/watch?v=PtWx9_RGH6I
 * site: https://www.youtube.com
 * author: UNLIMITED SOUNDS
 * 
 * nighttime ambience sound: https://www.youtube.com/watch?v=Z89gpveGiUM
 * site: https://www.youtube.com
 * author: MIXTAPE
 * 
 * explosion sound: https://www.youtube.com/watch?v=MdO3_r6juRU
 * site: https://www.youtube.com
 * author: Diman4ik
 * 
 * bulldozer sound: https://www.youtube.com/watch?v=UI4AVTW8moQ
 * site: https://www.youtube.com
 * author: SyentifikFilms
 * 
 * pedestrian hit: https://www.youtube.com/watch?v=hcS-DAvsgyk
 * site: https://www.youtube.com
 * author: Ted That's me
 * 
 * 
 * Description/features:
 * 
 * Cars have the ability to knock down pedestrians
 * 
 * Ambulances have the ability to bring Pedestrians back from the dead
 * Buses have the ability to pick pedestrians up
 * 
 * Cars have the ability to change lanes if the lane beside it is clear
 * If a car is at the center, it has a 15% chance of crossing to the other side of the road every act method
 * 
 * Cars explode with vehicles going the opposite direction(except bulldozers) 
 * Bulldozers have the ability to push cars going the opposite way
 * pedestrians within the blast radius get knocked down when the car explodes
 * 
 * Known Bugs: 
 * 
 * bulldozers cannot sweep the lane, they only push cars that move in the opposite direction
 * cars keep moving forward after exploding if they crash into an ambulance
 */
public class VehicleWorld extends World
{
    private GreenfootImage background;

    // Color Constants
    public static Color GREY_BORDER = new Color (108, 108, 108);
    public static Color GREY_STREET = new Color (88, 88, 88);
    public static Color YELLOW_LINE = new Color (255, 216, 0);

    // Instance variables / Objects
    private boolean twoWayTraffic, splitAtCenter;
    private int laneHeight, laneCount, spaceBetweenLanes;
    private int[] lanePositionsY;
    private VehicleSpawner[] laneSpawners;
    private int timer;
    private int backgroundNumber = 1;
    GreenfootSound nightSound = new GreenfootSound("sounds/night ambience.mp3");
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public VehicleWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1, false); 

        setPaintOrder (Bus.class, Car.class, Ambulance.class, Bulldozer.class, Pedestrian.class);

        // set up background
        background = new GreenfootImage ("background" + backgroundNumber + ".png");
        setBackground (background);

        // Set critical variables
        laneCount = 4;
        laneHeight = 48;
        spaceBetweenLanes = 6;
        splitAtCenter = true;
        twoWayTraffic = true;

        // Init lane spawner objects 
        laneSpawners = new VehicleSpawner[laneCount];

        // Prepare lanes method - draws the lanes
        lanePositionsY = prepareLanes (this, background, laneSpawners, 222, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic, splitAtCenter);

        prepare();
        timer = 0;
    }

    public void act () {
        //spawns vehicle, and changes  time of day
        spawn();
        timer++;
        prepare();
    }

    private void spawn () {
        // Chance to spawn a vehicle
        if (Greenfoot.getRandomNumber (60) == 0){
            int lane = Greenfoot.getRandomNumber(laneCount);
            if (!laneSpawners[lane].isTouchingVehicle()){
                int vehicleType = Greenfoot.getRandomNumber(4);
                if (vehicleType == 0)
                {
                    addObject(new Car(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 1)
                {
                    addObject(new Bus(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 2)
                {
                    addObject(new Ambulance(laneSpawners[lane]), 0, 0);
                } else if (vehicleType == 3)
                {
                    addObject(new Bulldozer(laneSpawners[lane]), 0, 0);
                }
            }
        }

        // Chance to spawn a Pedestrian
        if (Greenfoot.getRandomNumber (60) == 0){
            int xSpawnLocation = Greenfoot.getRandomNumber (600) + 100; // random between 99 and 699, so not near edges
            boolean spawnAtTop = Greenfoot.getRandomNumber(2) == 0 ? true : false;

            if (spawnAtTop){
                Pedestrian newHuman = Greenfoot.getRandomNumber(2) == 0 ? new walker(1) : new Runner(1);
                addObject(newHuman, xSpawnLocation, 50);
            } else {
                Pedestrian newHuman = Greenfoot.getRandomNumber(2) == 0 ? new walker(-1) : new Runner(-1);
                addObject(newHuman, xSpawnLocation, 550);
            }
        }
    }

    public void spawn(Vehicle other)
    {
        //change lane variable to get to lane number of crashed vehicle
        int lane = Greenfoot.getRandomNumber(laneCount);
        if (!laneSpawners[lane].isTouchingVehicle()){
            int vehicleType = Greenfoot.getRandomNumber(4);
            if (vehicleType == 0)
            {
                addObject(new Car(laneSpawners[lane]), 0, 0);
            } else if (vehicleType == 1)
            {
                addObject(new Car(laneSpawners[lane]), 0, 0);
            } else if (vehicleType == 2)
            {
                addObject(new Bulldozer(laneSpawners[lane]), 0, 0);
            } else if (vehicleType == 3)
            {
                addObject(new Bulldozer(laneSpawners[lane]), 0, 0);
            }
        }
    }

    /**
     *  Given a lane number (zero-indexed), return the y position
     *  in the centre of the lane. (doesn't factor offset, so 
     *  watch your offset, i.e. with Bus).
     *  
     *  @param lane the lane number (zero-indexed)
     *  @return int the y position of the lane's center, or -1 if invalid
     */
    public int getLaneY (int lane){
        if (lane < lanePositionsY.length){
            return lanePositionsY[lane];
        } 
        return -1;
    }

    /**
     * Given a y-position, return the lane number (zero-indexed).
     * Note that the y-position must be valid, and you should 
     * include the offset in your calculations before calling this method.
     * For example, if a Bus is in a lane at y=100, but is offset by -20,
     * it is actually in the lane located at y=80, so you should send
     * 80 to this method, not 100.
     * 
     * @param y - the y position of the lane the Vehicle is in
     * @return int the lane number, zero-indexed
     * 
     */
    public int getLane (int y){
        for (int i = 0; i < lanePositionsY.length; i++){
            if (y == lanePositionsY[i]){
                return i;
            }
        }
        return -1;
    }

    public static int[] prepareLanes (World world, GreenfootImage target, VehicleSpawner[] spawners, int startY, int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit, int centreSpacing)
    {
        // Declare an array to store the y values as I calculate them
        int[] lanePositions = new int[lanes];
        // Pre-calculate half of the lane height, as this will frequently be used for drawing.
        // To help make it clear, the heightOffset is the distance from the centre of the lane (it's y position)
        // to the outer edge of the lane.
        int heightOffset = heightPerLane / 2;

        // draw top border
        target.setColor (GREY_BORDER);
        target.fillRect (0, startY, target.getWidth(), spacing);

        // Main Loop to Calculate Positions and draw lanes
        for (int i = 0; i < lanes; i++){
            // calculate the position for the lane
            lanePositions[i] = startY + spacing + (i * (heightPerLane+spacing)) + heightOffset ;
            // draw lane
            target.setColor(GREY_STREET); 
            // the lane body
            target.fillRect (0, lanePositions[i] - heightOffset, target.getWidth(), heightPerLane);
            // the lane spacing - where the white or yellow lines will get drawn
            target.fillRect(0, lanePositions[i] + heightOffset, target.getWidth(), spacing);

            // Place spawners and draw lines depending on whether its 2 way and centre split
            if (twoWay && centreSplit){
                // first half of the lanes go rightward (no option for left-hand drive, sorry UK students .. ?)
                if ( i < lanes / 2){
                    spawners[i] = new VehicleSpawner(false, heightPerLane);
                    world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                } else { // second half of the lanes go leftward
                    spawners[i] = new VehicleSpawner(true, heightPerLane);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw yellow lines if middle 
                if (i == lanes / 2){
                    target.setColor(YELLOW_LINE);
                    target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                } else if (i > 0){ // draw white lines if not first lane
                    for (int j = 0; j < target.getWidth(); j += 120){
                        target.setColor (Color.WHITE);
                        target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                } 

            } else if (twoWay){ // not center split
                if ( i % 2 == 0){
                    spawners[i] = new VehicleSpawner(false, heightPerLane);
                    world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                } else {
                    spawners[i] = new VehicleSpawner(true, heightPerLane);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw Grey Border if between two "Streets"
                if (i > 0){ // but not in first position
                    if (i % 2 == 0){
                        target.setColor(GREY_BORDER);
                        target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                    } else { // draw dotted lines
                        for (int j = 0; j < target.getWidth(); j += 120){
                            target.setColor (YELLOW_LINE);
                            target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                        }
                    } 
                }
            } else { // One way traffic
                spawners[i] = new VehicleSpawner(true, heightPerLane);
                world.addObject(spawners[i], 0, lanePositions[i]);
                if (i > 0){
                    for (int j = 0; j < target.getWidth(); j += 120){
                        target.setColor (Color.WHITE);
                        target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                }
            }
        }
        // draws bottom border
        target.setColor (GREY_BORDER);
        target.fillRect (0, lanePositions[lanes-1] + heightOffset, target.getWidth(), spacing);

        return lanePositions;
    }

    /**
     * <p>The prepareLanes method is a static (standalone) method that takes a list of parameters about the desired roadway and then builds it.</p>
     * 
     * <p><b>Note:</b> So far, Centre-split is the only option, regardless of what values you send for that parameters.</p>
     *
     * <p>This method does three things:</p>
     * <ul>
     *  <li> Determines the Y coordinate for each lane (each lane is centered vertically around the position)</li>
     *  <li> Draws lanes onto the GreenfootImage target that is passed in at the specified / calculated positions. 
     *       (Nothing is returned, it just manipulates the object which affects the original).</li>
     *  <li> Places the VehicleSpawners (passed in via the array parameter spawners) into the World (also passed in via parameters).</li>
     * </ul>
     * 
     * <p> After this method is run, there is a visual road as well as the objects needed to spawn Vehicles. Examine the table below for an
     * in-depth description of what the roadway will look like and what each parameter/component represents.</p>
     * 
     * <pre>
     *                  <=== Start Y
     *  ||||||||||||||  <=== Top Border
     *  /------------\
     *  |            |  
     *  |      Y[0]  |  <=== Lane Position (Y) is the middle of the lane
     *  |            |
     *  \------------/
     *  [##] [##] [##| <== spacing ( where the lane lines or borders are )
     *  /------------\
     *  |            |  
     *  |      Y[1]  |
     *  |            |
     *  \------------/
     *  ||||||||||||||  <== Bottom Border
     * </pre>
     * 
     * @param world     The World that the VehicleSpawners will be added to
     * @param target    The GreenfootImage that the lanes will be drawn on, usually but not necessarily the background of the World.
     * @param spawners  An array of VehicleSpawner to be added to the World
     * @param startY    The top Y position where lanes (drawing) should start
     * @param heightPerLane The height of the desired lanes
     * @param lanes     The total number of lanes desired
     * @param spacing   The distance, in pixels, between each lane
     * @param twoWay    Should traffic flow both ways? Leave false for a one-way street (Not Yet Implemented)
     * @param centreSplit   Should the whole road be split in the middle? Or lots of parallel two-way streets? Must also be two-way street (twoWay == true) or else NO EFFECT
     * 
     */
    public static int[] prepareLanes (World world, GreenfootImage target, VehicleSpawner[] spawners, int startY, int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit){
        return prepareLanes (world, target, spawners, startY, heightPerLane, lanes, spacing, twoWay, centreSplit, spacing);
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        //changes time of day when timer hits 400
        if (timer > 400)
        {
            backgroundNumber %= 4;
            backgroundNumber++;
            //plays night ambience noise when it is nightime
            if (backgroundNumber == 4)
            {
                nightSound.playLoop();
            }else
            {
                //stops playing noise when daytime comes
                nightSound.stop();
            }
            background = new GreenfootImage ("background" + backgroundNumber + ".png");
            setBackground (background);
            //prepares the lanes
            prepareLanes (this, background, laneSpawners, 222, laneHeight, laneCount, spaceBetweenLanes, twoWayTraffic, splitAtCenter);
            timer %= 400;
        }
        
    
    }
    
    //pauses night ambience sound after world is paused
    public void stopped() {
        nightSound.pause();
    }
}
