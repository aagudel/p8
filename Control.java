
import java.awt.event.*;
import java.awt.AWTEvent;
import javax.media.j3d.*;
import java.util.Enumeration;
import javax.vecmath.*;

class Control extends Behavior
{
	Juego juego;
	
    WakeupCondition condiciones;    
   
    TransformGroup aeronave;
    
    double rotacionX=0.0;
    double rotacionY=0.0;

    public static final float MAX_VELOCIDAD=6.0f;
    public static final float MIN_VELOCIDAD=1.5f;
    public static final float DELTA_VELOCIDAD=0.5f;
    public float velocidad=4.5f;
        
    public static final double ANG_ROT_X=0.01;
    public static final double ANG_ROT_Y=0.01;
    //public static final double ANG_REC_Z=0.004;
    //public static final double ANG_CERO=0.001;
    
    public static final double MAX_ANG_X=Math.PI/2;
            
    public Control(TransformGroup tg,Juego j) {
		
		aeronave=tg;
		juego=j;
		Bounds b = new BoundingSphere(new Point3d(0.0,0.0,0.0),10000.0);
		this.setSchedulingBounds(b);
    }
    
    public void initialize() {
		WakeupOnAWTEvent teclado=new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    	WakeupCriterion tiempo=new WakeupOnElapsedFrames(1);
    	WakeupOnCollisionEntry colision=new WakeupOnCollisionEntry(aeronave);
    	WakeupOnCollisionMovement c1=new WakeupOnCollisionMovement(aeronave);
    	WakeupOnCollisionExit c2=new WakeupOnCollisionExit(aeronave);
	 	
	 	WakeupCriterion[] criterios = {teclado,tiempo,colision,c1,c2};
	 	condiciones=new WakeupOr(criterios);
	
		wakeupOn(condiciones);
    }

    public void processStimulus(Enumeration criterio) {
		WakeupOnAWTEvent ev;
		WakeupCriterion evento;	
		AWTEvent[] eventos;
		
		while(criterio.hasMoreElements()){
	    	evento = (WakeupCriterion) criterio.nextElement();
	    	if(evento instanceof WakeupOnElapsedFrames){
		   		procesarPulso();

	    	}else if (evento instanceof WakeupOnAWTEvent) {
				ev = (WakeupOnAWTEvent) evento;
				eventos = ev.getAWTEvent();
				procesarTecla(eventos);
	    	}else if (evento instanceof WakeupOnCollisionEntry) {
    			Node theLeaf = ((WakeupOnCollisionEntry)evento).
    							getTriggeringPath().getObject();
    			System.out.println("Entro" + theLeaf.getUserData());
			}
			else if(evento instanceof WakeupOnCollisionMovement){
				Object obj = ((WakeupOnCollisionMovement)evento).
    							getTriggeringPath().getObject().getUserData();
    			if(obj!=null)
    			System.out.println("Colision con " + obj);
    			if(obj instanceof Integer)
    				juego.reservarObjetivo((Integer)obj);
			}
	    	else if(evento instanceof WakeupOnCollisionExit){
	    		Node theLeaf = ((WakeupOnCollisionExit)evento).
    							getTriggeringPath().getObject();
    			System.out.println("Salio " + theLeaf.getUserData());
	    	}
	    	else{
	    		System.out.println("Hubo evento descono");
	    	}
	    	
		}
		wakeupOn(condiciones);
    }

	void procesarPulso(){
		
		Transform3D t = new Transform3D();        
		//Transform3D tz = new Transform3D();
		//Transform3D ty = new Transform3D();
		Vector3f direccion=new Vector3f(0f,0f,velocidad);
		
		aeronave.getTransform(t);
			
		Vector3f translacion = new Vector3f();
		t.get(translacion);
		t.transform(direccion);
		
		translacion.x -= direccion.x;
		translacion.y -= direccion.y;
		translacion.z -= direccion.z;
		
		//Cálcula la recuperación aerodinámica
		/*if(rotacionZ>ANG_CERO){
			tz.rotZ(-ANG_REC_Z);
			//ty.rotY(ANG_REC_Z);
			rotacionZ-=ANG_REC_Z;
		}
		else if(rotacionZ<-ANG_CERO){
			tz.rotZ(ANG_REC_Z);
			//ty.rotY(-ANG_REC_Z);
			rotacionZ+=ANG_REC_Z;
		}else
			rotacionZ=0f;
		
		//t.mul(ty);
		t.mul(tz);
		*/
		t.setTranslation(translacion);
		
		aeronave.setTransform(t);
		t.transform(direccion);
		Point3d euler=Euler.getEulerRotation(t);
		juego.actualizarPosicion(translacion,euler);
	}
	
	void procesarJoystick(){}
	
    void procesarTecla(AWTEvent[] eventos){
	
		for(int i = 0; i < eventos.length; ++i){
	    
	    if(eventos[i] instanceof KeyEvent) {
			
			KeyEvent ev=(KeyEvent)eventos[i];
			int caracter=ev.getKeyChar();
			
			Transform3D t = new Transform3D();			
			Transform3D tx = new Transform3D();        
			Transform3D ty = new Transform3D();
			//Transform3D tz = new Transform3D();
			
			Vector3f direccion=new Vector3f(0f,0f,0.5f);
		
			aeronave.getTransform(t);
			
			Vector3f translacion = new Vector3f();
			t.get(translacion);
			t.transform(direccion);
		
			translacion.x -= direccion.x;
			translacion.y -= direccion.y;
			translacion.z -= direccion.z;
	
			if (ev.getKeyCode()==KeyEvent.VK_UP){
			    if(rotacionX>-MAX_ANG_X)
			    	rotacionX-=ANG_ROT_X;
			    
			}
			else if(ev.getKeyCode()==KeyEvent.VK_DOWN){
			    if(rotacionX<MAX_ANG_X)
			    	rotacionX+=ANG_ROT_X;
			}
			else if(ev.getKeyCode()==KeyEvent.VK_RIGHT){
				
				rotacionY-=ANG_ROT_Y;
				//ty.rotY(-ANG_ROT_Y);
				//tz.rotZ(-ANG_ROT_Y);
				//rotacionZ+=-ANG_ROT_Y;
				//rotacionZ=rotacionZ%Math.PI;
			}
			else if(ev.getKeyCode()==KeyEvent.VK_LEFT) {
			    
			    rotacionY+=ANG_ROT_Y;
			    //ty.rotY(ANG_ROT_Y);
			    //tz.rotZ(ANG_ROT_Y);
			    //rotacionZ +=ANG_ROT_Y;
			    //rotacionZ=rotacionZ%Math.PI;
			}
			else if(caracter=='+'&&velocidad<MAX_VELOCIDAD){
			   	velocidad+=DELTA_VELOCIDAD;
			}
			else if(caracter=='-'&&velocidad>MIN_VELOCIDAD){
			    velocidad-=DELTA_VELOCIDAD;
			}
			else if(caracter=='p'){
			    velocidad=0f;
			}
			else if(caracter=='a'){
			System.out.println(translacion.x+","+translacion.y+","+translacion.z);
			}
			
			// ty.rotY(rotacionY);			
			
			//t.mul(ty);
			
			tx.rotX(rotacionX);
			t.rotY(rotacionY);			
			t.mul(tx);
			//t.mul(tz);
			
			t.setTranslation(translacion);
			
			aeronave.setTransform(t);
			
			t.transform(direccion);
			Point3d euler=Euler.getEulerRotation(t);
			juego.actualizarPosicion(translacion,euler);
	   	}
		}
	}
}



