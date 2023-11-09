package Entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entities {
    protected float x,y;
    protected int width,height;
    protected Rectangle2D.Float hitbox;
    public Entities(float X,float Y,int W,int H){
        x=X;
        y=Y;
        width = W;
        height = H;
    }
    protected void drawHitbox(Graphics g,int xoffset){
        g.setColor(Color.CYAN);
        g.drawRect((int)hitbox.x-xoffset, (int)hitbox.y, (int)hitbox.width, (int)hitbox.height);
    }
    protected void initHitbox(float x,float y,float w,float h) {
        hitbox=new Rectangle2D.Float(x,y,w,h);
    }
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }
}
