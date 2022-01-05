package bob;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    //Singleton
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }
    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        MouseListener mouse = MouseListener.get();
        mouse.lastX = mouse.xPos;
        mouse.lastY = mouse.yPos;
        mouse.xPos = xPos;
        mouse.yPos = yPos;
        if(anyButtonIsDown()){
            mouse.isDragging = true;
        }
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        MouseListener mouse = MouseListener.get();
        if(button >= mouse.mouseButtonPressed.length)
            return;
        mouse.mouseButtonPressed[button] =  action == GLFW_PRESS;
        if(action == GLFW_RELEASE){
            mouse.isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        MouseListener mouse = MouseListener.get();
        mouse.scrollX = xOffset;
        mouse.scrollY = yOffset;

    }

    public static void endFrame(){
        MouseListener mouse = MouseListener.get();
        mouse.scrollX = 0.0;
        mouse.scrollY = 0.0;
        mouse.lastX = mouse.xPos;
        mouse.lastY = mouse.yPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getDx(){
        MouseListener mouse = MouseListener.get();
        return (float)(mouse.lastX - mouse.xPos);
    }

    public static float getDy(){
        MouseListener mouse = MouseListener.get();
        return (float)(mouse.lastY - mouse.yPos);
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }

    public static float getScrollY(){
        return (float)get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean buttonIsDown(int button){
        if(button >= get().mouseButtonPressed.length)
            return false;
        return get().mouseButtonPressed[button];
    }

    private static boolean anyButtonIsDown(){
        for (boolean button: get().mouseButtonPressed) {
            if(button)
                return true;
        }
        return false;
    }
}
