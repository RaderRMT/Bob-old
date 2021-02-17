package fr.rader.bob.engine.engine;

public class RawModel {

    private int vertexCount;
    private int vaoID;

    public RawModel(int vertexCount, int vaoID) {
        this.vertexCount = vertexCount;
        this.vaoID = vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }
}
