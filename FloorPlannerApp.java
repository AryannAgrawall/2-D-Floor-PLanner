import java.awt.*;
import javax.swing.*;
//this is the main code
public class FloorPlannerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Floor Planner");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            RoomCanvas roomCanvas = new RoomCanvas();
            ControlPanel controlPanel = new ControlPanel(roomCanvas);  // Pass the RoomCanvas instance

            frame.add(controlPanel, BorderLayout.WEST);
            frame.add(roomCanvas, BorderLayout.CENTER);

            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
