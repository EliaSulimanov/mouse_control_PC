import java.awt.*;
import java.awt.event.InputEvent;

public class Main extends Thread {
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    public static void main(String[] args) {
        System.out.println("Mouse control app");
        System.out.println("Elia Sulimanov - 2019");
        ServerThread serverThread = new ServerThread();
        serverThread.run();
    }

    /**
     * Move mouse to (x, y)
     *
     * @param x screen x
     * @param y screen y
     */
    static void moveMouse(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x * width, y * height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Left click
     */
    static void leftClick(boolean isDoubleClick) {
        try {

            Robot robot = new Robot();
            if (isDoubleClick) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Right click
     */
    static void rightClick() {
        try {

            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON3_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_MASK);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}