#type vertex
#version 450
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTextureCords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTextureCords;

void main()
{
    fColor = aColor;
    fTextureCords = aTextureCords;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}


#type fragment
#version 450

in vec4 fColor;
in vec2 fTextureCords;
out vec4 color;

uniform float uTime;
uniform sampler2D TEXTURE_SAMPLER;

void main()
{
    color = texture(TEXTURE_SAMPLER, fTextureCords);
}