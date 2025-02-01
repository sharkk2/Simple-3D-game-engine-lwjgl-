package handlers;

public class Chunks {
    private cubeRender cube_render = new cubeRender();

    private static final int CHUNK_SIZE = 6;
    private static final int CHUNK_COUNT_X = 2;
    private static final int CHUNK_COUNT_Z = 3;

    public void renderChunks() {
        for (int chunkX = 0; chunkX < CHUNK_COUNT_X; chunkX++) {
            for (int chunkZ = 0; chunkZ < CHUNK_COUNT_Z; chunkZ++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    for (int y = 0; y < CHUNK_SIZE; y++) {
                        for (int z = 0; z < CHUNK_SIZE; z++) {
                            boolean exposed = (x == 0 || x == CHUNK_SIZE - 1 ||
                                               y == 0 || y == CHUNK_SIZE - 1 ||
                                               z == 0 || z == CHUNK_SIZE - 1);
                            
                            if (!exposed) continue; 
    
                            float worldX = chunkX * CHUNK_SIZE + x;
                            float worldY = y;
                            float worldZ = chunkZ * CHUNK_SIZE + z;
                            String texture = (y == CHUNK_SIZE - 1) ? "src/assets/grass.png" : "src/assets/sharkstone.png";
                            cube_render.renderCube(worldX, worldY, worldZ, texture);
                        }
                    }
                }
            }
        }
    }
}
