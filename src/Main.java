import javax.swing.SwingUtilities;

import data.GymDatabase;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GymDatabase database = new GymDatabase();

            LoginFrame loginFrame = new LoginFrame(database);
            loginFrame.setVisible(true);
        });
    }
}
// Sytem.out.println("Hello, World!");

