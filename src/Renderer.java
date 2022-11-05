import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {
    private int VAO;
    private Shader m_shader;

    public Renderer() throws IOException
    {
        this.m_shader = new Shader("res/sprite.vert", "res/sprite.frag");
        this.initialize_rendering();
    }


    private void initialize_rendering() {
        float vertices[] = new float[]{
                // pos      // tex
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
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
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        m_shader.use();
        m_shader.upload_mat4("projection", Window.get_projection());
        m_shader.unbind_every_shader();

    }

    public void render_sprite(Vector2f position, Vector2f size)
    {
        Shader.unbind_every_shader();
        Matrix4f transform = new Matrix4f();
        transform.translate(new Vector3f(position.x, position.y, 0.0f));
        transform.scale(new Vector3f(size.x, size.y, 1.0f));
        m_shader.use();
        m_shader.upload_mat4("transform", transform);
        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
        Shader.unbind_every_shader();
    }
}
