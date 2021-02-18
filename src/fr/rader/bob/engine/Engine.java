package fr.rader.bob.engine;

import fr.rader.bob.engine.engine.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class Engine {

    private static boolean isRunning = true;

    public static void startEngine(Canvas parent, int width, int height) {
        DisplayManager.createDisplay(parent, width, height);
        Loader loader = new Loader();

        Block block = new Block(3, 3, 3, 3, 0, 2);
        AtlasTexture texture = new AtlasTexture(loader.loadTexture("atlas"));

        TexturedModel texturedModel = new TexturedModel(texture, block.getRawModel(loader));
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
        Entity entity1 = new Entity(texturedModel, new Vector3f(1, 1, -5), 0, 0, 0, 1);
        Entity entity2 = new Entity(texturedModel, new Vector3f(0, 0, -6), 0, 0, 0, 1);
        Entity entity3 = new Entity(texturedModel, new Vector3f(1, 0, -6), 0, 0, 0, 1);

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while(isRunning && !Display.isCloseRequested()) {
            camera.move();

            renderer.processEntity(entity);
            renderer.processEntity(entity1);
            renderer.processEntity(entity2);
            renderer.processEntity(entity3);

            renderer.render(camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }

    public static void setRunning(boolean isRunning) {
        Engine.isRunning = isRunning;
    }
}
