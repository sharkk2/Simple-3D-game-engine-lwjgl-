package handlers;

import java.nio.DoubleBuffer;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.BufferUtils;

public class inputHandler {
    private static final float ACCELERATION = 0.02f;
    private static final float FRICTION = 0.90f;
    private static final float MAX_SPEED = 0.2f;
    private static final float MOUSE_SENSITIVITY = 0.1f;

    private float cameraX = 0, cameraY = 5, cameraZ = 0;
    private float cameraYaw = 0, cameraPitch = 0;
    private float velocityX = 0, velocityY = 0, velocityZ = 0;

    private boolean firstMouse = true;
    private double lastMouseX, lastMouseY;

    public void initMouse(long window) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xpos, ypos);
        lastMouseX = xpos.get();
        lastMouseY = ypos.get();
    }

    public void processInput(long window) {
        // Handle Mouse Look
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xpos, ypos);
    
        double mouseX = xpos.get();
        double mouseY = ypos.get();
    
        if (firstMouse) {
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            firstMouse = false;
        }
    
        float deltaX = (float) (mouseX - lastMouseX);
        float deltaY = (float) (lastMouseY - mouseY); 
    
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    
        cameraYaw += deltaX * MOUSE_SENSITIVITY;
        cameraPitch += deltaY * MOUSE_SENSITIVITY;
    
        if (cameraPitch > 89.0f) cameraPitch = 89.0f;
        if (cameraPitch < -89.0f) cameraPitch = -89.0f;
    
        float yawRad = (float) Math.toRadians(cameraYaw);
        float forwardX = (float) Math.cos(yawRad);
        float forwardZ = (float) Math.sin(yawRad);
        float rightX = (float) Math.cos(yawRad - Math.PI / 2);
        float rightZ = (float) Math.sin(yawRad - Math.PI / 2);
    
        float moveX = 0, moveY = 0, moveZ = 0;
    
        // Movement Input Handling
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            moveX += forwardX;
            moveZ += forwardZ;
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            moveX -= forwardX;
            moveZ -= forwardZ;
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            moveX -= rightX;
            moveZ -= rightZ;
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            moveX += rightX;
            moveZ += rightZ;
        }
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            moveY += 1;
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
            moveY -= 1;
        }
    
        // Normalize the movement vector to maintain consistent speed
        float magnitude = (float) Math.sqrt(moveX * moveX + moveY * moveY + moveZ * moveZ);
        if (magnitude > 0) {
            moveX /= magnitude;
            moveY /= magnitude;
            moveZ /= magnitude;
        }
    
        // Apply Acceleration
        velocityX += moveX * ACCELERATION;
        velocityY += moveY * ACCELERATION;
        velocityZ += moveZ * ACCELERATION;
    
        // Apply friction to slow down when no keys are pressed
        if (moveX == 0 && moveZ == 0 && moveY == 0) {
            velocityX *= FRICTION;
            velocityY *= FRICTION;
            velocityZ *= FRICTION;
        }
    
        // Apply max speed limit
        float speed = (float) Math.sqrt(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        if (speed > MAX_SPEED) {
            velocityX *= MAX_SPEED / speed;
            velocityY *= MAX_SPEED / speed;
            velocityZ *= MAX_SPEED / speed;
        }
    
        // Update the camera position with velocity
        cameraX += velocityX;
        cameraY += velocityY;
        cameraZ += velocityZ;
    }
    
    

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }

    public float getCameraZ() {
        return cameraZ;
    }

    public float getCameraYaw() {
        return cameraYaw;
    }

    public float getCameraPitch() {
        return cameraPitch;
    }


    public void setCameraPosition(float x, float y, float z) {
        this.cameraX = x;
        this.cameraY = y;
        this.cameraZ = z;
    }

    public void setCameraRotation(float yaw, float pitch) {
        this.cameraYaw = yaw;
        this.cameraPitch = pitch;
    }
}
