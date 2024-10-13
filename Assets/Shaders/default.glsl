#type vertex
#version 410

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTextureCoords;
layout (location=3) in float aTextureID;
layout (location=5) in float aEntityShow;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTextureCoords;
out float fTextureID;
out float fEntityShow;

void main()
{
    fColor = aColor;
    fTextureCoords = aTextureCoords;
    fTextureID = aTextureID;
    fEntityShow = aEntityShow;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}


#type fragment
#version 410

in vec4 fColor;
in vec2 fTextureCoords;
in float fTextureID;
in float fEntityShow;

out vec4 color;

uniform sampler2D uTextures[8];

void main()
{
    if (fEntityShow <= 0.0)
    {
        discard;
    }

    if (fTextureID > 0.0)
    {
        int id = int(fTextureID);
        color = fColor * texture(uTextures[id], fTextureCoords);
    }
    else
    {
        color = fColor;
    }
}