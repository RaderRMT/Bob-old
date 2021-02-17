package fr.rader.bob.engine;

public class CubeModel implements Model {

    private final float[] vertices = {
            // back
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,

            // front
            0.5f,  0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f,  0.5f, 0.5f,

            // right
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,

            // left
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,

            // top
            -0.5f, 0.5f,  0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f,  0.5f,

            // bottom
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f
    };

    private final int[] indices = {
            // back
            0, 3, 1,
            3, 2, 1,

            // front
            4, 7, 5,
            7, 6, 5,

            // right
            8, 11, 9,
            11, 10, 9,

            // left
            12, 15, 13,
            15, 14, 13,

            // top
            16, 19, 17,
            19, 18, 17,

            // bottom
            20, 23, 21,
            23, 22, 21
    };

    public CubeModel() {
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    @Override
    public int[] getIndices() {
        return indices;
    }
}
