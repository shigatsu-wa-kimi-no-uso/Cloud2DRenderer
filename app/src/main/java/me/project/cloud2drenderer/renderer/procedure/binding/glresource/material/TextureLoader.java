package me.project.cloud2drenderer.renderer.procedure.binding.glresource.material;




 public abstract class TextureLoader {

     protected LoadStatus status;

     public TextureLoader(){
         status = LoadStatus.NOT_LOADED;
     }

     public LoadStatus getStatus(){
         return status;
     }

     abstract public void load();

     abstract public void create();
 }
