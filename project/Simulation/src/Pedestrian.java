import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Pedestrian extends Objects {

    private float radius;
    private float speed;
    private float directionRadians;

    private boolean crossing;
    private boolean waiting;
    private boolean hit;

    private long hitTime;

    public Pedestrian(
            int x,
            int y,
            float radius,
            float speed,
            float directionRadians) {

        super(x, y);

        this.radius = radius;
        this.speed = speed;
        this.directionRadians = directionRadians;

        this.crossing = false;
        this.waiting = false;
        this.hit = false;
    }

    public void update(double deltaSeconds) {

        if (hit || waiting) {
            return;
        }

        x += (float) (
                Math.cos(directionRadians)
                        * speed
                        * deltaSeconds
        );

        y += (float) (
                Math.sin(directionRadians)
                        * speed
                        * deltaSeconds
        );
    }

    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        if (hit) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.BLUE);
        }

        g2d.fill(
                new Ellipse2D.Float(
                        x - radius,
                        y - radius,
                        radius * 2,
                        radius * 2
                )
        );

        // Direction indicator
        g2d.setColor(Color.BLACK);

        float endX = (float) (
                x
                        + Math.cos(directionRadians)
                        * radius
        );

        float endY = (float) (
                y
                        + Math.sin(directionRadians)
                        * radius
        );

        g2d.drawLine(
                (int) x,
                (int) y,
                (int) endX,
                (int) endY
        );
    }

    public void setCrossing(boolean crossing) {
        this.crossing = crossing;
    }

    public boolean isCrossing() {
        return crossing;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void hit() {

        if (!hit) {
            hit = true;
            speed = 0;
            hitTime = System.currentTimeMillis();
        }
    }

    public boolean isHit() {
        return hit;
    }

    public boolean shouldRemove() {

        if (hit) {
            return System.currentTimeMillis()
                    - hitTime >= 2000;
        }

        return x < -30
                || x > 830
                || y < -30
                || y > 830;
    }

    public float getRadius() {
        return radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirectionRadians() {
        return directionRadians;
    }
}