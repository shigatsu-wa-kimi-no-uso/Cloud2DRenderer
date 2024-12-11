package me.project.cloud2drenderer.renderer.loader;

import static android.opengl.GLES32.*;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

import me.project.cloud2drenderer.opengl.glcomponent.buffer.GLVertexBuffer;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.entity.model.TerrainMesh;
import me.project.cloud2drenderer.renderer.entity.model.shape.Cube;
import me.project.cloud2drenderer.renderer.entity.model.shape.Rectangle;
import me.project.cloud2drenderer.renderer.entity.model.shape.Triangle;
import me.project.cloud2drenderer.renderer.controller.ModelController;
import me.project.cloud2drenderer.renderer.controller.ShaderController;
import me.project.cloud2drenderer.renderer.controller.TextureController;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.entity.texture.Texture;
import me.project.cloud2drenderer.util.AssetUtils;

public class AssetLoader {

    private Context context;

    private ShaderController shaderController;
    private TextureController textureController;

    private ModelController modelController;

    private String shaderDirectory = "shaders";

    private String textureDirectory = "textures";
    private final static String vertexShaderSuffix = ".vert";
    private final static String fragmentShaderSuffix = ".frag";

    Map<String, String> vertShaderCodes;
    Map<String, String> fragShaderCodes;
    Map<String, Bitmap> textureBitmaps;

    Map<String, MeshModel> presetModels = Map.of(
            "triangle",new Triangle(),
            "rectangle",new Rectangle(),
            "cube",new Cube(),
            "terrain_mesh",new TerrainMesh(100,100)
            );


    private void constructor(Context context,
                             ShaderController shaderController,
                             TextureController textureController,
                             ModelController modelController){
        this.context = context;
        this.shaderController = shaderController;
        this.textureController = textureController;
        this.modelController = modelController;
        vertShaderCodes = new HashMap<>();
        fragShaderCodes = new HashMap<>();
        textureBitmaps = new HashMap<>();
    }

    public AssetLoader(Context context,
                       ShaderController shaderController,
                       TextureController textureController,
                       ModelController modelController) {
        constructor(context,shaderController,textureController,modelController);
    }

    public AssetLoader(Context context) {
        shaderController = new ShaderController();
        textureController = new TextureController();
        modelController = new ModelController();
        constructor(context,shaderController,textureController,modelController);
    }

    private Shader loadShaderScript(String vertShaderFilename, String fragShaderFilename, String key) {
        //同名文件防止重复加载
        String vertCode = vertShaderCodes.compute(vertShaderFilename, (k, v) -> {
            if (v == null) {
                return AssetUtils.readAssetFile(context, vertShaderFilename);
            } else {
                return v;
            }
        });

        String fragCode = fragShaderCodes.compute(fragShaderFilename, (k, v) -> {
            if (v == null) {
                return AssetUtils.readAssetFile(context, fragShaderFilename); //从资产目录读取，因此无需设置目录，只需要指定名称
            } else {
                return v;
            }
        });
        shaderController.compileShader(key, vertCode, GL_VERTEX_SHADER);
        shaderController.compileShader(key, fragCode, GL_FRAGMENT_SHADER);
        return shaderController.createShaderProgram(key);
    }

    public Shader loadShaderScript(String key) {
        String vertFilename = shaderDirectory + "/" + key + vertexShaderSuffix;
        String fragFilename = shaderDirectory + "/" + key + fragmentShaderSuffix;
        return loadShaderScript(vertFilename, fragFilename, key);
    }



    public Texture loadTexture(String key) {
        String[] suffixes = {".jpg",".png",".jpeg",".bmp",".tga"};
        for(String suffix : suffixes){
            if(AssetUtils.isAssetExist(context,textureDirectory,key+suffix)){
                return loadTexture(textureDirectory + "/" + key + suffix,key);
            }
        }
        return null;
    }

    public Texture loadTexture(String filename, String key) {
        //同名文件防止重复加载
        Bitmap bitmap = textureBitmaps.compute(filename, (k, v) -> {
            if (v == null) {
                return AssetUtils.getBitmapFromAsset(context, filename);
            } else {
                return v;
            }
        });
        Texture texture = textureController.createTexture2D(key, bitmap);
        bitmap.recycle();
        return texture;
    }

    public void freeAllBitmaps(){
        for(Bitmap bitmap : textureBitmaps.values()){
            bitmap.recycle();
        }
        textureBitmaps.clear();
    }


    public Map<String,MeshModel> getPresetModels(){
        return presetModels;
    }

    public ModelController getModelController(){
        return modelController;
    }



    public LoadedModel loadModel(String key){
        MeshModel model = presetModels.get(key);
        assert model != null;
        return modelController.loadModel(model,key);
    }

    @Deprecated
    public GLVertexBuffer loadModel(String filename, String key){
        return null;
    }

    public String getShaderDirectory() {
        return shaderDirectory;
    }

    public void setShaderDirectory(String shaderDirectory) {
        this.shaderDirectory = shaderDirectory;
    }

    public String getTextureDirectory() {
        return textureDirectory;
    }

    public void setTextureDirectory(String textureDirectory) {
        this.textureDirectory = textureDirectory;
    }




}
