package org.neil;

import org.neil.display.MainFrame;


public class Main {

    public static void main(String[] args) {
        try {
            new MainFrame();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
