#version 330 core

in vec2 texCoords;

uniform sampler2D m_texture;

void main()
{
    gl_FragColor = texture(m_texture, texCoords);
}