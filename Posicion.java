public class Posicion
{
	public float x;
	public float y;
	public float z;
	public float ox;
	public float oy;
	public float oz;
		
	public Posicion(){
		this.x=0.0f;	
		this.y=0.0f;
		this.z=0.0f;
		this.ox=0.0f;	
		this.oy=0.0f;
		this.oz=0.0f;
	}
	
	public Posicion(float x,float y,float z,float ox,float oy,float oz){
		this.x=x;	
		this.y=y;
		this.z=z;
		this.ox=x;	
		this.oy=y;
		this.oz=z;
	}
	
	public String toString(){
		return new String(x+", "+y+", "+z+", "+ox+", "+oy+", "+oz);
	}
	
}
