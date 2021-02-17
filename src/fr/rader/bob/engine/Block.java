package fr.rader.bob.engine;

import fr.rader.bob.engine.engine.Loader;
import fr.rader.bob.engine.engine.RawModel;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private final List<Float> textureCoords = new ArrayList<>();

    public Block(int back, int front, int right, int left, int top, int bottom) {
        addTextureCoords(getTexturePosition(back));
        addTextureCoords(getTexturePosition(front));
        addTextureCoords(getTexturePosition(right));
        addTextureCoords(getTexturePosition(left));
        addTextureCoords(getTexturePosition(top));
        addTextureCoords(getTexturePosition(bottom));
    }

    private float[] getTexturePosition(int textureId) {
        float[] out = new float[8];
        float textureLength = 1 / 16f;
        float textureX = (textureId % 16) * textureLength;
        float textureY = (textureId / 16) * textureLength;

        out[0] = textureX + textureLength;
        out[1] = textureY;

        out[2] = textureX + textureLength;
        out[3] = textureY + textureLength;

        out[4] = textureX;
        out[5] = textureY + textureLength;

        out[6] = textureX;
        out[7] = textureY;

        return out;
    }

    private void addTextureCoords(float[] positions) {
        for(float position : positions) {
            textureCoords.add(position);
        }
    }

    public Model getModel() {
        return Models.CUBE_MODEL;
    }

    public float[] getTextureCoords() {
        float[] out = new float[textureCoords.size()];

        int i = 0;
        for(Float value : textureCoords) {
            out[i++] = value;
        }

        return out;
    }

    public RawModel getRawModel(Loader loader) {
        return loader.loadToVAO(getModel().getVertices(), getTextureCoords(), getModel().getIndices());
    }
}
