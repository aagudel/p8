/**********************************************************
  Copyright (C) 2001 	Daniel Selman

  First distributed with the book "Java 3D Programming"
  by Daniel Selman and published by Manning Publications.
  http://manning.com/selman

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation, version 2.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  The license can be found on the WWW at:
  http://www.fsf.org/copyleft/gpl.html

  Or by writing to:
  Free Software Foundation, Inc.,
  59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

  Authors can be contacted at:
  Daniel Selman: daniel@selman.org

  If you make changes you think others would like, please 
  contact one of the authors or someone at the 
  www.j3d.org web site.
**************************************************************/

import javax.media.j3d.*;
import javax.vecmath.*;

public class Euler
{
	public static Point3d getEulerRotation( Transform3D t3d )
	{
		Point3d Rotation = new Point3d( );

		Matrix3d m1 = new Matrix3d( );
		t3d.get( m1 );

		// extract the rotation angles from the upper 3x3 rotation
		// component of the 4x4 transformation matrix
		Rotation.y = -java.lang.Math.asin( m1.getElement( 2, 0 ) );
		double c = java.lang.Math.cos( Rotation.y );
		double tRx, tRy, tRz;

		if( java.lang.Math.abs( Rotation.y ) > 0.00001 )
		{
			tRx = m1.getElement( 2, 2 ) / c;
			tRy = -m1.getElement( 2, 1 )  / c;

			Rotation.x = java.lang.Math.atan2( tRy, tRx );

			tRx = m1.getElement( 0, 0 ) / c;
			tRy = -m1.getElement( 1, 0 ) / c;

			Rotation.z = java.lang.Math.atan2( tRy, tRx );
		}
		else
		{
			Rotation.x  = 0.0;

			tRx = m1.getElement( 1, 1 );
			tRy = m1.getElement( 0, 1 );

			Rotation.z = java.lang.Math.atan2( tRy, tRx );
		}

		Rotation.x = -Rotation.x;
		Rotation.z = -Rotation.z;

		// now try to ensure that the values are positive by adding 2PI if necessary...		
		if( Rotation.x < 0.0 )
			Rotation.x += 2 * java.lang.Math.PI;

		if( Rotation.y < 0.0 )
			Rotation.y += 2 * java.lang.Math.PI;

		if( Rotation.z < 0.0 )
			Rotation.z += 2 * java.lang.Math.PI;

		return Rotation;
	}
}
