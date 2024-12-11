package me.project.cloud2drenderer.util;

import me.project.cloud2drenderer.renderer.context.BlinnPhongRenderContext;
import me.project.cloud2drenderer.renderer.context.CheckerBoardRenderContext;
import me.project.cloud2drenderer.renderer.context.LuminousRenderContext;
import me.project.cloud2drenderer.renderer.context.MixedTextureRenderContext;
import me.project.cloud2drenderer.renderer.context.flipbook.FlipbookBlinnPhongRenderContext;
import me.project.cloud2drenderer.renderer.context.flipbook.SequenceFrameRenderContext;
import me.project.cloud2drenderer.renderer.context.flipbook.SixWayLightingRenderContext;
import me.project.cloud2drenderer.renderer.context.TerrainMeshRenderContext;
import me.project.cloud2drenderer.renderer.entity.AssetBinding;
import me.project.cloud2drenderer.renderer.entity.MaterialBinding;
import me.project.cloud2drenderer.renderer.entity.material.CheckerBoard;
import me.project.cloud2drenderer.renderer.entity.material.luminous.Luminous;
import me.project.cloud2drenderer.renderer.entity.others.light.DistantLight;
import me.project.cloud2drenderer.renderer.entity.others.light.PointLight;
import me.project.cloud2drenderer.renderer.entity.material.BlinnPhong;
import me.project.cloud2drenderer.renderer.entity.material.DiffuseTextureMaterial;
import me.project.cloud2drenderer.renderer.entity.material.MixedImgMaterial;
import me.project.cloud2drenderer.renderer.entity.material.SixWayLighting;
import me.project.cloud2drenderer.renderer.entity.material.TerrainMaterial;
import me.project.cloud2drenderer.renderer.entity.others.flipbook.SequenceFrameParams;
import me.project.cloud2drenderer.renderer.procedure.binding.glcomponents.material.TextureSetter;

public class SceneUtils {

    public static AssetBinding getCubeAssetBinding(float ratio, float[] scale, float[] position){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        MixedImgMaterial material = new MixedImgMaterial();
        ab.pipelineName = "non_blend";
        mb.textureNames = new String[]{"container","awesomeface"};
        mb.textureSetters = new TextureSetter[]{material::setTexture1,material::setTexture2};
        mb.shaderName = "mixed_img";
        material.setRatio(ratio);
        mb.material = material;
        ab.modelName = "cube";
        ab.materialBinding = mb;
        MixedTextureRenderContext context = new MixedTextureRenderContext();
        ab.transform = MatUtils.newTransform(position,scale);
        ab.context = context;
        return ab;
    }

    public static AssetBinding getCubeAssetBinding2(String name,float[] rotate, float[] scale, float[] position, PointLight pointLight,DistantLight distantLight){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        BlinnPhong material = new BlinnPhong();
        material.setKa(new float[]{0.3f,0.3f,0.3f});
        material.setKs(new float[]{1f,1f,1f});
        material.setKd(new float[]{1f,1f,1f});
        material.setShininess(32);
        ab.pipelineName = "non_blend";
        mb.textureNames = new String[]{"container2","container2_specular"};
        mb.textureSetters = new TextureSetter[]{material::setDiffuseMap,material::setSpecularMap};
        mb.shaderName = "blinn_phong";
        mb.material = material;
        ab.modelName = "cube";
        ab.materialBinding = mb;
        BlinnPhongRenderContext context = new BlinnPhongRenderContext();
        context.name = name;
        context.setAmbientIntensity(new float[]{1f,1f,1f});
        context.setPointLight(pointLight);
        context.setDistantLight(distantLight);
        ab.transform = MatUtils.newTransform(position,scale,rotate);
        ab.context = context;
        return ab;
    }


    public static AssetBinding getLightCubeAssetBinding(String name,float[] rotate, float[] scale, PointLight pointLight){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        Luminous material = new Luminous();
        material.setLightIntensity(pointLight.getIntensity());
        ab.pipelineName = "non_blend";
        mb.shaderName = "light_cube";
        mb.material = material;
        ab.modelName = "cube";
        ab.materialBinding = mb;
        LuminousRenderContext context = new LuminousRenderContext();
        context.setPointLight(pointLight);
        context.name = name;
        ab.transform = MatUtils.newTransform(pointLight.getPosition(),scale,rotate);
        ab.context = context;
        return ab;
    }


    public static AssetBinding getCheckerBoardAssetBinding(String name, float[] scale, float[] position,PointLight pointLight,DistantLight distantLight){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        ab.pipelineName = "non_blend";
        CheckerBoard material = new CheckerBoard();
        material.setKa(new float[]{1f,1f,1f});
        material.setKs(new float[]{0.03f,0.03f,0.03f});
        material.setKd(new float[]{0.6f,0.6f,0.6f});
        material.setShininess(64);
        material.setColor1(1.0f,174.0f/255.0f,201.0f/255.0f);
        material.setColor2(1.0f,127.0f/255.0f,39.0f/255.0f);
        mb.material = material;
        mb.shaderName = "checkerboard";
        ab.modelName = "rectangle";
        ab.materialBinding = mb;
        CheckerBoardRenderContext context = new CheckerBoardRenderContext();
        context.setAmbientIntensity(new float[]{0.2f,0.2f,0.2f});
        context.setPointLight(pointLight);
        context.setDistantLight(distantLight);
        context.name = name;
        ab.transform = MatUtils.newTransform(scale,position,new float[]{90,0,0});
        ab.context = context;
        return ab;
    }


