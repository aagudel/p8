import java.awt.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.vrml97.*;

public class Enemigo extends BranchGroup{

    public Enemigo(){
    	crearAvatar();
    }
    
    private void crearAvatar()
    {
		Scene s=null;
        VrmlLoader f=new VrmlLoader();
		
		try {
	  		s = f.load("recursos/ndcx.wrl");
		}
		catch (Exception e) {
	  		System.err.println(e);
	  		System.exit(1);
		}

		this.addChild(s.getSceneGroup());
	} 
} 
