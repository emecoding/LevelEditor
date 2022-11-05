#version 330 core
layout(location = 0) in vec2 vertex;

uniform mat4 projection;
uniform mat4 transform;

void main()
{
    gl_Position = projection * transform * vec4(vertex.x, vertex.y, 1.0, 1.0);
}