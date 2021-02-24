package fr.rader.bob.engine.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;

    public Camera() {
    }

    public void move() {
        // todo: change hardcoded keybinds to use a map instead
        if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            position.x += Math.sin(Math.toRadians(yaw)) * .1f;
            position.z -= Math.cos(Math.toRadians(yaw)) * .1f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            position.x -= Math.cos(Math.toRadians(yaw)) * .1f;
            position.z -= Math.sin(Math.toRadians(yaw)) * .1f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.x -= Math.sin(Math.toRadians(yaw)) * .1f;
            position.z += Math.cos(Math.toRadians(yaw)) * .1f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += Math.cos(Math.toRadians(yaw)) * .1f;
            position.z += Math.sin(Math.toRadians(yaw)) * .1f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += 0.1f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= 0.1f;
        }

        if(Mouse.isButtonDown(1)) {
            yaw += Mouse.getDX() * 0.15f;
            if(yaw >= 360) yaw -= 360;
            if(yaw <= 0) yaw += 360;

            pitch += -Mouse.getDY() * 0.15f;
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
