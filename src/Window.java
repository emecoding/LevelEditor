import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class Window
{
    private static int WIDTH, HEIGHT;
    private String CAPTION;
    private static long m_window;
    public static Matrix4f view_matrix;
    public static float cam_x_off_set;

    public Window(int width, int height, String caption)
    {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CAPTION = caption;
    }

    public void init()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Failed to init glfw....");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        m_window = glfwCreateWindow(this.WIDTH, this.HEIGHT, this.CAPTION, NULL, NULL);
        if(m_window == NULL)
            throw new RuntimeException("Failed to init window...");

        glfwSetKeyCallback(m_window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                glfwSetWindowShouldClose(m_window, true);
        });

        glfwMakeContextCurrent(this.m_window);
        glfwSwapInterval(1);
        glfwShowWindow(m_window);

        GL.createCapabilities();
    }

    public void clear()
    {
        glClearColor(0.509803922f, 0.690196078f, 0.870588235f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void update()
    {
        glfwSwapBuffers(this.m_window);
        glfwPollEvents();
    }

    public void destroy()
    {
        glfwFreeCallbacks(this.m_window);
        glfwDestroyWindow(this.m_window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean should_close() { return glfwWindowShouldClose(this.m_window); }

    public static boolean key_is_pressed(int key) { return glfwGetKey(m_window, key) == GLFW_PRESS; }

    public static boolean key_pressed_once(int key) { return glfwGetKey(m_window, key) == GLFW_RELEASE; }
    public static Matrix4f get_projection() { return new Matrix4f().ortho(0.0f, WIDTH, HEIGHT, 0.0f, -1.0f, 1.0f); }

    public static Vector2f get_mouse_position()
    {
        DoubleBuffer x_pos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y_pos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(m_window, x_pos, y_pos);

        return new Vector2f((float) x_pos.get(0), (float) y_pos.get(0));
    }
    public static boolean mouse_button_is_pressed(int key) { return glfwGetMouseButton(m_window, key) == GLFW_PRESS; }
    public static int get_width() { return WIDTH; }
    public static int get_height() { return HEIGHT; }

    public static boolean collides(float[] rect1, float[] rect2)//[x,y,w,h]
    {
        return (
                rect1[0] < rect2[0] + rect2[2] &&
                        rect1[0] + rect1[2] > rect2[0] &&
                        rect1[1] < rect2[1] + rect2[3] &&
                        rect1[3] + rect1[1] > rect2[1]
        );
    }

}