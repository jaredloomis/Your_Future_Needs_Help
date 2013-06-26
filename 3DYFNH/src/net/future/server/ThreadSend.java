package net.future.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import net.future.player.Player;

public class ThreadSend extends Thread 
{
	ObjectOutputStream out;
	Player p;
	
	public ThreadSend(Player w, ObjectOutputStream o){
		p = w;
		out = o;
		start();
	}
	
	public void run()
	{
		while(true)
		{
			try 
			{
				out.writeObject(""+p.position.x);
				out.writeObject(""+p.position.y);
				out.writeObject(""+p.position.z);
				out.flush();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
