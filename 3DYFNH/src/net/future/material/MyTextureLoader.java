package net.future.material;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

public class MyTextureLoader 
{
	public static Texture getTexture(String location)
	{
		Texture texture = null;
		try 
		{
			if(location.endsWith(".png"))
				texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(location)));
			else if(location.endsWith(".jpg") || location.endsWith(".jpeg"))
				texture = TextureLoader.getTexture("JPG", new FileInputStream(new File(location)));
			else
				System.err.println("Texture File At " + location + " is not a supported image type");
			return texture;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		return null;
	}
	
	public static void setupCubeMap(int texture) 
	{
	    glActiveTexture(GL_TEXTURE0);
	    glEnable(GL_TEXTURE_CUBE_MAP);
	    IntBuffer tmp = BufferUtils.createIntBuffer(1);
	    tmp.put(texture);
	    glGenTextures(tmp);
	    glBindTexture(GL_TEXTURE_CUBE_MAP, texture);
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST); 
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
	}
}