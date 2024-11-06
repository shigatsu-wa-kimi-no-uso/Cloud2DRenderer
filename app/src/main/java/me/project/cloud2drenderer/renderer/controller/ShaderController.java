package me.project.cloud2drenderer.renderer.controller;


import static android.opengl.GLES32.*;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.ShaderUniformMeta;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.VertexAttributeMeta;
import me.project.cloud2drenderer.util.DebugUtils;

public class ShaderController {

    static class ShaderContainer{
        Map<String, String> shaderCodes;
        Map<String, Integer> compiledShaderIds;

        ShaderContainer(){
            shaderCodes = new HashMap<>();
            compiledShaderIds = new HashMap<>();
        }
    }
    private final ShaderContainer vertContainer;
    private final ShaderContainer fragContainer;

    private final Map<String, Shader> shaders;


    public ShaderController(){
        vertContainer = new ShaderContainer();
        fragContainer = new ShaderContainer();
        shaders = new HashMap<>();
    }

    //不要使用ShaderController读取代码
    @Deprecated
    public void loadShader(String name,String code,int type){
        ShaderContainer container = null;
        if(type == GL_VERTEX_SHADER){
            container = vertContainer;
        }else if(type == GL_FRAGMENT_SHADER){
            container = fragContainer;
        }
        assert container != null;
        container.compiledShaderIds.compute(name,
                (k, v) -> Objects.requireNonNullElseGet(v,
                        () -> GLShaderManager.compileShader(code,type)
                )
        );
    }

    public void compileShader(String name,String code,int type) {
        ShaderContainer container;
        if (type == GL_VERTEX_SHADER) {
            container = vertContainer;
        } else if (type == GL_FRAGMENT_SHADER) {
            container = fragContainer;
        } else {
            container = null;
        }
        assert container != null;
        container.compiledShaderIds.compute(name,
                (k, v) -> Objects.requireNonNullElseGet(v,
                        () -> GLShaderManager.compileShader(code, type)
                )
        );
    }

    @Deprecated
    public void compileShader(String name,int type) {
        ShaderContainer container;
        if (type == GL_VERTEX_SHADER) {
            container = vertContainer;
        } else if (type == GL_FRAGMENT_SHADER) {
            container = fragContainer;
        } else {
            container = null;
        }
        assert container != null;
        container.compiledShaderIds.compute(name, (k, v) ->
                Objects.requireNonNullElseGet(v, () -> {
                            String code = container.shaderCodes.get(name);
                            return GLShaderManager.compileShader(code, type);
                        }
                )
        );
    }

    public Shader createShaderProgram(String name) {
        Integer vertId = vertContainer.compiledShaderIds.get(name);
        Integer fragId = fragContainer.compiledShaderIds.get(name);
        assert vertId != null && fragId != null;
        return shaders.compute(name,
                (k, v) -> Objects.requireNonNullElseGet(v, () -> {
                            Shader shader = new Shader();
                            shader.program = GLShaderManager.createProgram(vertId, fragId);
                            shader.setUniformMetas(getUniformInfo(shader));
                           // shader.uniformSetters = generateShaderUniformBinders();
                            return shader;
                        }
                )
        );
    }


    public void bindShaderAttributePointers(Shader shader, @NonNull LoadedModel model){
        Map<String, VertexAttributeMeta> metaMap = model.modelMetaGetter.getModelMeta().attributeMetas;
        GLShaderManager.use(shader.program);
        GLVertexBufferManager.bind(model.vertexBuffer);
        for(Map.Entry<String, VertexAttributeMeta> entry : metaMap.entrySet()){
            VertexAttributeMeta attribMeta = entry.getValue();
            String attributeName = attribMeta.attributeName;
            GLShaderManager.setAttribute(attributeName,attribMeta.elemCnt,attribMeta.elemType,attribMeta.normalized,attribMeta.strideInBytes,attribMeta.offset);
        }
    }


    private Map<String,ShaderUniformMeta> getUniformInfo(@NonNull Shader shader){
        String[][] names = new String[1][];
        int[][] types = new int[1][];
        int[][] sizes = new int[1][];
        GLShaderManager.use(shader.program);
        GLShaderManager.getShaderUniformInfo(names,types,sizes);
        int count = names[0].length;
        Map<String,ShaderUniformMeta> metas = new HashMap<>();
        for(int i = 0; i < count; i++){
            // Get the name of the uniform block
            ShaderUniformMeta meta = new ShaderUniformMeta();
            meta.uniformName = names[0][i];
            meta.elemCnt = sizes[0][i];
            meta.type = types[0][i];
            meta.location = new int[meta.elemCnt];
            String keyName;
            if(meta.elemCnt>1){
                String arrayMarker = "[0]";
                int len = arrayMarker.length();
                String arrayName = meta.uniformName.substring(0,meta.uniformName.length() - len);
                keyName = arrayName;
                for(int j =0;j<meta.elemCnt;j++){
                    String elemName = arrayName + "[" + j+ "]";
                    meta.location[j] = GLShaderManager.getUniformLocation(elemName);
                }
            }else {
                meta.location[0] = GLShaderManager.getUniformLocation(meta.uniformName);
                keyName = meta.uniformName;
            }
            meta.intBased = GLShaderManager.typeIsIntBased(meta.type);
            metas.put(keyName,meta);
        }
        return metas;
    }


    public void useShaderProgram(@NonNull Shader shader){
        GLShaderManager.use(shader.program);
    }

    public Shader getShaderProgram(String name){
        return DebugUtils.checkNotNull(shaders.get(name));
    }



}
