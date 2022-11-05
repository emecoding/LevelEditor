#version 330 core
layout(location = 0) in vec4 vertex;

out vec2 texCoords;

uniform mat4 projection;
uniform mat4 transform;
uniform mat4 view;
uniform int use_view;

void main()
{
    if(use_view == 1)
    {
        gl_Position = projection * view * transform * vec4(vertex.x, vertex.y, 1.0, 1.0);
    }
    else
    {
        gl_Position = projection * transform * vec4(vertex.x, vertex.y, 1.0, 1.0);
    }

    texCoords = vertex.zw;
}