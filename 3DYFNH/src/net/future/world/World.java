package net.future.world;
import java.util.ArrayList;
import java.util.List;
import net.future.gameobject.GameObject;
import net.future.gameobject.Light;
import net.future.material.SkyBox;
import net.future.physics.Physics;
import net.future.player.Player;
import net.future.player.ServerPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class World 
{
	public Player player;
	public List<ServerPlayer> players;
	public List<GameObject> objects;
	public Light[] lights;
	public Physics physics;
	public boolean paused;
	public SkyBox sky;
	
	public GameObject cube;

	
	public World()
	{
		this.players = new ArrayList<ServerPlayer>();
		this.objects = new ArrayList<GameObject>();
		this.lights = new Light[6];
		this.physics = new Physics(0.0026f);
		this.paused = false;

		//TODO
		/*cube = new ObjectBunny(this);
		cube.position = new Vector3f(5, 5, -3);
		cube.name = "bunny";
		//this.add(cube);

		GameObject test = new GameObject(this, Reference.test);
		test.position = new Vector3f(-3, -5, -3);
		test.name = "test";
		this.add(test);*/

		Light l1 = new Light(this, GL11.GL_LIGHT0, new float[]{0.5f,0.5f,0.5f,1}, new float[]{0.5f,0.5f,0.5f,1}, new float[]{0.01f, 0.01f, 0.01f, 1});
		l1.init();
		this.lights[0] = l1;
	}

	/**
	 * Safely adds object
	 */
	public void add(GameObject obj)
	{
		if(canPlaceHere(obj, (int)obj.position.x, (int)obj.position.y, (int)obj.position.z))
		{
			this.objects.add(obj);
		}
	}

	/**
	 * Safely moves objects by checking
	 * if canPlaceHere
	 */
	public boolean moveObj(GameObject obj, Vector3f pos)
	{
		Vector3f initialPosition = new Vector3f(obj.position.x, obj.position.y, obj.position.z);

		//Placing the object first and then testing to see if it
		//is allowed to be there seems to be faster.
		obj.position = pos;
		if(!canPlaceHere(obj, pos.x, pos.y, pos.z))
		{
			obj.position = initialPosition;
			return false;
		}
		return true;
	}

	/**
	 * Makes sure Object does not
	 * collide with other objects
	 */
	public boolean canPlaceHere(GameObject obj, float x, float y, float z)
	{
		if(obj.model != null)
		{
			for(int i = 0; i < this.objects.size(); i++)
			{
				GameObject cur = this.objects.get(i);

				if(cur != obj)
				{
					if(Physics.willIntersect(obj, cur, new Vector3f(x, y, z)))
					{
						return false;
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * @return Custom ID given to sp
	 */
	public int addServerPlayer(ServerPlayer sp)
	{
		this.players.add(sp);
		return this.players.size()-1;
	}
}

