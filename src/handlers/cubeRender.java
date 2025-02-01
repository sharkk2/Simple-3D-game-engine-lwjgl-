package handlers;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class cubeRender {
    public int loadTexture(String texturePath) {
        int textureID = 0;

        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(texturePath, width, height, channels, 0);
            if (image == null) {
                System.err.println("Failed to load texture file!");
                return -1;
            }

            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return textureID;
    }

    public void renderCube(float x, float y, float z, String texturePath) {
        int textureID = loadTexture(texturePath);

        if (textureID == -1) {
            return;  
        }

        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, textureID);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);

        float[] lightPosition = {0.0f, 50.0f, 0.0f, 1.0f};
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);
        
        glLightfv(GL_LIGHT0, GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 1.0f}); 
        glLightfv(GL_LIGHT0, GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1.0f}); 
        glLightfv(GL_LIGHT0, GL_SPECULAR, new float[]{1.0f, 1.0f, 1.0f, 1.0f}); 

        float[] materialAmbient = {0.1f, 0.1f, 0.1f, 1.0f}; 
        float[] materialDiffuse = {0.8f, 0.8f, 0.8f, 1.0f}; 
        float[] materialSpecular = {1.0f, 1.0f, 1.0f, 1.0f}; 
        float shininess = 32.0f; 

        glMaterialfv(GL_FRONT, GL_AMBIENT, materialAmbient);
        glMaterialfv(GL_FRONT, GL_DIFFUSE, materialDiffuse);
        glMaterialfv(GL_FRONT, GL_SPECULAR, materialSpecular);
        glMaterialf(GL_FRONT, GL_SHININESS, shininess);

        glBegin(GL_QUADS);

        glNormal3f(0.0f, 0.0f, 1.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(x - 0.5f, y - 0.5f, z + 0.5f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(x + 0.5f, y - 0.5f, z + 0.5f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(x + 0.5f, y + 0.5f, z + 0.5f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(x - 0.5f, y + 0.5f, z + 0.5f);

        // Back face
        glNormal3f(0.0f, 0.0f, -1.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(x - 0.5f, y - 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(x - 0.5f, y + 0.5f, z - 0.5f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(x + 0.5f, y + 0.5f, z - 0.5f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(x + 0.5f, y - 0.5f, z - 0.5f);

        // Left face
        glNormal3f(-1.0f, 0.0f, 0.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(x - 0.5f, y - 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(x - 0.5f, y - 0.5f, z + 0.5f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(x - 0.5f, y + 0.5f, z + 0.5f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(x - 0.5f, y + 0.5f, z - 0.5f);

        // Right face
        glNormal3f(1.0f, 0.0f, 0.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(x + 0.5f, y - 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(x + 0.5f, y - 0.5f, z + 0.5f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(x + 0.5f, y + 0.5f, z + 0.5f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(x + 0.5f, y + 0.5f, z - 0.5f);

        // Top face
        glNormal3f(0.0f, 1.0f, 0.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(x - 0.5f, y + 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(x + 0.5f, y + 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(x + 0.5f, y + 0.5f, z + 0.5f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(x - 0.5f, y + 0.5f, z + 0.5f);

        // Bottom face
        glNormal3f(0.0f, -1.0f, 0.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(x - 0.5f, y - 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(x + 0.5f, y - 0.5f, z - 0.5f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(x + 0.5f, y - 0.5f, z + 0.5f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(x - 0.5f, y - 0.5f, z + 0.5f);

        glEnd();

        glDisable(GL_LIGHTING);  
        glDisable(GL_TEXTURE_2D);
    }
}