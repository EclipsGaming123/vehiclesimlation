import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Effect here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 * 
 * credit: Affan Bin Hasnat
 */
public abstract class Effect extends Actor
{
    protected GreenfootImage image;
    protected GreenfootImage[] explosionAnimation = new GreenfootImage[6];
    public Effect(String imageName, int imageNumber){
        for (int i = 1; i <= imageNumber; i++)
        {
            explosionAnimation[i-1] = new GreenfootImage(imageName + i + ".png");
        }
        setImage(new GreenfootImage(imageName + 1 + ".png"));
    }
}
