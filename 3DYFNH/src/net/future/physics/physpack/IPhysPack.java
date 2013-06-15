package net.future.physics.physpack;
import net.future.gameobject.GameObject;

public interface IPhysPack 
{
	/**
	 * Called by GameObject when physics is needed
	 */
	public void update(GameObject o);
}
