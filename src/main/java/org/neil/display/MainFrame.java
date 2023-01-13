package org.neil.display;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private MapPanel mapPanel = new MapPanel();

    public MainFrame() throws HeadlessException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new JScrollPane(mapPanel));
        this.setSize(500, 500);
        this.setLocationRelativeTo(null); // Opens in the middle
        this.setVisible(true);
        this.pack();

        mapPanel.addFrameListener(() -> this);

    }
}