    public static AssetBinding getBillboardAssetBinding(float width,float height,float[] position){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        ab.pipelineName = "blend";
        DiffuseTextureMaterial material = new DiffuseTextureMaterial();
        mb.material = material;
        mb.textureNames = new String[]{"SmokeBall_Albedo"};
        mb.textureSetters = new TextureSetter[]{material::setDiffuseTexture};
        mb.shaderName = "flipbook/straight";
        ab.modelName = "rectangle";
        ab.materialBinding = mb;

        SequenceFrameRenderContext context = new SequenceFrameRenderContext();
        SequenceFrameParams seqFrameParams = new SequenceFrameParams();
        seqFrameParams.setCurrentFrameIndex(0);
        seqFrameParams.setFlipBookShape(new float[]{8.0f,8.0f});
        seqFrameParams.setFrequency(1.0f/2.5f);
        context.setSeqFrameParams(seqFrameParams);
        ab.transform = MatUtils.newTransform(position,new float[]{width,height,1});
        ab.context = context;
        return ab;
    }

    public static MaterialBinding getSixWayLightingMaterialBinding(String albedoTex,String lightMapA,String lightMapB,int imagesPerLine,int rowCnt, float framesPerSecond,String shaderName){
        MaterialBinding mb = new MaterialBinding();
        SixWayLighting material = new SixWayLighting();
        mb.material = material;
        // mb.textureNames = new String[]{"SmokeBall_Albedo","SmokeBall_RLT","SmokeBall_BBF"};
        //    mb.textureNames = new String[]{"vex.albedo","vex.A_RTB","vex.B_LBF"};
        //  mb.textureNames = new String[]{"cloud_v830.albedo","cloud_v830.lightmap_RLT","cloud_v830.lightmap_BBF"};
        //   mb.textureNames = new String[]{"1-76_Albedo","1-76_A","1-76_B"};
        material.setShape(imagesPerLine, rowCnt);
        material.setFrequency(framesPerSecond);
        mb.textureNames = new String[]{albedoTex,lightMapA,lightMapB};
        mb.textureSetters = new TextureSetter[]{material::setDiffuseTexture,material::setLightMapA,material::setLightMapB};
        mb.shaderName = shaderName;
        return mb;
    }

    public static AssetBinding getBillboardAssetBinding(float width, float height, float[] position, float[] rotation, MaterialBinding mb, PointLight pointLight, DistantLight distantLight){
        AssetBinding ab = new AssetBinding();
        ab.pipelineName = "blend";
        ab.modelName = "rectangle";
        ab.materialBinding = mb;
        SixWayLightingRenderContext context = new SixWayLightingRenderContext();
        context.setPointLight(pointLight);
        context.setDistantLight(distantLight);

//        SequenceFrameParams seqFrameParams = new SequenceFrameParams();
//        seqFrameParams.setCurrentFrameIndex(0);
//        seqFrameParams.setFlipBookShape(new float[]{18.0f,14.0f});
//        seqFrameParams.setFrequency(8.0f/60.0f);
 //       context.setSeqFrameParams(seqFrameParams);
        context.setPosition(position);
        context.setScale(new float[]{width,height,1});
        ab.transform = MatUtils.newTransform(position,new float[]{width,height,1},rotation);
        ab.context = context;
        return ab;
    }




    public static AssetBinding getBillboardAssetBinding2(float width, float height, float[] position, float[] rotation, PointLight pointLight){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        ab.pipelineName = "blend";

        BlinnPhong material = new BlinnPhong(true);
        mb.material = material;
        material.setKa(new float[]{0.3f,0.3f,0.3f});
        material.setKs(new float[]{0.01f,0.01f,0.01f});
        material.setKd(new float[]{1f,1f,1f});
        material.setShininess(64);
        mb.textureNames = new String[]{"SmokeBall_Albedo","SmokeBall_Normal"};
        mb.textureSetters = new TextureSetter[]{material::setDiffuseMap,material::setNormalMap};
        mb.shaderName = "flipbook/normal_lighting";
        ab.modelName = "rectangle";
        ab.materialBinding = mb;

        FlipbookBlinnPhongRenderContext context = new FlipbookBlinnPhongRenderContext();
        context.setPointLight(pointLight);
        context.setAmbientIntensity(new float[]{0.2f,0.2f,0.2f});
        SequenceFrameParams seqFrameParams = new SequenceFrameParams();
        seqFrameParams.setCurrentFrameIndex(0);
        seqFrameParams.setFlipBookShape(new float[]{8.0f,8.0f});
        seqFrameParams.setFrequency(1.0f/2.5f);
        context.setSeqFrameParams(seqFrameParams);
        context.setPosition(position);
        context.setScale(new float[]{width,height,1});
        ab.transform = MatUtils.newTransform(position,new float[]{width,height,1},rotation);
        ab.context = context;
        return ab;
    }


    public static AssetBinding getTerrainAssetBinding(float width,float height,float[] position){
        AssetBinding ab = new AssetBinding();
        MaterialBinding mb = new MaterialBinding();
        ab.pipelineName = "non_blend";

        TerrainMaterial material =  new TerrainMaterial();
        mb.textureNames = new String[]{"japan.terrain_heightmap","japan.terrain_color"};
        mb.material = material;
        mb.textureSetters = new TextureSetter[]{material::setHeightMap,material::setAlbedo};
        mb.shaderName = "terrain";

        ab.modelName = "terrain_mesh";
        ab.materialBinding = mb;

        ab.context = new TerrainMeshRenderContext();
        ab.transform = MatUtils.newTransform(position,new float[]{width,height,1});
        return ab;
    }


}
