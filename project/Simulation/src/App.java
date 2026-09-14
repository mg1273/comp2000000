import java.awt.*;
import javax.swing.*;

public class App {

    static float worldTimeElapsed = 0;
    static boolean paused = false;

    static LightManager trafficLights;
    static private VehicleManager vehicleManager;
    static private PedestrianManager pedestrianManager;

    public static void main(String[] args) {

        int width = 800;
        int height = 800;

        trafficLights = new LightManager();

        vehicleManager = new VehicleManager(trafficLights);

        pedestrianManager = new PedestrianManager(trafficLights, vehicleManager);

        JFrame frame = new JFrame("Traffic Sim");

        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                g.setColor(Color.GRAY);
                g.fillRect(0, 300, width, 200);
                g.fillRect(300, 0, 200, height);

                g.setColor(Color.BLACK);
                g.drawLine(0, height / 2, width, height / 2);
                g.drawLine(width / 2, 0, width / 2, height);

                trafficLights.draw(g);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("Time elapsed: " + worldTimeElapsed / 1000 + "s", 20, 40);

                vehicleManager.draw(g);
                pedestrianManager.draw(g);
            }
        };

        pauseButtonSetup(frame, panel);

        long[] lastTime = {System.nanoTime()};
        long startTime = System.currentTimeMillis();

        Timer timer = new Timer(16, e -> {

            long currentTime = System.nanoTime();

            double deltaTime = (currentTime - lastTime[0]) / 1_000_000_000.0;

            lastTime[0] = currentTime;

            worldTimeElapsed = (int) (System.currentTimeMillis() - startTime);

            if (paused) {
                return;
            }

            vehicleManager.update(deltaTime, worldTimeElapsed);
            trafficLights.update();
            pedestrianManager.update(deltaTime, worldTimeElapsed);

            panel.repaint();
        });

        timer.start();

        frame.add(panel);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void pauseButtonSetup(JFrame frame, JPanel panel) {

        frame.add(panel);

        panel.setLayout(new GridBagLayout());

        JButton pauseButton = new JButton("Pause");

        pauseButton.addActionListener(e -> {

            paused = !paused;

            pauseButton.setText(paused ? "Resume" : "Pause");

            panel.repaint();
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(pauseButton, gbc);
    }
}