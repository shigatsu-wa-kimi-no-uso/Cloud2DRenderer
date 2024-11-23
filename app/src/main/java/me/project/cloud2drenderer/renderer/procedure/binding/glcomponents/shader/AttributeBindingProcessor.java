package me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader;

import static android.opengl.GLES20.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.project.cloud2drenderer.renderer.entity.model.MeshModel;
import me.project.cloud2drenderer.renderer.entity.model.ModelMeta;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.VertexAttribute;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.shader.annotation.VertexDataClass;
import me.project.cloud2drenderer.util.DebugUtils;

import androidx.annotation.NonNull;

public class AttributeBindingProcessor {

    private static final Map<String,Integer> typeNameToGLType;


    static {
        typeNameToGLType = Map.of(
                "float",GL_FLOAT,
                "Float",GL_FLOAT,
                "int",GL_INT,
                "Integer",GL_INT,
                "byte", GL_BYTE,
                "Byte",GL_BYTE,
                "short",GL_SHORT,
                "Short",GL_SHORT);
    }

    public AttributeBindingProcessor(){


    }

    private static int glTypeToSize(int glTypeEnum){
        switch (glTypeEnum){
            case GL_FLOAT:
            case GL_FIXED:
                return Float.BYTES;
            case GL_INT:
            case GL_UNSIGNED_INT:
                return Integer.BYTES;
            case GL_SHORT:
            case GL_UNSIGNED_SHORT:
                return Short.BYTES;
            case GL_BYTE:
            case GL_UNSIGNED_BYTE:
                return Byte.BYTES;
            default:
                throw new IllegalArgumentException("No type matched.");
        }
    }

    @NonNull
    private static VertexAttributeMeta getSingleVertexAttributeMeta(@NonNull Field field) {
        VertexAttributeMeta meta = new VertexAttributeMeta();
        VertexAttribute attribute = field.getAnnotation(VertexAttribute.class);
        DebugUtils.checkNotNull(attribute);
        meta.attributeName = attribute.attributeName();
        meta.elemCnt = DebugUtils.checkRange(attribute.elemCnt(),1,4);
        meta.elemType = attribute.elemType().glEnumCode;
        meta.normalized = attribute.normalized();
        meta.offset = attribute.offset();
        completeDefaults(field,meta);
        return meta;
    }

    private static void completeDefaults(Field field, @NonNull VertexAttributeMeta meta){
        if (meta.elemType == -1) {
            Class<?> type = field.getType();
            //最多解析一维数组
            try {
                if (type.isArray()) {
                    String typeName = Objects.requireNonNull(type.getComponentType()).getSimpleName();
                    meta.elemType = Objects.requireNonNull(typeNameToGLType.get(typeName));
                } else {
                    meta.elemType = Objects.requireNonNull(typeNameToGLType.get(type.getSimpleName()));
                }
            }catch (NullPointerException e){
                throw new IllegalArgumentException(type.getName() + " is not supported.");
            }
        }
        if (Objects.equals(meta.attributeName, "")) {
            meta.attributeName = field.getName();
        }
    }

    private static void locateInterleaved(@NonNull Map.Entry<String,VertexAttributeMeta>[] metas){
        int strideInBytes = 0;
        for(Map.Entry<String,VertexAttributeMeta> entry : metas) {
            VertexAttributeMeta meta = entry.getValue();
            meta.offset = strideInBytes;
            strideInBytes += glTypeToSize(meta.elemType) * meta.elemCnt;
        }
        for(Map.Entry<String,VertexAttributeMeta> entry:  metas) {
            VertexAttributeMeta meta = entry.getValue();
            meta.strideInBytes = strideInBytes;
        }
    }

    private static void locateSeparate(@NonNull Map.Entry<String,VertexAttributeMeta>[] metas,int vertexCount) {
        for (Map.Entry<String,VertexAttributeMeta> entry: metas) {
            VertexAttributeMeta meta = entry.getValue();
            meta.strideInBytes = glTypeToSize(meta.elemType) * meta.elemCnt;
        }
        int offset = 0;
        for (Map.Entry<String,VertexAttributeMeta> entry: metas) {
            VertexAttributeMeta meta = entry.getValue();
            meta.offset = offset;
            offset += meta.strideInBytes * vertexCount;
        }
    }

    @NonNull
    public static ModelMeta getModelMeta(@NonNull MeshModel model)  {
        Class<? extends MeshModel> modelClass = model.getClass();
        VertexDataClass vertexDataClassAnno = modelClass.getAnnotation(VertexDataClass.class);
        DebugUtils.checkNotNull(vertexDataClassAnno);
        BufferMode bufferMode = vertexDataClassAnno.bufferMode();
        boolean autoLocate = vertexDataClassAnno.autoLocate();
        ModelMeta modelMeta = new ModelMeta();
        List<Field> fields = new ArrayList<>(List.of(modelClass.getDeclaredFields()));
        fields.removeIf(elem-> !elem.isAnnotationPresent(VertexAttribute.class));
        fields.sort(Comparator.comparingInt(
                elem -> DebugUtils.assertNotNull(elem.getAnnotation(VertexAttribute.class)).order()
        ));

        Map.Entry<String,VertexAttributeMeta>[] metas = new Map.Entry[fields.size()];
        //VertexAttributeMeta[] metas = new VertexAttributeMeta[fields.size()];
        int i = 0;
        for (Field field: fields) {
            VertexAttributeMeta meta = getSingleVertexAttributeMeta(field);
            metas[i] = Map.entry(meta.attributeName,meta);
            i++;
        }
        modelMeta.vertexCount = model.getVertexCount();
        if(autoLocate){
            switch (bufferMode){
                case INTERLEAVED:
                    locateInterleaved(metas); break;
                case SEPARATE:
                    locateSeparate(metas,modelMeta.vertexCount); break;
                default:
            }
        }
        modelMeta.attributeMetas = Map.ofEntries(metas);
        return modelMeta;
    }





}
