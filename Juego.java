import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import javax.vecmath.*;

public class Juego
{
	private EscenarioBase escenario;
	private Vector objetivos;
	private boolean objetivoRevisado[];
	private Vector enemigos;
	AudioClip snd;
	AudioClip fnd;
	AudioClip lst;
				
	public int reserva=Escenario.NO_RESERVAR;
	public Posicion pos;
	public int id;
	
		
	public static boolean DEBUG=false;
	
	public Juego(){
		objetivos=new Vector(Escenario.NUM_OBJ);
		enemigos=new Vector(20);
		pos=new Posicion();				
		int i;	
		for(i=0;i<Escenario.NUM_JUGADORES;i++)
			enemigos.add(new Posicion());
		
		for(i=0;i<Escenario.NUM_OBJ;i++)		
			objetivos.add(new Boolean(false));
		
		objetivoRevisado=new boolean[Escenario.NUM_OBJ];
		
		try{
    		snd=Applet.newAudioClip(new URL("file:recursos/sonido1.wav"));   
    		lst=Applet.newAudioClip(new URL("file:recursos/sonido2.wav"));   
    		fnd=Applet.newAudioClip(new URL("file:recursos/jet.wav"));   
    		
    	}catch(Exception ex) { }
	}
	
	public void cargarEscenario(){
		
		escenario=new EscenarioBase(this);
                
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) 
            {System.exit(0);}
        };
        
        Frame frame = new Frame("Jugador "+id);
        frame.addWindowListener(l);
        frame.add("Center",escenario);
        frame.setSize(400,400);
        frame.show();
        escenario.requestFocus();
    } 
	
	public void actSonido(){
	    fnd.loop();
	}
	
	public void  iniciarJuego(String servidor){
		Enlace en=new Enlace(this,pos,objetivos,enemigos);
		id=en.conectar(servidor);
		System.out.println(servidor+": id del jugador "+id);
		en.start();
	}
	
	public synchronized void reservarObjetivo(Integer id){
		if(reserva==Escenario.NO_RESERVAR&&
			((Boolean)objetivos.get(id.intValue())).booleanValue()==false){
			reserva=id.intValue();
			System.out.println("Se esta tratando de reservar el objetivo "+reserva);
		}	
	}
	
	public synchronized void reservaAceptada(boolean reservaAceptada){
		if(reserva!=Escenario.NO_RESERVAR){
			if(reservaAceptada){
				System.out.println("Se acepto la reserva de "+reserva);
				escenario.marcarObjetivo(reserva);
				snd.play();
			}
			
		}
		reserva=Escenario.NO_RESERVAR;
	}
	
	public void finConexion(){}
	
	public void cicloPrincipal(){
		
		Posicion p[]=new Posicion[Escenario.NUM_JUGADORES];
		while(true){
		//Ubicar enemigos
		enemigos.toArray(p);
		escenario.actualizarEnemigos(p);
		//Revisar Objetivos
		for(int i=0;i<Escenario.NUM_OBJ;i++){
			if(((Boolean)objetivos.get(i)).booleanValue()&&
				objetivoRevisado[i]==false){
				escenario.marcarObjetivo(i);
				objetivoRevisado[i]=true;
				lst.play();
			}
		}
		try{Thread.sleep(Escenario.SLEEP+10);}catch(Exception ex){}
		}
	}
	
	public void actualizarPosicion(Vector3f transl,Point3d euler){
		pos.x=transl.x; 
		pos.y=transl.y;
		pos.z=transl.z;
		pos.ox=(float)euler.x;
		pos.oy=(float)euler.y;
		pos.oz=(float)euler.z;
	}
		
	public static void main(String args[]){
		Juego j=new Juego();
		try{
			j.iniciarJuego(args[0]);
			
		}catch(Exception ex){
			System.out.println("Direccion del servidor invalida");
			System.exit(-1);
		}
		j.cargarEscenario();
		try{
			if(args[1].equals("s"))
			j.actSonido();
		}catch(Exception ex){}
		j.cicloPrincipal();
	}
}