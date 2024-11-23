package me.project.cloud2drenderer;

import static android.opengl.GLES32.*;

import android.content.Context;
import android.opengl.GLSurfaceView;

import androidx.annotation.NonNull;

import java.util.Map;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.project.cloud2drenderer.opengl.GLErrorUtils;
import me.project.cloud2drenderer.opengl.glcomponent.buffer.GLVertexBuffer;
import me.project.cloud2drenderer.opengl.glcomponent.shader.GLShaderProgram;
import me.project.cloud2drenderer.opengl.statemanager.GLCanvasManager;
import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.opengl.statemanager.GLVertexBufferManager;
import me.project.cloud2drenderer.opengl.glcomponent.texture.GLTexture;
import me.project.cloud2drenderer.renderer.entity.model.LoadedModel;
import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.entity.model.ModelMeta;
import me.project.cloud2drenderer.renderer.entity.model.shape.Cube;
import me.project.cloud2drenderer.renderer.controller.CanvasController;
import me.project.cloud2drenderer.renderer.controller.ModelController;
import me.project.cloud2drenderer.renderer.controller.ShaderController;
import me.project.cloud2drenderer.renderer.controller.TextureController;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.loader.AssetLoader;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.VertexAttributeMeta;
import me.project.cloud2drenderer.util.AssetUtils;


public class GLRendererTest implements GLSurfaceView.Renderer{

    Context context;
    public GLRendererTest(Context context){
        this.context = context;
        assetLoader = new AssetLoader(
                context,
                shaderController,
                textureController,
                modelController);
    }

    private final CanvasController canvasController= new CanvasController();

    private final ShaderController shaderController = new ShaderController();
    private final TextureController textureController = new TextureController();

    private final ModelController modelController = new ModelController();

    private AssetLoader assetLoader;
    public Shader loadShaderScript(String vertShaderFilename, String fragShaderFilename) {
        //同名文件防止重复加载

        String vertCode= AssetUtils.readAssetFile(context, vertShaderFilename);
        String fragCode = AssetUtils.readAssetFile(context, fragShaderFilename);
        int vertId = GLShaderManager.compileShader(vertCode, GL_VERTEX_SHADER);
        int fragId = GLShaderManager.compileShader(fragCode, GL_FRAGMENT_SHADER);
        Shader shader = new Shader();
        shader.program = GLShaderManager.createProgram(vertId,fragId);
        return shader;
    }

    public void bindShaderAttributePointers(GLShaderProgram program, @NonNull ModelMeta meta){
        Map<String, VertexAttributeMeta> metaMap = meta.attributeMetas;
        GLShaderManager.use(program);
        for(Map.Entry<String, VertexAttributeMeta> entry : metaMap.entrySet()){
            VertexAttributeMeta attribMeta = entry.getValue();
            String attributeName = attribMeta.attributeName;
            GLShaderManager.setAttribute(attributeName,attribMeta.elemCnt,attribMeta.elemType,attribMeta.normalized,attribMeta.strideInBytes,attribMeta.offset);
        }
    }

    MeshModel model;
    Shader shader;
    GLVertexBuffer buffer;

    GLTexture texture;



