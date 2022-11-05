import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Grid
{
    private final int GRID_WIDTH = 32;
    private final int GRID_HEIGHT = 32;

    private Shader m_shader;

    private int VAO;

    public Grid() throws IOException
    {
        this.m_shader = new Shader("res/grid.vert", "res/grid.frag");

        /*float vertices[] = new float[]{
                // pos      // tex
                0.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };*/
        float vertices[] = new float[]
                {
                        0.0f, 0.0f,
                        0.0f, 1.0f,

                        0.0f, 1.0f,
                        1.0f, 1.0f,

                        1.0f, 1.0f,
                        1.0f, 0.0f,

                        1.0f, 0.0f,
                        0.0f, 0.0f

                };

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(6 * 4);
        vertexData.put(vertices);
        vertexData.flip();

        VAO = glGenVertexArrays();
        int VBO = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

        glBindVertexArray(this.VAO);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        Shader.unbind_every_shader();
        this.m_shader.use();
        this.m_shader.upload_mat4("projection", Window.get_projection());
        Shader.unbind_every_shader();
    }

    public void render()
    {
        Shader.unbind_every_shader();
        this.m_shader.use();
        for(int x = 0; x < Window.get_width(); x++)
        {
            for(int y = 0; y < Window.get_height(); y++)
            {
                Matrix4f transform = new Matrix4f();
                transform.translate(new Vector3f(x*GRID_WIDTH, y*GRID_HEIGHT, 0.0f));
                transform.scale(new Vector3f(GRID_WIDTH, GRID_HEIGHT, 1.0f));
                this.m_shader.upload_mat4("transform", transform);
                glBindVertexArray(VAO);
                glDrawArrays(GL_LINES, 0, 8);
            }
        }

        glBindVertexArray(0);
        Shader.unbind_every_shader();
    }
}
