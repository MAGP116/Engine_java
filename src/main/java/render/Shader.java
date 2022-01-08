package render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            String[] patterns = new String[splitString.length - 1];
            int index = 0;
            int eol = 0;
            for(int i = 1; i < splitString.length; i++){
                index = source.indexOf("#type", eol) + 6;
                eol = source.indexOf("\r\n", index);
                patterns[i - 1] = source.substring(index, eol).trim();

            }

            for (int i = 0; i < patterns.length; i++) {
                if (patterns[i].equals("vertex")) {
                    assert vertexSource == null : "ERROR: Two vertex shaders in file '" + filepath + "'";
                    vertexSource = splitString[i + 1];
                } else if (patterns[i].equals("fragment")) {
                    assert fragmentSource == null : "ERROR: Two fragment shaders in file '"+ filepath + "'";
                    fragmentSource = splitString[i + 1];
                } else {
                    throw new IOException("Unexpected token '" + patterns[i] + "' from '" +filepath + "'");
                }


            }


        } catch (IOException e){
            e.printStackTrace();
            assert false: "ERROR: Could not open shader file: '" + filepath + "'";
        }
    }

    public void compile(){
        //Compile and link Shaders
        int vertexID, fragmentID;

        //Creates a vertex shaders and returns its id
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //loads the shader source
        glShaderSource(vertexID,vertexSource);

        //Compiles the shader
        glCompileShader(vertexID);

        //Check for errors
        if(glGetShaderi(vertexID,GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("ERROR: vertex shader compilation failed in '" + filepath + "'");
            System.out.println(glGetShaderInfoLog(vertexID,
                    glGetShaderi(vertexID,GL_INFO_LOG_LENGTH)));
            assert false: "";
        }

        //Creates a fragment shaders and returns its id
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //loads the shader source
        glShaderSource(fragmentID,fragmentSource);

        //Compiles the shader
        glCompileShader(fragmentID);

        //Check for errors
        if(glGetShaderi(fragmentID,GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("ERROR: fragment shader compilation failed in '" + filepath + "'");
            System.out.println(glGetShaderInfoLog(fragmentID,
                    glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH)));
            assert false: "";
        }

        //Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        if(glGetProgrami(shaderProgramID,GL_LINK_STATUS) == GL_FALSE){
            System.out.println("ERROR: linking of shaders failed using '" + filepath + "'");
            System.out.println(glGetProgramInfoLog(shaderProgramID,
                    glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH)));
            assert false: "";
        }

    }

    public void use(){
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }
}
