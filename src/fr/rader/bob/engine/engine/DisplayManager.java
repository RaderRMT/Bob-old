package fr.rader.bob.engine.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class DisplayManager {

    private static final int FPS_CAP = 60;

    public static void createDisplay(Canvas parent, int width, int height) {
        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create(new PixelFormat(), attribs);
            Display.setParent(parent);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        glViewport(0, 0, width, height);
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    public static void closeDisplay() {
        Display.destroy();
    }
}
