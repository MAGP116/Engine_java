package bob;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];

    public static KeyListener get(){
        if (KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if (key == GLFW_KEY_UNKNOWN){
            return;
        }
        KeyListener instance = get();
        instance.keyPressed[key] = action == GLFW_PRESS;
    }

    public static boolean isKeyPressed(int  keyCode){
        return get().keyPressed[keyCode];
    }
}
