import java.awt.*;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.event.*;

import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.vrml97.*;


public class EscenarioBase extends Escenario{
	
	Juego juego;
		
	SimpleUniverse universo;
	private Cubo objetivos[];
	private TransformGroup grupo[];
	private Enemigo enemigos[];
	private TransformGroup grupoe[];
			
	static Vector3f posObjetivos[]={
	new Vector3f(-717f,192f,-210f),		//0
	new Vector3f(-682f,48f,-549f),		//1
	new Vector3f(-591f,159f,-920f),		//2
	new Vector3f(-861f,73f,-1553f),		//3
	new Vector3f(-504f,60f,-1924f),		//4
	new Vector3f(282f,99f,-1875f),		//5
	new Vector3f(731f,74f,-1798f),		//6
	new Vector3f(759f,64f,-1093f),		//7
	new Vector3f(799f,25f,-472f),		//8
	new Vector3f(33f,26f,-1321f),		//9
	new Vector3f(-322f,-3f,-1437f),		//10
	new Vector3f(61f,114f,-545f),		//11
	new Vector3f(203f,32f,-290f),		//12
	new Vector3f(-13f,2014f,-999f),		//13
	new Vector3f(-505f,-21f,-1197f),	//14
	};

	public BranchGroup crearSuperficie(){	
		Scene s = null;
		
		VrmlLoader f=new VrmlLoader();
		
		try {
	  		s = f.load("recursos/escenariob.wrl");
		}
		catch (Exception e) {
	  		System.err.println(e);
	  		System.exit(1);
		}
		
		s.getSceneGroup().setCollidable(false);
		return s.getSceneGroup();
	}
	
	public BackgroundSound cargarSonido(){
	    
	    BackgroundSound sonido;
	    
		MediaContainer contenedor=new MediaContainer("recursos/jet.wav");
		sonido=new BackgroundSound(contenedor,1.0f);
		sonido.setSchedulingBounds(new BoundingSphere(new Point3d(0,0,0),10000));
		sonido.setEnable(true);
		sonido.setLoop(BackgroundSound.INFINITE_LOOPS);
		
		return sonido;		
	}

	public BranchGroup crearEscenario(SimpleUniverse su) {
	
		//Raiz de todo el escenario
		BranchGroup raiz = new BranchGroup();

		BoundingSphere bounds =
	  	new BoundingSphere(new Point3d(0.0,0.0,0.0), 10000.0);
        
        
        //Creación del background
        Color3f color = new Color3f(0.65f, 0.75f, 0.85f);
        Background nodoFondo = new Background(color);
        nodoFondo.setApplicationBounds(bounds);
        raiz.addChild(nodoFondo);
                
        //Escala el escenario
        TransformGroup escala = new TransformGroup();
        Transform3D transformacionEscala = new Transform3D();
        transformacionEscala.setScale(10);
        escala.setTransform(transformacionEscala);
        raiz.addChild(escala);
                
        //Instalacion de las luces

        //Luz ambiental
        Color3f colorAmb = new Color3f(0.7f,0.7f,0.7f);
        AmbientLight nodoLuzAmb = new AmbientLight(colorAmb);
        nodoLuzAmb.setInfluencingBounds(bounds);
        raiz.addChild(nodoLuzAmb);

        //Una luz direccional para dar sensación de profundidad
        Color3f colorDir = new Color3f(0.7f,0.7f,0.7f);
        Vector3f vectorLuzDir  = new Vector3f(-1.0f, -1.0f, -1.0f);

        DirectionalLight luzDir
        =new DirectionalLight(colorDir,vectorLuzDir);
        luzDir.setInfluencingBounds(bounds);
        raiz.addChild(luzDir);
       
       	//Otra luz direccional para dar sensación de profundidad
        colorDir = new Color3f(0.4f,0.4f,0.4f);
        vectorLuzDir  = new Vector3f(1.0f, -1.0f, -1.0f);

        luzDir=new DirectionalLight(colorDir,vectorLuzDir);
        luzDir.setInfluencingBounds(bounds);
        raiz.addChild(luzDir);
       
       	//Carga la superficie VRML
       	escala.addChild(crearSuperficie());
        
		
		//Carga el efecto de sonido
		//raiz.addChild(cargarSonido());

        //Optimiza la raiz
        raiz.compile();

        return raiz;
    } 

