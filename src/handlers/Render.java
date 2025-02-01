package handlers;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Render {
     private float[][] cubePositions = new float[20][3];

     private long lastTime = System.nanoTime();
     private int fps = 0;
     private cubeRender cube_render = new cubeRender();
    

     public boolean isBlock(int x, int y, int z) {
        for (int i = 0; i < 20; i++) {
            if (cubePositions[i][0] == x && cubePositions[i][1] == y && cubePositions[i][2] == z) {
                return true;
            }
        }
        return false;

     }   


     private void renderCrosshair(long window) {
        // Disable depth testing to render the crosshair on top
        glDisable(GL_DEPTH_TEST); 
    
        // Set up 2D orthogonal projection
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(window, width, height);  
        glOrtho(0, width[0], height[0], 0, -1, 1); // Ortho projection for 2D rendering
        
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
    
        // Set the crosshair style
        glLineWidth(2.0f);
        glColor3f(1.0f, 1.0f, 1.0f); 
    
        // Render the crosshair
        glBegin(GL_LINES);
        glVertex2f(width[0] / 2 - 10, height[0] / 2); 
        glVertex2f(width[0] / 2 + 10, height[0] / 2); 
        glVertex2f(width[0] / 2, height[0] / 2 - 10); 
        glVertex2f(width[0] / 2, height[0] / 2 + 10); 
        glEnd();
    
        // Reset transformations and projection
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    
        // Re-enable depth testing after the crosshair
        glEnable(GL_DEPTH_TEST);
    }
    
    
    private void updateWindowTitle(long window, float cameraX, float cameraY, float cameraZ) {
        String title = "Nigga cube | FPS: " + getFPS() + " | X: " + cameraX + " Y: " + cameraY + " Z: " + cameraZ;
        glfwSetWindowTitle(window, title);
    }

    private String getFPS() {
        long currentTime = System.nanoTime();
        float elapsedTime = (currentTime - lastTime) / 1000000000.0f; 
        lastTime = currentTime;
        fps = (int) (1.0f / elapsedTime);  

        return String.valueOf(fps);
    }

    public void render(int chunkDisplayList, long window, float cameraX, float cameraY, float cameraZ, float cameraPitch, float cameraYaw) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glLoadIdentity();
        glRotatef(-cameraPitch, 1.0f, 0.0f, 0.0f);
        glRotatef(-cameraYaw, 0.0f, 1.0f, 0.0f);

       
        glTranslatef(-cameraX, -cameraY, -cameraZ);

        glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
        for (int i = 0; i < 20; i++) {
            float x = cubePositions[i][0];
            float y = cubePositions[i][1];
            float z = cubePositions[i][2];
            
            cube_render.renderCube(x, y, z, "src/assets/sharkstone.png");
        }
        glCallList(chunkDisplayList);
        renderCrosshair(window);
        updateWindowTitle(window, cameraX, cameraY, cameraZ);
    }
}
