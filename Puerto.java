import java.util.*;
import java.io.*;
import java.net.*;


public class Puerto extends Thread {
    
    private Socket socket = null;
    private Vector objetivos;
    private Vector jugadores;
    private Servidor servidor;
    private final int id;
    

    public Puerto(Socket socket,int id,Vector obj,Vector jug,Servidor s)
    {
		super("Cliente "+id);
		this.socket = socket;
		this.id=id;
		objetivos=obj;
		jugadores=jug;
		servidor=s;
    }

    public void run() {

	try {
	    
	    OutputStream salida=socket.getOutputStream();
	    InputStream entrada=socket.getInputStream();
	    
	    
	    salida.write(id);	    
		
		byte mensajeEntrada[]=new byte[28];
		int reserva;
		byte respuesta;
		
		
		while(isAlive()){
	    	//Escucha
	    	entrada.read(mensajeEntrada);
	    	if(Servidor.DEBUG)System.out.println("---ENTRADA-----------");
	    	reserva=decodificarMensaje(mensajeEntrada);
	    		    	
  	    	if(reserva!=Escenario.NO_RESERVAR){
	    		synchronized(objetivos){
	    			if(((Boolean)objetivos.get(reserva)).booleanValue()==false){
	    				objetivos.set(reserva,new Boolean(true));
	    				respuesta=Escenario.NO_ESTABA_RESERVADO;
	    				servidor.adicionarPunto(id);
	    			}
	    			else{
	    				respuesta=Escenario.YA_ESTABA_RESERVADO;
	    			}
	    		}
	    	}else respuesta=Escenario.NO_RESERVAR;
	 		
	 		try{Thread.sleep(Escenario.SLEEP);}catch(Exception exx){}
	 		
	 		//Responde
	 		if(Servidor.DEBUG)System.out.println("---SALIDA-----------");
	 		salida.write(codificarMensaje(respuesta));
	 	}
	    
	}catch(IOException e){
	    
	    synchronized(jugadores){
	    	jugadores.set(id,new Posicion());
	    }	    
	    System.out.println("Se perdio la conexion con el cliente:"+id);
	    //e.printStackTrace();
	}
    }
    
    private int decodificarMensaje(byte mensaje[]){
    
    	Posicion pos=new Posicion();
    	int reserva=Escenario.NO_RESERVAR;
    
    	try{
			ByteArrayInputStream arreglo;
			arreglo=new ByteArrayInputStream(mensaje);
			DataInputStream lector;
			lector=new DataInputStream(arreglo);
			
			if(Servidor.DEBUG)
			System.out.println("l. del mens. "+mensaje.length);
			
			pos.x=lector.readFloat();
			pos.y=lector.readFloat();
			pos.z=lector.readFloat();
			pos.ox=lector.readFloat();
			pos.oy=lector.readFloat();
			pos.oz=lector.readFloat();
			
			if(Servidor.DEBUG)
			System.out.println("Pos de "+id+" "+pos);
			
			synchronized(jugadores){
	    		jugadores.set(id,pos);
	    	}
			
			reserva=lector.readByte();
			
			if(Servidor.DEBUG)
			System.out.println("Reserv: "+reserva);

		}catch(Exception e){
			System.out.println("Error decodificando un mensaje");
			e.printStackTrace();
			System.exit(-1);
		}
		return reserva;
    }
    
    private byte[] codificarMensaje(byte reserva){

		byte mensaje[]=null;
		int i;

		try{
			ByteArrayOutputStream arreglo;
			arreglo=new ByteArrayOutputStream();
			DataOutputStream escritor;
			escritor=new DataOutputStream(arreglo);
			
			//Escribe el estado de los objetivos
			for(i=0;i<Escenario.NUM_OBJ;i++){
				escritor.writeBoolean(
					((Boolean)objetivos.get(i)).booleanValue());
			}						
			
			//respuesta a la solicitud de reserva									
			escritor.writeByte(reserva);
			
			//posicion de los jugadores
			synchronized(jugadores){
						
			//escritor.writeInt(Escenario.NUM_JUGADORES);
			
			Posicion p;
			for(i=0;i<Escenario.NUM_JUGADORES;i++){
				p=(Posicion)jugadores.get(i);
				escritor.writeFloat(p.x);	
				escritor.writeFloat(p.y);
				escritor.writeFloat(p.z);
				escritor.writeFloat(p.ox);	
				escritor.writeFloat(p.oy);
				escritor.writeFloat(p.oz);
				
				if(Servidor.DEBUG)
				System.out.println("La posicion de "+i+" es: "+p);				
			}
			}
			
			mensaje=arreglo.toByteArray();

		}catch(Exception e){
			System.out.println("Error codificando un mensaje");
			return null;
		}
		
		return mensaje;
	}
	
}