	public BranchGroup cargarObjetivos(){
	
		BranchGroup raiz=new BranchGroup();
		
		objetivos=new Cubo[Escenario.NUM_OBJ];
		Transform3D transformacion;
		grupo=new TransformGroup[Escenario.NUM_OBJ];
						
		for(int i=0;i<NUM_OBJ;i++){
			
			objetivos[i]=new Cubo();
			//El Cubo guarda su id de objetivo para identificarlo
			//en colisiones
        	objetivos[i].setUserData(new Integer(i));
        	
        	transformacion=new Transform3D();
        	transformacion.setTranslation(posObjetivos[i]);
        	transformacion.setScale(8);
        	grupo[i]=new TransformGroup(transformacion);
        	grupo[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        	grupo[i].addChild(objetivos[i]);
 	         	        
            raiz.addChild(grupo[i]);
    	}
       
        raiz.compile();
        return raiz;
	}
	
	public void marcarObjetivo(int id){
		
		Transform3D transformacion=new Transform3D();
        //grupo[id].getTransform(transformacion);
        transformacion.setTranslation(posObjetivos[id]);
        transformacion.setScale(0.00001);
        grupo[id].setTransform(transformacion);
	}

	public BranchGroup cargarEnemigos(){
	
		BranchGroup raiz=new BranchGroup();
		
		enemigos=new Enemigo[NUM_JUGADORES];
		Transform3D transformacion;
		grupoe=new TransformGroup[NUM_JUGADORES];
				
		for(int i=0;i<NUM_JUGADORES;i++){
			
			enemigos[i]=new Enemigo();
			        	
        	transformacion=new Transform3D();
        	transformacion.setTranslation(new Vector3f(-505f,-21f,-1197f));
        	grupoe[i]=new TransformGroup(transformacion);
        	grupoe[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        	grupoe[i].addChild(enemigos[i]);
 	         	        
 	        raiz.addChild(grupoe[i]);
    	}
       
        return raiz;
	}

	public void actualizarEnemigos(Posicion pos[]){
		Transform3D transf;
		for(int i=0;i<NUM_JUGADORES;i++){
			if(i!=juego.id){
			transf=new Transform3D();
        	transf.setEuler(new Vector3d(pos[i].ox,pos[i].oy,pos[i].oz));
        	transf.setTranslation(new Vector3f(pos[i].x,pos[i].y,pos[i].z));
        	//transf.setScale(1);
        	grupoe[i].setTransform(transf);
        	}
        }
	}
		
    public EscenarioBase(Juego j) {
        
        juego=j;
        
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.requestFocus();
        
        /*SimpleUniverse*/ universo = new SimpleUniverse(canvas3D);

        //Crea el escenario
        BranchGroup escena = crearEscenario(universo);
        universo.addBranchGraph(escena);
        
        //Carga los objetivos
        BranchGroup objs = cargarObjetivos();
        universo.addBranchGraph(objs);
                
        //Carga el usuario
        Usuario usuario=new Usuario(universo,juego);
        universo.addBranchGraph(usuario);
                
        //Ubicación del usuario
        Vector3f translacion = new Vector3f();
        Transform3D transformacion = new Transform3D();
        TransformGroup grupoVista = null;
        grupoVista=universo.getViewingPlatform().getViewPlatformTransform();
        translacion.set(0.0f, 20f, 0.0f);
        transformacion.setTranslation(translacion);
        grupoVista.setTransform(transformacion);
        
        //Adaptacion de la vista
        View vista = universo.getViewer().getView();
		vista.setBackClipDistance(5000f);
		//vista.setFieldOfView(1.5);
   
        
        //Carga de los otros jugadores
        universo.addBranchGraph(cargarEnemigos());       	
	} 

	
	//Método main de demostración
    public static void main(String[] args) {
        
        EscenarioBase b=new EscenarioBase(new Juego());
                
        WindowListener l = new WindowAdapter(){
            public void windowClosing(WindowEvent e) 
            {System.exit(0);}
        };
        
        Frame frame = new Frame("Base");
        frame.addWindowListener(l);
        frame.add("Center",b);
        frame.setSize(400,400);
        frame.show();
        b.requestFocus();
                      
    } 

}
