import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Runner here.
 * 
 * @author Aous Alomari
 */
public class Runner extends Pedestrian
{

    public Runner(int direction)
    {
        super(direction);
        maxSpeed = Math.random()*5 + 1;
        speed = maxSpeed;
    }
    public void act()
    {
        move(direction, speed);
    }
}
