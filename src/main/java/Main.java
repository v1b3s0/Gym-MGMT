import javax.swing.SwingUtilities;

import data.GymDatabase;
import ui.AppStyle;
import ui.LoginFrame;

/**
 * Application entry point for the Gym Management System.
 * Applies the global theme, loads saved data, then shows the login screen.
 */
public class Main {
    public static void main(String[] args) {
        // All Swing UI must be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            AppStyle.apply();                          // ui.AppStyle: look-and-feel, fonts, colours
            GymDatabase database = new GymDatabase();  // data.GymDatabase constructor -> FileManager.load* reads the CSV files

            // ui.LoginFrame: the login screen; on a valid login it opens DashboardFrame
            LoginFrame loginFrame = new LoginFrame(database);
            loginFrame.setVisible(true);
        });
    }
}
