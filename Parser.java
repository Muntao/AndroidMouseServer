package MouseServer;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Muntao
 */
public class Parser {

    int x1 = -1;
    int x2 = -1;
    int y1 = -1;
    int y2 = -1;

    Robot bot;
    int tap;

    public Parser() {
        try {
            bot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parse(String command) throws AWTException {
        if (command.contains(";")) {
            String[] com = command.split(";");

            int goX = Math.round(Float.parseFloat(com[0]));
            int goY = Math.round(Float.parseFloat(com[1]));
            int sensivity = Integer.parseInt(com[2]);

            sensivity = (sensivity + 10);

            tap = Integer.parseInt(com[3]);

            move(goX, goY, sensivity);
        }

        switch (command) {
            case "lpm":
                leftClick();
                break;
            case "ppm":
                rightClick();
                break;
            case "next":
                nextClick();
                break;
            case "prev":
                prevClick();
                break;
            case "start":
                startClick();
                break;
            case "stop":
                stopClick();
                break;
        }
    }

    public void stopClick() throws AWTException {
        bot.keyPress(KeyEvent.VK_ESCAPE);
        bot.keyRelease(KeyEvent.VK_ESCAPE);
        System.out.println("Wciśnięto ESC");
    }
    
    public void prevClick() throws AWTException {
        bot.keyPress(KeyEvent.VK_LEFT);
        bot.keyRelease(KeyEvent.VK_LEFT);
        System.out.println("Wciśnięto strzalke w lewo");
    }
    
    public void nextClick() throws AWTException {
        bot.keyPress(KeyEvent.VK_RIGHT);
        bot.keyRelease(KeyEvent.VK_RIGHT);
        System.out.println("Wciśnięto strzalke w prawo");
    }
    
    public void startClick() throws AWTException {        
        bot.keyPress(KeyEvent.VK_F5);
        bot.keyRelease(KeyEvent.VK_F5);        
        System.out.println("Wciśnięto F5");
    }

    public void leftClick() throws AWTException {
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
        System.out.println("Wciśnięto lpm");
    }

    public void rightClick() throws AWTException {
        bot.mousePress(InputEvent.BUTTON3_MASK);
        bot.mouseRelease(InputEvent.BUTTON3_MASK);
        System.out.println("Wciśnięto ppm");
    }

    public void move(int x, int y, int sensivity) throws AWTException {
        if ((x1 == -1) && (y1 == -1)) {
            x1 = x;
            y1 = y;
            return;
        }
        if ((x2 == -1) && (y2 == -1)) {
            x2 = x;
            y2 = y;

            int dx = x2 - x1;
            int dy = y2 - y1;

            if ((tap == 1) && (dx == 0) && (dy == 0)) {
                leftClick();
                reset();
                return;
            }

            if (dx < -80 || dy < -80 || dx > 100 || dy > 80) {
                reset();
                return;
            }

            mouseGlide(dx * sensivity / 10, dy * sensivity / 10, 25, 5);
            reset();
        }

    }

    public void mouseGlide(int x1, int y1, int t, int n) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        try {
            double dx = (x1) / ((double) n);
            double dy = (y1) / ((double) n);
            double dt = t / ((double) n);
            for (int step = 1; step <= n; step++) {
                Thread.sleep((int) dt);
                bot.mouseMove((int) (p.getX() + dx * step), (int) (p.getY() + dy * step));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        x1 = -1;
        y1 = -1;
        x2 = -1;
        y2 = -1;
    }
}
