#version 330 core
layout(location = 0) in vec4 vertex;

out vec2 texCoords;

uniform mat4 projection;
uniform mat4 transform;

void main()
{
    gl_Position = projection * transform * vec4(vertex.x, vertex.y, 1.0, 1.0);
    texCoords = vertex.zw;
}