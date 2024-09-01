#type vertex
#version 450
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTextureCoords;
layout (location=3) in float aTextureID;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTextureCoords;
out float fTextureID;

void main()
{
    fColor = aColor;
    fTextureCoords = aTextureCoords;
    fTextureID = aTextureID;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}


#type fragment
#version 450

in vec4 fColor;
in vec2 fTextureCoords;
in float fTextureID;
out vec4 color;

uniform sampler2D uTextures[8];


void main()
{
    if (fTextureID > 0)
    {
        int id = int(fTextureID);
        color = fColor * texture(uTextures[id], fTextureCoords);
        //color = vec4(fTextureCoords, 0, 1);
    }
    else
    {
        color = fColor;
    }
}