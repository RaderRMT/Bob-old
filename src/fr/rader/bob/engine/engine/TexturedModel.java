package fr.rader.bob.engine.engine;

import fr.rader.bob.engine.AtlasTexture;

public class TexturedModel {

    private AtlasTexture texture;
    private RawModel rawModel;

    public TexturedModel(AtlasTexture texture, RawModel rawModel) {
        this.texture = texture;
        this.rawModel = rawModel;
    }

    public AtlasTexture getTexture() {
        return texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }
}
