package me.project.cloud2drenderer.renderer.procedure.binding.glresource.material;




 public abstract class TextureLoader {

     protected LoadStatus status;

     protected String textureName;

     protected int unit;

     public TextureLoader(String textureName,int unit){
         this.textureName = textureName;
         this.unit = unit;
         status = LoadStatus.NOT_LOADED;
     }

     public LoadStatus getStatus(){
         return status;
     }

     abstract public void asyncLoad();

     abstract public void load();

     abstract public void create();
 }
