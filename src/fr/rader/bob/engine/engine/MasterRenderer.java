package fr.rader.bob.engine.engine;

import fr.rader.bob.engine.engine.shader.StaticShader;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glViewport;

public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Camera camera) {
        renderer.prepare();
        shader.start();

        if(Display.wasResized()) {
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            recalculateAspectRatio();
        }

        shader.loadViewMatrix(camera);
        renderer.render(entities);

        shader.stop();
        entities.clear();
    }

    public void recalculateAspectRatio() {
        renderer.recalculateAspectRatio();
        shader.loadProjectionMatrix(renderer.getProjectionMatrix());
    }

    public void processEntity(Entity entity) {
        TexturedModel model = entity.getTexturedModel();

        List<Entity> batch = entities.get(model);
        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(model, newBatch);
        }
    }

    public void cleanup() {
        shader.cleanup();
    }
}
