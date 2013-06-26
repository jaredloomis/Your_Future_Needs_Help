package net.future.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import net.future.player.ServerPlayer;

public class ThreadReceive extends Thread 
{

	ObjectInputStream in;
	ServerPlayer p;
	
	public ThreadReceive(ServerPlayer w, ObjectInputStream i)
	{
		p = w;
		in = i;
		p.id = w.world.addServerPlayer(p);
		
		start();
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				p.position.x = Float.parseFloat((String)in.readObject());
				p.position.y = -Float.parseFloat((String)in.readObject());
				p.position.z = Float.parseFloat((String)in.readObject());
			} 
			catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
