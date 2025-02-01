import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import handlers.inputHandler;
import handlers.Render;
import handlers.Chunks;


public class Game {
    private long window;
    private float cameraX = 5.0f, cameraY = 6.0f, cameraZ = 8.0f; 
    private float cameraYaw = 0.0f, cameraPitch = 0.0f;
    private inputHandler input = new inputHandler();
    private Chunks chunks = new Chunks();
    private Render render = new Render();
    private int chunkDisplayList;

    public void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    
        this.window = glfwCreateWindow(800, 600, "Nigga cube", NULL, NULL);
        if (this.window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
    
        glfwMakeContextCurrent(this.window);
        glfwShowWindow(this.window);
        glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(70, 800.0f / 600.0f, 0.1f, 100.0f);
        glMatrixMode(GL_MODELVIEW);
    

        glfwSetFramebufferSizeCallback(window, (w, width, height) -> {
            glViewport(0, 0, width, height);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            gluPerspective(70, (float) width / (float) height, 0.1f, 100.0f);
            glMatrixMode(GL_MODELVIEW);
        });
    
        glEnable(GL_LIGHTING);  
        glEnable(GL_LIGHT0);   
        glEnable(GL_COLOR_MATERIAL); 
        
        float[] lightPosition = {0.0f, 10.0f, 0.0f, 1.0f}; 
        float[] lightColor = {1.0f, 1.0f, 1.0f, 1.0f};  
        
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, lightColor);  
        glLightfv(GL_LIGHT0, GL_SPECULAR, lightColor); 
    
        chunkDisplayList = glGenLists(1);
        glNewList(chunkDisplayList, GL_COMPILE);
        chunks.renderChunks();
        glEndList();
    
        input.setCameraPosition(cameraX, cameraY, cameraZ);
        input.setCameraRotation(cameraYaw, cameraPitch);
    }
    
    private void gluPerspective(float fov, float aspect, float zNear, float zFar) {
        float ymax = zNear * (float) Math.tan(Math.toRadians(fov / 2));
        float xmax = ymax * aspect;
        glFrustum(-xmax, xmax, -ymax, ymax, zNear, zFar);
    }

    private void processInput() {
        input.processInput(window);
        cameraX = input.getCameraX();
        cameraY = input.getCameraY();
        cameraZ = input.getCameraZ();
        cameraYaw = input.getCameraYaw();
        cameraPitch = input.getCameraPitch();
    }


    public void loop() {
        while (!glfwWindowShouldClose(this.window)) {
            processInput();
            render.render(chunkDisplayList, window, cameraX, cameraY, cameraZ, cameraPitch, cameraYaw);
            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }
    }

    public void cleanup() {
        glfwDestroyWindow(this.window);
        glfwTerminate();
    }

    public static void main(String[] args) {
        System.out.println("Initlizing...");
        System.out.println("OpenGL Version: " + GL_VERSION);
        Game game = new Game();
        game.init();
        game.loop();
        game.cleanup();
    }
}
