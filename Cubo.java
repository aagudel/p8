import javax.media.j3d.*;
import javax.vecmath.*; 


class Cubo extends Shape3D
{
	static IndexedQuadArray indexedCube = new IndexedQuadArray(8,
	IndexedQuadArray.COORDINATES|IndexedQuadArray.NORMALS, 24);

	static Point3f[] cubeCoordinates = { new Point3f( 1.0f, 1.0f, 1.0f),
					      new Point3f(-1.0f, 1.0f, 1.0f),
					      new Point3f(-1.0f,-1.0f, 1.0f),
					      new Point3f( 1.0f,-1.0f, 1.0f),
					      new Point3f( 1.0f, 1.0f,-1.0f),
					      new Point3f(-1.0f, 1.0f,-1.0f),
					      new Point3f(-1.0f,-1.0f,-1.0f),
					      new Point3f( 1.0f,-1.0f,-1.0f)};
	static Vector3f[] cubeNormals= {new Vector3f( 0.0f, 0.0f, 1.0f),
               			 new Vector3f( 0.0f, 0.0f,-1.0f),
               			 new Vector3f( 1.0f, 0.0f, 0.0f),
               			 new Vector3f(-1.0f, 0.0f, 0.0f),
               			 new Vector3f( 0.0f, 1.0f, 0.0f),
               			 new Vector3f( 0.0f,-1.0f, 0.0f)};
	static int cubeCoordIndices[] = {0,1,2,3,7,6,5,4,0,3,7,4,5,6,2,1,0,4,5,1,6,7,3,2};
	static int cubeNormalIndices[] = {0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5};
	Appearance ap;
	
	static Color3f emissiveColour = new Color3f(0.0f,0.0f,0.0f);
	static Color3f specularColour = new Color3f(1.0f,1.0f,1.0f);
    static float shininess = 20.0f;
	
	static{
	indexedCube.setCoordinates(0, cubeCoordinates);
	indexedCube.setNormals(0,cubeNormals);
	indexedCube.setCoordinateIndices(0, cubeCoordIndices);
	indexedCube.setNormalIndices(0, cubeNormalIndices);
	
	/*	
	PolygonAttributes pa=new PolygonAttributes();
	pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
	ap.setPolygonAttributes(pa);*/
		
	}	
	
	public Cubo(){
		super(indexedCube);
		setAppearanceOverrideEnable(true);  
		ap=new Appearance();
		//setAppearance(ap);
		ap.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		rojo();
		
	}
	
	public void rojo()
	{
		Color3f ambientColour = new Color3f(1.0f,0.0f,0.0f);
		Color3f diffuseColour = new Color3f(1.0f,0.0f,0.0f);
	
    	ap.setMaterial(new Material(ambientColour,emissiveColour,
           			diffuseColour,specularColour,shininess));
		setAppearance(ap);
 	}
}