
import java.util.*;
import java.io.*;
import java.net.*;

public class Servidor{
	
	Vector objetivos;
	Vector jugadores;
	int clientes;
	int puntaje[];
	public static boolean DEBUG=false;
	
	
	Servidor(){
		
		objetivos=new Vector(Escenario.NUM_OBJ);
		jugadores=new Vector(20);
		clientes=0;
		
		int i;
		for(i=0;i<Escenario.NUM_JUGADORES;i++)
			jugadores.add(new Posicion());
		
		for(i=0;i<Escenario.NUM_OBJ;i++)		
			objetivos.add(new Boolean(false));		
		
		puntaje=new int[Escenario.NUM_JUGADORES];
	}	
    
    public void escucharClientes()
    {
      	ServerSocket s=null;
        boolean escuchando=true;

        try{
        	s=new ServerSocket(5000);
        
        }catch (IOException e) {
            System.err.println("Fallo la apertura del puerto");
            System.exit(-1);
    	}
        
        System.out.println("Esperando clientes...");

		try{
        while(escuchando){
	    	
	    	if(clientes<Escenario.NUM_JUGADORES){
	    		Puerto p;
	    		p=new Puerto(s.accept(),clientes,objetivos,jugadores,this);
	    		p.start();
	    		System.out.println("Ingreso el jugador "+clientes);
	    		clientes++;
	    	}else{
	    	
	    	System.out.println("Se completo el numero de jugadores ("+
	    						Escenario.NUM_JUGADORES+")");	
	    		Thread.sleep(100000);
	    	}
	    	
		}
		s.close();
		}catch(Exception ex){
			System.out.println("Error esperando los clientes");
		}
    }
    
    public void adicionarPunto(int id){
    	puntaje[id]++;
    	System.out.print("PUNTAJE ");
    	for(int i=0;i<clientes;i++)
    		System.out.print("j"+i+":"+puntaje[i]+", ");
    	System.out.println();
    }

    public static void main(String[] args){
    	Servidor servidor=new Servidor();
    	servidor.escucharClientes();    	
    }
}
