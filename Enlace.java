import java.util.*;
import java.io.*;
import java.net.*;


public class Enlace extends Thread
{
	private Socket socket = null;
    private Vector objetivos;
    private Vector enemigos;
	private Posicion pos;
	private Juego juego;
	
	public Enlace(Juego j,Posicion pos,Vector obj,Vector eng)
	{
		super("Enlace");
		juego=j;
		this.pos=pos;
		objetivos=obj;
		enemigos=eng;
		//this.setPriority(Thread.MIN_PRIORITY);
	}
	
	public int conectar(String servidor){

		try{
            socket=new Socket(servidor,5000);
        	InputStream entrada=socket.getInputStream();
        	int r=entrada.read();
        	return r;
        	        	
        }catch (UnknownHostException e) {
            System.out.println("Servidor desconocido");
            System.exit(1);
        }catch (IOException e) {
            System.out.println("No se pudo establecer la conexion con "+servidor);
			System.exit(1);
		}
		return -1;
	}

	public void run() {

	try {
	    
	    OutputStream salida=socket.getOutputStream();
	    InputStream entrada=socket.getInputStream();
				
		byte mensajeEntrada[]=new byte[512]; 
		int reserva;
		
		while(isAlive()){
	    	
	    	//Habla
	    	if(Juego.DEBUG)System.out.println("---SALIDA-----------");	
	    	salida.write(codificarMensaje());	    	
	    	
	    	//Escucha
	    	entrada.read(mensajeEntrada);
	    	if(Juego.DEBUG)System.out.println("---ENTRADA-----------");
	    	reserva=decodificarMensaje(mensajeEntrada);
	    	
	    	if(reserva!=Escenario.NO_RESERVAR){
	    		if(reserva==Escenario.YA_ESTABA_RESERVADO)
	    			juego.reservaAceptada(false);
	    		else if(reserva==Escenario.NO_ESTABA_RESERVADO)
	    			juego.reservaAceptada(true);
	    	}
	    	
	 	}
	    
	}catch(IOException e){
	    System.out.println("Fallo un hilo de cliente.");
	    e.printStackTrace();
	    juego.finConexion();
	}
    }
    
    private byte[] codificarMensaje(){
    
    	byte mensaje[]=null;
    
    	try{
			ByteArrayOutputStream arreglo;
			arreglo=new ByteArrayOutputStream();
			DataOutputStream escritor;
			escritor=new DataOutputStream(arreglo);
			
			synchronized(pos){
			escritor.writeFloat(pos.x);
			escritor.writeFloat(pos.y);
			escritor.writeFloat(pos.z);
			escritor.writeFloat(pos.ox);
			escritor.writeFloat(pos.oy);
			escritor.writeFloat(pos.oz);
			}
			
			if(Juego.DEBUG)System.out.println("Se puso pos="+pos);
			
			escritor.writeByte(juego.reserva);
			
			if(Juego.DEBUG)
			System.out.println("Se puso reserva="+juego.reserva);
						
			mensaje=arreglo.toByteArray();

		}catch(Exception e){
			System.out.println("Error codicando un mensaje");
			return null;
		}
		return mensaje;
    }
    
    private int decodificarMensaje(byte mensaje[]){

		int respuesta=Escenario.NO_RESERVAR;
		int i;
				
		try{
			ByteArrayInputStream arreglo;
			arreglo=new ByteArrayInputStream(mensaje);
			DataInputStream lector;
			lector=new DataInputStream(arreglo);
			
			if(Juego.DEBUG)
			System.out.println("l. del mens. "+mensaje.length);
			
			//Lee el estado de los objetivos
			synchronized(objetivos){
			boolean r;
			for(i=0;i<Escenario.NUM_OBJ;i++){
				if((r=lector.readBoolean())==true)
					objetivos.set(i,new Boolean(true));
				if(Juego.DEBUG)
				System.out.println("Se recibio obj("+i+")="+r);
			}						
			}
			
			//respuesta a la solicitud de reserva									
			respuesta=lector.readByte();
			if(Juego.DEBUG)
			System.out.println("La respuesta fue: "+respuesta);
			
			//posicion de los jugadores
			synchronized(enemigos){
			Posicion p;
			for(i=0;i<Escenario.NUM_JUGADORES;i++){
				p=(Posicion)enemigos.get(i);
				p.x=lector.readFloat();	
				p.y=lector.readFloat();
				p.z=lector.readFloat();
				p.ox=lector.readFloat();	
				p.oy=lector.readFloat();
				p.oz=lector.readFloat();
				if(Juego.DEBUG)
				System.out.println("La posicion de "+i+" es: "+p);
			}
			}
			
		}catch(Exception e){
			System.out.println("Error decodificando un mensaje");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return respuesta;
	}
	
}
