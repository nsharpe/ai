package org.neil.display;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private MapPanel mapPanel = new MapPanel();

    public MainFrame() throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null); // Opens in the middle
        this.setVisible(true);
        this.add(mapPanel);
        this.pack();


    }
}
