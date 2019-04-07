
import java.awt.Robot;
import java.awt.event.InputEvent;

public class Main extends Thread {
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
    public static void moveMouse(int x, int y) {
        try {
            // TODO make it relative to screen size
            Robot robot = new Robot();
            robot.mouseMove(x, y);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Left click
     */
    public static void leftClick(boolean isDoubleClick) {
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
    public static void rightClick() {
        try {

            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON3_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_MASK);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}