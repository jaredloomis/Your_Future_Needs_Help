package net.future.save;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import net.future.gameobject.GameObject;
import net.future.helper.Reference;
import net.future.material.SkyBox;
import net.future.model.OBJLoader;
import net.future.player.Player;
import net.future.world.World;

public class SaveManager 
{
	public static void saveGame(World w, File f)
	{
		try
		{
			//Create the specified file if it does not
			//already exist
			if (!f.exists())
				f.createNewFile();

			//Create a BufferedWriter to write to the file
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			//Write to file the player's info
			bw.write("p ");
			bw.append(Float.toString(w.player.position.x));
			bw.append(" ");
			bw.append(Float.toString(w.player.position.y));
			bw.append(" ");
			bw.append(Float.toString(w.player.position.z));
			bw.newLine();

			//Write each GameObject's info to file
			for(int i = 0; i < w.objects.size(); i++)
			{
				GameObject cur = w.objects.get(i);

				if(!(cur instanceof Player))
				{
					bw.append("go ");
					bw.append(cur.model.file.getPath());
					bw.append(" ");

					bw.append(Float.toString(cur.position.x));
					bw.append(" ");
					bw.append(Float.toString(cur.position.y));
					bw.append(" ");
					bw.append(Float.toString(cur.position.z));
					bw.append(" ");
					bw.append(Float.toString(cur.model.scale));
					bw.newLine();
				}
			}

			//Write skybox to file
			if(w.sky != null)
			{
				bw.append("sky ");
				bw.append(w.sky.f.getPath());
			}

			bw.close();
		}
		catch(Exception e){e.printStackTrace();}
	}

	public static void saveGame(World w, String f)
	{
		saveGame(w, new File(f));
	}

	/**
	 * Reads a save file and returns a world
	 * object with all the contents of the file
	 */
	public static World loadSave(File f)
	{
		World ret = new World();

		try
		{
			//Create a file reader to read each line of file
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;

			//While the next line contains text
			while ((line = reader.readLine()) != null)
			{
				//A line starting with "#" is commented out
				if(line.startsWith("#"))
					;
				//A line starting with an "go " indicates a GameObject
				if(line.startsWith("go "))
				{
					//Add a GameObject with specified model to world and move it to specified location
					GameObject g = new GameObject(ret, OBJLoader.completeLoadModel(
							new File(line.split(" ")[1]), 
							128, 								//Shininess
							Float.valueOf(line.split(" ")[5]), 	//Scale
							Reference.pixelTextureVert, 
							Reference.pixelTextureFrag));

					g.model.scale = Float.valueOf(line.split(" ")[5]);
					
					ret.add(g);
					ret.moveObj(g, new Vector3f(
							Float.valueOf(line.split(" ")[2]), 
							Float.valueOf(line.split(" ")[3]), 
							Float.valueOf(line.split(" ")[4])));

				}
				//A line starting with "p " indicates a player
				else if(line.startsWith("p"))
				{
					//Create player and move position and set the world's player
					Player p = new Player(ret, (float) Display.getWidth() / Display.getHeight(), 70);
					ret.add(p);
					ret.moveObj(p, new Vector3f(
							Float.valueOf(line.split(" ")[1]), 
							Float.valueOf(line.split(" ")[2]), 
							Float.valueOf(line.split(" ")[3])));
					ret.player = p;
				}
				else if(line.startsWith("sky "))
				{
					ret.sky = new SkyBox(new File(line.split(" ")[1]));
				}
			}
			reader.close();
		}
		catch(Exception e){e.printStackTrace();}

		return ret;
	}

	public static World loadSave(String loc)
	{
		return loadSave(new File(loc));
	}
}
