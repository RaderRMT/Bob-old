package fr.rader.bob.engine;

import fr.rader.bob.engine.engine.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;

public class Engine {

    private static boolean isRunning = true;

    private static AtlasTexture atlasTexture;

    public static void startEngine(Canvas parent, int width, int height) {
        DisplayManager.createDisplay(parent, width, height);
        Loader loader = new Loader();

        atlasTexture = new AtlasTexture(loader.loadTexture("atlas"));

        Block grass = new Block(3, 3, 3, 3, 0, 2);
        Block furnace = new Block(45, 44, 45, 45, 62, 62);
        Block dirt = new Block(2, 2, 2, 2, 2, 2);

        Entity entity = new Entity(grass.getTexturedModel(loader), new Vector3f(0, 0, -5), 0, 0, 0, 1);
        Entity entity1 = new Entity(grass.getTexturedModel(loader), new Vector3f(1, 0, -5), 0, 0, 0, 1);
        Entity entity2 = new Entity(grass.getTexturedModel(loader), new Vector3f(0, 0, -6), 0, 0, 0, 1);
        Entity entity3 = new Entity(dirt.getTexturedModel(loader), new Vector3f(1, 0, -6), 0, 0, 0, 1);
        Entity entity4 = new Entity(furnace.getTexturedModel(loader), new Vector3f(1, 1, -6), 0, 0, 0, 1);

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();
        while(isRunning && !Display.isCloseRequested()) {
            camera.move();

            renderer.processEntity(entity);
            renderer.processEntity(entity1);
            renderer.processEntity(entity2);
            renderer.processEntity(entity3);
            renderer.processEntity(entity4);

            renderer.render(camera);

            DisplayManager.updateDisplay();
        }

        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }

    public static AtlasTexture getAtlasTexture() {
        return atlasTexture;
    }

    public static void setRunning(boolean isRunning) {
        Engine.isRunning = isRunning;
    }
}
