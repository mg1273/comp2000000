
import java.awt.Graphics;

public class PedestrianLightManager {

    private LightManager trafficLights;

    private Lights top;
    private Lights bottom;
    private Lights left;
    private Lights right;

    public PedestrianLightManager(LightManager trafficLights) {

        this.trafficLights = trafficLights;

        // Create separate pedestrian lights
        top = new Lights(470, 270, 16, 16);
        bottom = new Lights(314, 514, 16, 16);
        left = new Lights(270, 314, 16, 16);
        right = new Lights(514, 470, 16, 16);

        update();
    }

    public void update() {

        // Pedestrian signals only turn green
        // during the vehicle all-red phases.
        int vehicleState = trafficLights.getState();

        boolean pedestrianGreen =
                vehicleState == 3 || vehicleState == 6;

        if (pedestrianGreen) {

            top.changeLight(1);
            bottom.changeLight(1);
            left.changeLight(1);
            right.changeLight(1);

        } else {

            top.changeLight(3);
            bottom.changeLight(3);
            left.changeLight(3);
            right.changeLight(3);
        }
    }

    public boolean canCrossTop() {
        return top.isGreen();
    }

    public boolean canCrossBottom() {
        return bottom.isGreen();
    }

    public boolean canCrossLeft() {
        return left.isGreen();
    }

    public boolean canCrossRight() {
        return right.isGreen();
    }

    public void draw(Graphics g) {

        top.spawnLight(g);
        bottom.spawnLight(g);
        left.spawnLight(g);
        right.spawnLight(g);
    }
}
