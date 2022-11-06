#version 330 core

in vec2 texCoords;

uniform sampler2D m_texture;
uniform float ALPHA;

void main()
{
    gl_FragColor = texture(m_texture, texCoords) * vec4(1.0, 1.0, 1.0, ALPHA);
}