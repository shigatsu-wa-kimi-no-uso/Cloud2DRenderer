package me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader;

import static android.opengl.GLES30.*;

import android.util.Log;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import me.project.cloud2drenderer.opengl.statemanager.GLShaderManager;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.entity.shader.Shader;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.annotation.ShaderUniform;
import me.project.cloud2drenderer.util.AnnotationUtils;
import me.project.cloud2drenderer.util.DebugUtils;

public class UniformBindingProcessor {

    private static final String tag = UniformBindingProcessor.class.getSimpleName();


    @FunctionalInterface
    public interface GLShaderFloatUniformSetter{
        void set(int location, float[] value, int count, int offset, boolean transpose);
    }

    @FunctionalInterface
    public interface GLShaderIntScalarUniformSetter {
        void set(int location, int value);
    }

    @FunctionalInterface
    public interface GLShaderFloatScalarUniformSetter {
        void set(int location, float value);
    }



    @FunctionalInterface
    public interface GLShaderIntUniformSetter {
        void set(int location, int[] value, int count, int offset, boolean transpose);
    }

    private static final Map<Integer, GLShaderIntUniformSetter> intTypeToSetter = new HashMap<>();
    private static final Map<Integer, GLShaderFloatUniformSetter> floatTypeToSetter = new HashMap<>();
    private static final GLShaderFloatUniformSetter[][] floatSetter = new GLShaderFloatUniformSetter[5][5];
    private static final GLShaderIntUniformSetter[][] intSetter = new GLShaderIntUniformSetter[5][2];
    private static final GLShaderIntScalarUniformSetter intScalarSetter = GLShaderManager::setUniformScalar;
    private static final GLShaderFloatScalarUniformSetter floatScalarSetter = GLShaderManager::setUniformScalar;

