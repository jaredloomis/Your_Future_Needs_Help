package net.future.player;
import net.future.gameobject.GameObject;
import net.future.helper.Reference;
import net.future.world.World;

public class ServerPlayer extends GameObject
{
	public int id;
	
	public ServerPlayer(World world) 
	{
		super(world, Reference.bunny);
	}
}
