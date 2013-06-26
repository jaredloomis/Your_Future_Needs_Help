package net.future.physics.physpack;
import org.lwjgl.util.vector.Vector3f;

import net.future.GameLoop;
import net.future.gameobject.GameObject;
import net.future.player.Player;

public class PhysPlayer implements IPhysPack
{
	public float decay=0.85f;
	public float gravity = 0.003f;

	@Override
	public void update(GameObject o) 
	{
		Vector3f v = o.velocity;
		this.velocity(o, v);
	}

	public void velocity(GameObject o, Vector3f v)
	{	
		int delta = (int)GameLoop.delta;

		//If the player can fly
		if(((Player)o).cam.fly)
		{
			//If at least one of the x, y, z velocity variables 
			//has a value other than 0
			if(Math.abs(v.x)+Math.abs(v.y)+Math.abs(v.z)>=0)
			{
				if(v.x != 0)
					o.world.moveObj(o, new Vector3f(o.position.x+v.x, o.position.y, o.position.z));
				if(v.y != 0)
					o.world.moveObj(o, new Vector3f(o.position.x, o.position.y+v.y, o.position.z));
				if(v.z != 0)
					o.world.moveObj(o, new Vector3f(o.position.x, o.position.y, o.position.z+v.z));

				o.velocity = new Vector3f(v.x*(decay), v.y*(decay), v.z*(decay));
			}
		}
		//Player cannot fly
		else
		{
			//If at least one of the x, y, z velocity variables 
			//has a value other than 0
			if(Math.abs(v.x)+Math.abs(v.y)+Math.abs(v.z)>0)
			{
				o.world.moveObj(o, new Vector3f(o.position.x+v.x, o.position.y, o.position.z));

				if(!o.world.moveObj(o, new Vector3f(o.position.x, o.position.y+v.y, o.position.z)))
					o.grounded = true;
				else
					o.grounded = false;

				o.world.moveObj(o, new Vector3f(o.position.x, o.position.y, o.position.z+v.z));

				o.velocity = new Vector3f(v.x*decay, v.y, v.z*decay);
			}
			if(!o.grounded)
				o.velocity.y -= gravity*(delta/16);
			else
				o.velocity.y = 0;
		}

		//Round Velocities to 0
		if(o.velocity.x < 0.001f && o.velocity.x > -0.001f)
			o.velocity.x = 0;
		if(o.velocity.y < 0.001f && o.velocity.y > -0.001f)
			o.velocity.y = 0;
		if(o.velocity.z < 0.001f && o.velocity.z > -0.001f)
			o.velocity.z = 0;
	}
}
