package net.future.physics.physpack;
import org.lwjgl.util.vector.Vector3f;

import net.future.gameobject.GameObject;

public class PhysStandard implements IPhysPack
{
	public float decay=0.0001f;
	public float gravity = 0.0026f;
	
	@Override
	public void update(GameObject o)
	{
		gravity(o, o.velocity);
	}
	
	public void gravity(GameObject o, Vector3f v)
	{
		//If at least one of the x, y, z velocity variables 
		//has a value other than 0
		if(Math.abs(v.x)+Math.abs(v.y)+Math.abs(v.z)>0)
		{
			if(!o.world.moveObj(o, new Vector3f(o.position.x, o.position.y+v.y*1, o.position.z)))
				o.grounded = true;
			else
				o.grounded = false;
			
			o.world.moveObj(o, new Vector3f(o.position.x+v.x*1, o.position.y, o.position.z));

			o.world.moveObj(o, new Vector3f(o.position.x, o.position.y, o.position.z+v.z*1));

			o.velocity = new Vector3f(v.x*decay, v.y, v.z*decay);
			//o.velocity = new Vector3f(0, 0, 0);
		}
		
		if(!o.grounded)
			o.velocity.y -= gravity;
		else
			o.velocity.y = 0;
	}
}
