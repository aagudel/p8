import java.awt.*;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.vrml97.*;

public class Usuario extends BranchGroup{

    Juego juego;
    public Usuario(SimpleUniverse universo,Juego j){
    	juego=j;
    	crearAvatar(universo);
    }
    
    private void crearAvatar(SimpleUniverse universo)
    {
		
		//poner a un cubo de avatar
        /*
		ColorCube movil=new ColorCube(0.2);
		Vector3f translacion=new Vector3f(0,0,-5);
		Transform3D transformacion=new Transform3D();
		transformacion.setTranslation(translacion);
		//transformacion.rotY(-Math.PI/2);
        TransformGroup grupoTransformado=new TransformGroup(transformacion);
        grupoTransformado.addChild(movil);
        */
        
        Scene s=null;
        VrmlLoader f=new VrmlLoader();
		
		try {
	  		s = f.load("recursos/ndcx.wrl");
		}
		catch (Exception e) {
	  		System.err.println(e);
	  		System.exit(1);
		}

		ViewerAvatar avatar=new ViewerAvatar();
        avatar.addChild(s.getSceneGroup());
        
        universo.getViewer().setAvatar(avatar);
                        
        TransformGroup puntoDeVista;
        puntoDeVista = universo.getViewingPlatform().getViewPlatformTransform();
        Control control = new Control(puntoDeVista,juego);
        control.setSchedulingBounds(new BoundingSphere(new Point3d(),10000.0));
        this.addChild(control);
	}
} 
