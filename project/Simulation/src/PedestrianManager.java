import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PedestrianManager {

    private List<Pedestrian> pedestrians;

    private LightManager trafficLights;
    private VehicleManager vehicleManager;

    private static final int PEDESTRIAN_SPAWN_INTERVAL_MS = 2000;
    private int lastPedestrianSpawnTime = 0;

    private static final float WALK_SPEED = 100;

    public PedestrianManager(LightManager trafficLights, VehicleManager vehicleManager) {
        this.trafficLights = trafficLights;
        this.vehicleManager = vehicleManager;
        pedestrians = new ArrayList<>();
    }

    public void update(double deltaTime, float worldTimer) {

        // Spawn pedestrians
        if (worldTimer - lastPedestrianSpawnTime >= PEDESTRIAN_SPAWN_INTERVAL_MS) {

            Random random = new Random();
            int side = random.nextInt(4);

            Pedestrian pedestrian = null;

            switch (side) {

                // Top: right to left
                case 0:
                    pedestrian = new Pedestrian(
                        800,
                        295,
                        7,
                        WALK_SPEED,
                        (float) Math.PI
                    );
                    break;

                // Bottom: left to right
                case 1:
                    pedestrian = new Pedestrian(
                        0,
                        505,
                        7,
                        WALK_SPEED,
                        0
                    );
                    break;

                // Left: bottom to top
                case 2:
                    pedestrian = new Pedestrian(
                        295,
                        800,
                        7,
                        WALK_SPEED,
                        (float) (3 * Math.PI / 2)
                    );
                    break;

                // Right: top to bottom
                case 3:
                    pedestrian = new Pedestrian(
                        505,
                        0,
                        7,
                        WALK_SPEED,
                        (float) (Math.PI / 2)
                    );
                    break;
            }

            pedestrians.add(pedestrian);
            lastPedestrianSpawnTime = (int) worldTimer;
        }

        // Update pedestrians
        for (Pedestrian pedestrian : pedestrians) {

            if (pedestrian.isHit()) {
                continue;
            }

            handleCrossing(pedestrian);
            pedestrian.update(deltaTime);
            checkCarCollision(pedestrian);
        }

        // Remove pedestrians
        Iterator<Pedestrian> iterator = pedestrians.iterator();

        while (iterator.hasNext()) {

            Pedestrian pedestrian = iterator.next();

            if (pedestrian.shouldRemove()) {
                iterator.remove();
            }
        }
    }

    private void handleCrossing(Pedestrian pedestrian) {

        float x = pedestrian.getX();
        float y = pedestrian.getY();

        // Top crossing
        if (Math.abs(y - 295) < 1) {

            if (pedestrian.isCrossing()) {

                if (x <= 300) {
                    pedestrian.setCrossing(false);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                }

                return;
            }

            if (x <= 500 && x > 480) {

                if (verticalRoadIsRed()) {
                    pedestrian.setCrossing(true);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                } else {
                    pedestrian.setWaiting(true);
                    pedestrian.setSpeed(0);
                }

                return;
            }
        }

        // Bottom crossing
        if (Math.abs(y - 505) < 1) {

            if (pedestrian.isCrossing()) {

                if (x >= 500) {
                    pedestrian.setCrossing(false);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                }

                return;
            }

            if (x >= 300 && x < 320) {

                if (verticalRoadIsRed()) {
                    pedestrian.setCrossing(true);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                } else {
                    pedestrian.setWaiting(true);
                    pedestrian.setSpeed(0);
                }

                return;
            }
        }

        // Left crossing
        if (Math.abs(x - 295) < 1) {

            if (pedestrian.isCrossing()) {

                if (y <= 300) {
                    pedestrian.setCrossing(false);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                }

                return;
            }

            if (y <= 500 && y > 480) {

                if (horizontalRoadIsRed()) {
                    pedestrian.setCrossing(true);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                } else {
                    pedestrian.setWaiting(true);
                    pedestrian.setSpeed(0);
                }

                return;
            }
        }

        // Right crossing
        if (Math.abs(x - 505) < 1) {

            if (pedestrian.isCrossing()) {

                if (y >= 500) {
                    pedestrian.setCrossing(false);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                }

                return;
            }

            if (y >= 300 && y < 320) {

                if (horizontalRoadIsRed()) {
                    pedestrian.setCrossing(true);
                    pedestrian.setWaiting(false);
                    pedestrian.setSpeed(WALK_SPEED);
                } else {
                    pedestrian.setWaiting(true);
                    pedestrian.setSpeed(0);
                }

                return;
            }
        }

        if (!pedestrian.isCrossing() && !pedestrian.isWaiting()) {
            pedestrian.setSpeed(WALK_SPEED);
        }
    }

    private boolean verticalRoadIsRed() {

        return trafficLights.getLightTop().isRed()
                && trafficLights.getLightBottom().isRed();
    }

    private boolean horizontalRoadIsRed() {

        return trafficLights.getLightLeft().isRed()
                && trafficLights.getLightRight().isRed();
    }

    private void checkCarCollision(Pedestrian pedestrian) {

        float[] pedestrianPosition = {
            pedestrian.getX(),
            pedestrian.getY()
        };

        for (Car car : vehicleManager.getVehicles()) {

            float[] carPosition = car.getPosition();

            double distance = VehicleManager.getDistance(
                pedestrianPosition[0],
                pedestrianPosition[1],
                carPosition[0],
                carPosition[1]
            );

            double collisionDistance =
                pedestrian.getRadius() + car.getRadius();

            if (distance <= collisionDistance) {
                pedestrian.hit();
                return;
            }
        }
    }

    public void draw(Graphics g) {

        for (Pedestrian pedestrian : pedestrians) {
            pedestrian.draw(g);
        }
    }

    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }
}