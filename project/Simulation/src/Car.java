// Car.java manages the properties of a car in the simulation, including its position, speed, direction, 
// -and handles the drawing of the car on the screen.

import java.awt.*;
import java.awt.geom.*;

public class Car extends Vehicle {    
    private float radius;
    private float speed;
    private Color color;
    private float directionRadians;  // 0 = facing +x, increases counter-clockwise
    private final float acceleration; // accel and decel, pixels per second squared

    public float getX() {
    return x;
}
public float getY() {
    return y;
}
    public Car(int x, int y, float radius, float speed, float directionRadians, Color color) {
        super(x, y);

        if (radius <= 0 || speed < 0) {
            throw new IllegalArgumentException("Radius and speed must be positive values.");
        }
        this.acceleration = 40 * (20/radius); // Adjust acceleration based on size
        this.radius = radius;
        this.speed = speed;
        this.color = color;
        this.directionRadians = directionRadians;
    }

    @Override
    public void move(double deltaSeconds) {
        x += (float) (Math.cos(directionRadians) * speed * deltaSeconds);
        y += (float) (Math.sin(directionRadians) * speed * deltaSeconds);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        g2d.fill(new Ellipse2D.Float(x - radius, y - radius, 2 * radius, 2 * radius)); // draw the car as a circle
        
        float endX = (float) (x + Math.cos(directionRadians) * radius);
        float endY = (float) (y + Math.sin(directionRadians) * radius);
        g2d.setColor(Color.BLACK);
        g2d.drawLine((int) x, (int) y, (int) endX, (int) endY);
    }

    public void shouldStopAtLight(double deltaSeconds) {
        slowDown(deltaSeconds);
    }






// Getters and Setters

    @Override
    public float getMass() {
        return radius * 10; // Arbitrary mass based on size
    }

    public float getDirectionDegrees() {
        return (float) Math.toDegrees(directionRadians);
    }
    public float getDirectionRadians() {
        return directionRadians;
    }
    public void setDirectionRadians(float radians) {
        this.directionRadians = radians;
    }
    public void setDirectionDegrees(float degrees) {
        this.directionRadians = (float) Math.toRadians(degrees);
    }
    public void turnDirection(float degrees) {
        directionRadians += Math.toRadians(degrees);
    }

    public float getRadius() {
        return radius;
    }
    
    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
       if (speed < 0) {
            throw new IllegalArgumentException("Speed must be a non-negative value.");
        }
        this.speed = speed;
    }
    public float getVelocityX() {
        return (float) (Math.cos(directionRadians) * speed);
    }
    public float getVelocityY() {
        return (float) (Math.sin(directionRadians) * speed);
    }
    public void setVelocity(float vx, float vy) {
        this.speed = (float) Math.sqrt(vx * vx + vy * vy);
        this.directionRadians = (float) Math.atan2(vy, vx);
    }

    public float getAcceleration() {
        return acceleration;
    }
    public void speedUp(double deltaSeconds) {
        float speedlimit = 100; // max speed limit
        speed += acceleration * deltaSeconds;
        if (speed > speedlimit) { 
            speed = speedlimit;
        }
    }
    public void slowDown(double deltaSeconds) {
        speed -= acceleration * deltaSeconds;
        if (speed < 0) {
            speed = 0;
        }
    }

}
