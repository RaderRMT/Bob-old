package fr.rader.bob.engine.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;

    private final float MOVEMENT_SPEED = 0.1f;
    private final float MOUSE_SENSITIVITY = 0.15f;

    public Camera() {
    }

    public void move() {
        // todo: change hardcoded keybinds to use a map instead
        if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            position.x += Math.sin(Math.toRadians(yaw)) * MOVEMENT_SPEED;
            position.z -= Math.cos(Math.toRadians(yaw)) * MOVEMENT_SPEED;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            position.x -= Math.cos(Math.toRadians(yaw)) * MOVEMENT_SPEED;
            position.z -= Math.sin(Math.toRadians(yaw)) * MOVEMENT_SPEED;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.x -= Math.sin(Math.toRadians(yaw)) * MOVEMENT_SPEED;
            position.z += Math.cos(Math.toRadians(yaw)) * MOVEMENT_SPEED;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += Math.cos(Math.toRadians(yaw)) * MOVEMENT_SPEED;
            position.z += Math.sin(Math.toRadians(yaw)) * MOVEMENT_SPEED;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += MOVEMENT_SPEED;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= MOVEMENT_SPEED;
        }

        // show wireframe
        if(Keyboard.isKeyDown(Keyboard.KEY_U)) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        // hide wireframe
        if(Keyboard.isKeyDown(Keyboard.KEY_I)) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }

        if(Mouse.isButtonDown(1)) {
            yaw += Mouse.getDX() * MOUSE_SENSITIVITY;
            if(yaw >= 360) yaw -= 360;
            if(yaw <= 0) yaw += 360;

            pitch += -Mouse.getDY() * MOUSE_SENSITIVITY;
            if(pitch >= 90) pitch = 90;
            if(pitch <= -90) pitch = -90;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
