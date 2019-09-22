package ika.colororacle;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    /**
     * Entry point for the Color Oracle application.
     *
     * @param args The standard command line arguments, which are ignored.
     */
    public static void main(String[] args) {

        // don't run in headless mode
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Headless mode not supported by Color Oracle.");
            System.exit(-1);
            return;
        }

        // default Look and Feel on some systems is Metal, install the native
        // look and feel instead.
        String nativeLF = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(nativeLF);
        } catch (Exception ex) {
            Logger.getLogger(ColorOracle.class.getName()).log(Level.SEVERE, null, ex);
        }

        // set icon for JOptionPane dialogs, e.g. for error messages.
        setOptionPaneIcons("/icons/icon48x48.png");

        // make sure screenshots are allowed by the security manager
        try {
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                security.checkPermission(new AWTPermission("createRobot"));
                security.checkPermission(new AWTPermission("readDisplayPixels"));
            }
        } catch (SecurityException ex) {
            Logger.getLogger(ColorOracle.class.getName()).log(Level.SEVERE, null, ex);
            ColorOracle.showErrorMessage("Screenshots are not possible on "
                    + "your system.", true);
            System.exit(-1);
            return;
        }

        // test whether the system supports the SystemTray
        try {
            if (!SystemTray.isSupported()) {
                throw new UnsupportedOperationException("SystemTray not supported");
            }
        } catch (Exception ex) {
            ColorOracle.showErrorMessage("Access to the system tray or "
                            + "notification area \nis not supported on your system.",
                    true);
            Logger.getLogger(ColorOracle.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
            return;
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    new ColorOracle();
                } catch (Exception ex) {
                    Logger.getLogger(ColorOracle.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(-1);
                }
            }
        });
    }

    /**
     * Changes the icon displayed in JOptionPane dialogs to the passed icon.
     * Error, information, question and warning dialogs will show this icon.
     * This will also replace the icon in ProgressMonitor dialogs.
     */
    private static void setOptionPaneIcons(String iconPath) {
        LookAndFeel lf = UIManager.getLookAndFeel();
        if (lf != null) {
            Class iconBaseClass = lf.getClass();
            Object appIcon = LookAndFeel.makeIcon(iconBaseClass, iconPath);
            UIManager.put("OptionPane.errorIcon", appIcon);
            UIManager.put("OptionPane.informationIcon", appIcon);
            UIManager.put("OptionPane.questionIcon", appIcon);
            UIManager.put("OptionPane.warningIcon", appIcon);
        }
    }


}