    public void onSurfaceCreated2(GL10 gl, EGLConfig config) {
        GLCanvasManager.setCanvasSize(800,800);
        GLCanvasManager.enableGLFunction(GL_DEPTH_TEST);
        GLCanvasManager.clearColor(0.2f,0.3f,0.3f,0.0f);
        shader = loadShaderScript("shaders/test.vert","shaders/test.frag");
        buffer = GLVertexBufferManager.genVertexBuffer(false);

        /*
        boolean useEBO = false;
        buffer = new VertexBuffer();
        int[] tmp = new int[1];
        glGenVertexArrays(1, tmp, 0);
        buffer.vao = tmp[0];
        glGenBuffers(1, tmp, 0);
        buffer.vbo = tmp[0];
        if(useEBO){
            glGenBuffers(1, tmp, 0);
            buffer.ebo = tmp[0];
        }else{
            buffer.ebo = GLConstants.NULL_INDEX;
        }
*/
        GLVertexBufferManager.bind(buffer);

        GLErrorUtils.assertNoError();
        /*

        float[] data = model.getVertexData();
        int drawMethod = model.getDrawMethod();
        ByteBuffer dataInBytes = ByteBuffer.allocateDirect(data.length* Float.BYTES)
                .order(ByteOrder.nativeOrder());
        FloatBuffer dataInFloats = dataInBytes.asFloatBuffer();
        dataInFloats.put(data).position(0);

        glBindVertexArray(buffer.vao);
        glBindBuffer(GLConstants.VertexBuffer.ARRAY_BUFFER, buffer.vbo);
        glBufferData(GLConstants.VertexBuffer.ARRAY_BUFFER,
                dataInFloats.capacity() * Float.BYTES,
                dataInFloats,
                drawMethod);*/

        /*
        ModelMeta modelMeta = model.getModelMeta();
        for(ModelMeta.VertexAttributeMeta meta : modelMeta.attributeMetas.values()){
            final int index = glGetAttribLocation(program.programId,meta.attributeName);
            glEnableVertexAttribArray(index);

            glVertexAttribPointer(index, meta.elemCnt, meta.elemType, meta.normalized,
                    meta.strideInBytes,
                    meta.offset);
        }*/

        GLVertexBufferManager.loadVertexAttributeData(model.getVertexData(), model.getDrawMethod());
       // VertexBufferManager.loadElementIndices(model.getVertexIndices(), model.getDrawMethod());
     //   VertexBufferManager.bind(buffer);
        //shader.uniformBinders;
        bindShaderAttributePointers(shader.program, model.getModelMeta());
        //texture = TextureManager.genTexture2D();
       // TextureManager.bind(texture);
       // Bitmap bitmap = AssetUtils.getBitmapFromAsset(context, "textures/container.jpg");
        //texture.unit = 0;
        //TextureManager.loadTexture2D(bitmap);

       // ShaderManager.setUniformMatrix4f("uModeling",transform,false);
        //ShaderManager.setUniformMatrix4f("uView",camera.getView(),false);
        //ShaderManager.setUniformMatrix4f("uProjection",camera.getProjection(),false);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    //    glEnableClientState(GL_VERTEX_ARRAY);
        canvasController.setCanvasSize(800, 800);
        //   CanvasManager.setCanvasSize(800, 800);

        canvasController.enableDepthTest();
        //    CanvasManager.enableGLFunction(GL_DEPTH_TEST);

        canvasController.clearCanvas(0.2f, 0.3f, 0.3f, 0.0f);
        //       CanvasManager.clearColor(0.2f, 0.3f, 0.3f, 0.0f);
        shader = assetLoader.loadShaderScript("straight");
        model = new Cube();
        LoadedModel loadedModel = modelController.loadModel(model,"cube");
        /*
        boolean elemBased = true;
        float[] vertices = model.getVertexData();
        short[] indices = model.getVertexIndices();
        GLVertexBuffer vertexBuffer = GLVertexBufferManager.genVertexBuffer(elemBased);
        GLVertexBufferManager.bind(vertexBuffer);
        GLVertexBufferManager.loadVertexAttributeData(vertices, model.getDrawMethod());
        LoadedModel loadedModel = new LoadedModel();
        loadedModel.elemBased = elemBased;
        loadedModel.vertexBuffer = vertexBuffer;
        loadedModel.vertexCount = model.getVertexCount();
        loadedModel.modelMetaGetter = model::getModelMeta;

        if(elemBased){
            GLVertexBufferManager.loadElementIndices(indices,model.getDrawMethod());
        }*/
       // GLVertexBufferManager.unbind();

     // //  LoadedModel loadedModel = modelController.loadModel(model, "cube");
        buffer = loadedModel.vertexBuffer;
     //   GLVertexBufferManager.bind(buffer);*

        shaderController.bindShaderAttributePointers(shader, loadedModel);
    //    modelController.bindVertexBuffer(loadedModel.vertexBuffer);
        //buffer = VertexBufferManager.genVertexBuffer(false);
        //   VertexBufferManager.bind(buffer);
        //   VertexBufferManager.loadVertexAttributeData(model.getVertexData(), model.getDrawMethod());

        //    program = loadShaderScript("shaders/test.vert", "shaders/test.frag");

        //bindShaderAttributePointers(program, model.getModelMeta());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLCanvasManager.clearCanvas(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
        GLShaderManager.use(shader.program);
        GLVertexBufferManager.bind(buffer);
       // glBindVertexArray(buffer.vao);

       // GLCanvasManager.drawArrays(GL_TRIANGLES,0,model.getVertexCount());
       // glDrawElements(GL_TRIANGLES, model.getVertexCount(),GLConstants.Type.UNSIGNED_SHORT,0);
        GLCanvasManager.drawElements(GL_TRIANGLES, model.getVertexCount(),GL_UNSIGNED_SHORT,0);
    }
}