    static {
        intSetter[1][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformScalarArray(location,value,count,offset);
        intSetter[2][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformVector2(location,value,count,offset);
        intSetter[3][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformVector3(location,value,count,offset);
        intSetter[4][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformVector4(location,value,count,offset);

        floatSetter[1][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformScalarArray(location,value,count,offset);
        floatSetter[2][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformVector2(location,value,count,offset);
        floatSetter[3][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformVector3(location,value,count,offset);
        floatSetter[4][1] = (location, value, count, offset, transpose) -> GLShaderManager.setUniformVector4(location,value,count,offset);
        floatSetter[2][2] = GLShaderManager::setUniformMatrix2f;
        floatSetter[2][3] = GLShaderManager::setUniformMatrix2x3f;
        floatSetter[2][4] = GLShaderManager::setUniformMatrix2x4f;
        floatSetter[3][2] = GLShaderManager::setUniformMatrix3x2f;
        floatSetter[3][3] = GLShaderManager::setUniformMatrix3f;
        floatSetter[3][4] = GLShaderManager::setUniformMatrix3x4f;
        floatSetter[4][2] = GLShaderManager::setUniformMatrix4x2f;
        floatSetter[4][3] = GLShaderManager::setUniformMatrix4x3f;
        floatSetter[4][4] = GLShaderManager::setUniformMatrix4f;

        int[][] floatTypes = new int[5][5];
        int[][] intTypes = new int[5][3];
        floatTypes[1][1] = GL_FLOAT;
        floatTypes[2][1] = GL_FLOAT_VEC2;
        floatTypes[3][1] = GL_FLOAT_VEC3;
        floatTypes[4][1] = GL_FLOAT_VEC4;
        floatTypes[2][2] = GL_FLOAT_MAT2;
        floatTypes[2][3] = GL_FLOAT_MAT2x3;
        floatTypes[2][4] = GL_FLOAT_MAT2x4;
        floatTypes[3][2] = GL_FLOAT_MAT3x2;
        floatTypes[3][3] = GL_FLOAT_MAT3x2;
        floatTypes[3][4] = GL_FLOAT_MAT3x4;
        floatTypes[4][2] = GL_FLOAT_MAT4x2;
        floatTypes[4][3] = GL_FLOAT_MAT4x3;
        floatTypes[4][4] = GL_FLOAT_MAT4;
        intTypes[1][0] = GL_INT;
        intTypes[2][0] = GL_INT_VEC2;
        intTypes[3][0] = GL_INT_VEC3;
        intTypes[4][0] = GL_INT_VEC4;
        intTypes[1][1] = GL_UNSIGNED_INT;
        intTypes[2][1] = GL_UNSIGNED_INT_VEC2;
        intTypes[3][1] = GL_UNSIGNED_INT_VEC3;
        intTypes[4][1] = GL_UNSIGNED_INT_VEC4;
        intTypes[1][2] = GL_BOOL;
        intTypes[2][2] = GL_BOOL_VEC2;
        intTypes[3][2] = GL_BOOL_VEC3;
        intTypes[4][2] = GL_BOOL_VEC4;
        for(int i=1;i<=4;i++){
            floatTypeToSetter.put(floatTypes[i][1],floatSetter[i][1]);
            intTypeToSetter.put(intTypes[i][0],intSetter[i][1]);
            intTypeToSetter.put(intTypes[i][1],intSetter[i][1]);
            intTypeToSetter.put(intTypes[i][2],intSetter[i][1]);
        }
        for(int i=2;i<=4;i++){
            for(int j=2;j<=4;j++){
                floatTypeToSetter.put(floatTypes[i][j],floatSetter[i][j]);
            }
        }
    }

    @NonNull
    private static void setUniformVar(@NonNull UniformVar uniformVar, @NonNull ShaderUniformMeta meta){
        uniformVar.meta = meta;
        uniformVar.setCount(meta.elemCnt);
        uniformVar.setName(meta.uniformName);
        uniformVar.setOffset(0);
        uniformVar.setTranspose(false);
    }

    @NonNull
    private static UniformVar newUniformVar(ShaderUniformMeta meta){
        UniformVar uniformVar=new UniformVar();
        setUniformVar(uniformVar,meta);
        return uniformVar;
    }

    @NonNull
    @Deprecated
    private static ShaderUniformSetterWrapper injectUniformVar(Field field, RenderContext context, @NonNull ShaderUniformMeta meta){
        ShaderUniformSetterWrapper setterWrapper;
        UniformVar uniformVar;
        if (meta.intBased) {
            UniformVar<int[]> intUniformVar = newUniformVar(meta);
            // TODO: value类型转换在被调用时才发生，需要有一种方法将类型转换移动到调用之前
            GLShaderIntUniformSetter setter = DebugUtils.assertNotNull(intTypeToSetter.get(meta.type));
            setterWrapper = () -> setter.set(intUniformVar.getLocation(), intUniformVar.getValue(), intUniformVar.getCount(), intUniformVar.getOffset(), intUniformVar.isTranspose());
            uniformVar = intUniformVar;
        }else {
            UniformVar<float[]> floatUniformVar = newUniformVar(meta);
            // TODO: value类型转换在被调用时才发生，需要有一种方法将类型转换移动到调用之前
            GLShaderFloatUniformSetter setter  = DebugUtils.assertNotNull(floatTypeToSetter.get(meta.type));
            setterWrapper = () -> setter.set(floatUniformVar.getLocation(), floatUniformVar.getValue(), floatUniformVar.getCount(), floatUniformVar.getOffset(), floatUniformVar.isTranspose());
            uniformVar = floatUniformVar;
        }
        uniformVar.setUniformSetterWrapper(setterWrapper);
        try {
            field.set(context,uniformVar); //字段注入
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return setterWrapper;
    }

    @NonNull
    private static ShaderUniformSetterWrapper getUniformVarSetterWrapper(Field field, Object object, @NonNull ShaderUniformMeta meta){
        ShaderUniformSetterWrapper setterWrapper;
        UniformVar uniformVar;
        if (meta.intBased) {
            UniformVar<int[]> intUniformVar;
            try {
                Object val = field.get(object);
                assert val instanceof UniformVar;
                intUniformVar = (UniformVar<int[]>)val;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // TODO: value类型转换在被调用时才发生，需要有一种方法将类型转换移动到调用之前
            GLShaderIntUniformSetter setter  = DebugUtils.assertNotNull(intTypeToSetter.get(meta.type));
            setterWrapper = () -> setter.set(intUniformVar.getLocation(), intUniformVar.getValue(), intUniformVar.getCount(), intUniformVar.getOffset(), intUniformVar.isTranspose());
            uniformVar = intUniformVar;
        }else {
            UniformVar<float[]> floatUniformVar;
            try {
                Object val = field.get(object);
                assert val instanceof UniformVar;
                floatUniformVar = (UniformVar<float[]>)val;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // TODO: value类型转换在被调用时才发生，需要有一种方法将类型转换移动到调用之前
            GLShaderFloatUniformSetter setter  = DebugUtils.assertNotNull(floatTypeToSetter.get(meta.type));
            setterWrapper = () -> setter.set(floatUniformVar.getLocation(), floatUniformVar.getValue(), floatUniformVar.getCount(), floatUniformVar.getOffset(), floatUniformVar.isTranspose());
            uniformVar = floatUniformVar;
        }
        uniformVar.setUniformSetterWrapper(setterWrapper);
        return setterWrapper;
    }

    @NonNull
    private static ShaderUniformSetterWrapper getRawFieldSetterWrapper(@NonNull Field field, Object object, ShaderUniformMeta meta) {
        //注意：不使用UniformVar封装时，类型必须是引用类型才能在后续修改时同步在setter中修改，且此种方法无法修改count，transpose，offset等属性！
        Object value;
        try {
            if (field.getType().isPrimitive()) {
                throw new RuntimeException("Primitive type field binding is not supported.");
            }
            value = field.get(object);
            if (value == null) {
                Log.e(tag, "Shader uniform variable binding a null raw field may make uniform setter not work!");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ShaderUniformSetterWrapper setterWrapper;
        // TODO: value类型转换在被调用时才发生，需要有一种方法将类型转换移动到调用之前
        if (meta.intBased) {
            GLShaderIntUniformSetter setter = DebugUtils.assertNotNull(intTypeToSetter.get(meta.type));
            setterWrapper = () -> setter.set(meta.location[0], (int[]) value, meta.elemCnt, 0, false);
        } else {
            GLShaderFloatUniformSetter setter = DebugUtils.assertNotNull(floatTypeToSetter.get(meta.type));
            setterWrapper = () -> setter.set(meta.location[0], (float[]) value, meta.elemCnt, 0, false);
        }
        return setterWrapper;
    }

    private static void processFields(@NonNull RenderContext context, @NonNull List<Field> fields, Map<String, ShaderUniformMeta> uniformMetas,Object object,String fieldNamePrefix,String uniformNamePrefix){
        for (Field field : fields) {
            ShaderUniform anno = field.getAnnotation(ShaderUniform.class);
            DebugUtils.assertNotNull(anno);
            String uniformName = uniformNamePrefix + anno.uniformName();
            ShaderUniformMeta meta = uniformMetas.get(uniformName);
            String fieldName = fieldNamePrefix + field.getName();

            String initMode;
            String assignmentMode;
            UniformFlag[] flags = anno.flags();
            int mask = 0;

            for(UniformFlag flag : flags){
                mask |= flag.mask;
            }

            ShaderUniformSetterWrapper setterWrapper;
            if((mask&UniformFlag.AUTO_INITIALIZE.mask)!=0){
                try{
                    field.set(object,field.getClass().newInstance());
                }catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
                initMode = "auto initialized";
            }else {
                initMode = "manual initialized";
            }

            //对于shader中的结构体，没有对应的metadata，只有结构体成员展开后的metadata
            if((mask&UniformFlag.IS_STRUCT.mask)!=0){
                try {
                    generateShaderUniformSetter(context,uniformMetas,field.get(object),fieldName+".",uniformName+".");
                    continue;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }else {
                if (meta == null) {
                    String msg = String.format("Shader uniform variable \"%s\" has no metadata!",uniformName);
                    Log.e(tag, msg);
                    continue;
                }
            }

            if((mask&UniformFlag.USING_RAW.mask)!=0) {
                setterWrapper = getRawFieldSetterWrapper(field, context,meta);
            }else {
                setterWrapper = getUniformVarSetterWrapper(field, context, meta);
                try {
                    setUniformVar((UniformVar) Objects.requireNonNull(field.get(object)), meta);
                }catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            if((mask&UniformFlag.AUTO_ASSIGN.mask) != 0) {
                context.addAutoAssignedUniforms(setterWrapper);
                assignmentMode = "auto assignment";
            }else {
                if((mask&UniformFlag.USING_RAW.mask) != 0){
                    throw new RuntimeException("Unsupported operation.");
                }
                assignmentMode = "manual assignment";
            }
            String msg = String.format(Locale.getDefault(),
                    "Shader uniform variable \"%s\" has registered %s in @%s@%d binding with \"%s\" %s.",
                    uniformName,
                    assignmentMode,
                    context.getClass().getSimpleName(),
                    context.hashCode(),
                    fieldName,
                    initMode);
            Log.d(tag, msg);
        }
    }

    //采用method绑定只能为自动提交
    private static void processMethods(@NonNull RenderContext context, @NonNull List<Method> methods, Map<String, ShaderUniformMeta> uniformMetas,Object object,String fieldNamePrefix,String uniformNamePrefix)  {
        for (Method method : methods) {
            ShaderUniform anno = method.getAnnotation(ShaderUniform.class);
            DebugUtils.assertNotNull(anno);
            String uniformName = uniformNamePrefix + anno.uniformName();
            ShaderUniformMeta meta = uniformMetas.get(uniformName);
            String methodName = fieldNamePrefix + method.getName();
            if (meta == null) {
                String msg = String.format("Shader uniform variable \"%s\" has no metadata!",uniformName);
                Log.e(tag, msg);
                continue;
            }

            ShaderUniformSetterWrapper setterWrapper;
            boolean isArray = method.getReturnType().isArray();
            // TODO: value类型转换在被调用时才发生，需要有一种方法将类型转换移动到调用之前
            if (meta.intBased) {
                if(isArray) {
                    GLShaderIntUniformSetter setter = DebugUtils.assertNotNull(intTypeToSetter.get(meta.type));
                    setterWrapper = () -> {
                        try {
                            setter.set(meta.location[0], (int[]) method.invoke(object), meta.elemCnt, 0, false);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }else {
                    setterWrapper = () -> {
                        try {
                            intScalarSetter.set(meta.location[0], (int) method.invoke(object));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }
            }else {
                if(isArray) {
                    GLShaderFloatUniformSetter setter = DebugUtils.assertNotNull(floatTypeToSetter.get(meta.type));
                    setterWrapper = () -> {
                        try {
                            setter.set(meta.location[0], (float[]) method.invoke(object), meta.elemCnt, 0, false);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }else {
                    setterWrapper = () -> {
                        try {
                            floatScalarSetter.set(meta.location[0], (float) method.invoke(object));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }
            }
            context.addAutoAssignedUniforms(setterWrapper);
            String msg = String.format(Locale.getDefault(),
                    "Shader uniform variable \"%s\" has registered auto assignment in %s@%d binding with method \"%s\"",
                    uniformName,context.getClass().getSimpleName(),
                    context.hashCode(),
                    methodName);
            Log.d(tag, msg);
        }
    }

    public static void generateShaderUniformSetter(@NonNull RenderContext context, @NonNull Shader shader) {
        Map<String, ShaderUniformMeta> uniformMetas = shader.getUniformMetas();
        generateShaderUniformSetter(context,uniformMetas,context,"","");
      /*  Class<? extends RenderContext> contextClass = context.getClass();
        List<Field> fields = AnnotationUtils.getAnnotatedFields(contextClass,ShaderUniform.class);
        List<Method> methods = AnnotationUtils.getAnnotatedMethods(contextClass,ShaderUniform.class);
        Map<String, ShaderUniformMeta> uniformMetas = shader.getUniformMetas();
        processFields(context,fields,uniformMetas,context,"","");
        processMethods(context,methods,uniformMetas,context,"","");*/
    }


    public static void generateShaderUniformSetter(@NonNull RenderContext context, @NonNull Map<String, ShaderUniformMeta>  uniformMetas,Object object,String fieldNamePrefix,String uniformNamePrefix) {
        Class<?> structClass = object.getClass();
        List<Field> fields = AnnotationUtils.getAnnotatedFields(structClass,ShaderUniform.class);
        List<Method> methods = AnnotationUtils.getAnnotatedMethods(structClass,ShaderUniform.class);
        processFields(context,fields,uniformMetas,object,fieldNamePrefix,uniformNamePrefix);
        processMethods(context,methods,uniformMetas,object,fieldNamePrefix,uniformNamePrefix);
    }

}
