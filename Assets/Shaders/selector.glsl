#type vertex
#version 410

    layout (location=0) in vec3 aPos;
    layout (location=1) in vec4 aColor;
    layout (location=2) in vec2 aTextureCoords;
    layout (location=3) in float aTextureID;
    layout (location=4) in float aEntityID;

    uniform mat4 uProjection;
    uniform mat4 uView;

    out vec4 fColor;
    out vec2 fTextureCoords;
    out float fTextureID;
    out float fEntityID;

    void main()
    {
        fColor = aColor;
        fTextureCoords = aTextureCoords;
        fTextureID = aTextureID;
        gl_Position = uProjection * uView * vec4(aPos, 1.0);
        fEntityID = aEntityID;
    }


#type fragment
#version 410

    in vec4 fColor;
    in vec2 fTextureCoords;
    in float fTextureID;
    in float fEntityID;

    out vec3 color;

    uniform sampler2D uTextures[8];


    void main()
    {
        vec4 texColor = vec4(1, 1, 1, 1);
        if (fTextureID > 0)
        {
           int id = int(fTextureID);
           texColor = fColor * texture(uTextures[id], fTextureCoords);
        }
        if (texColor.a < 0.5)
        {
            discard;
        }
        color = vec3(fEntityID, fEntityID, fEntityID);
    }