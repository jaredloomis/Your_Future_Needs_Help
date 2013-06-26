package net.future.material;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;


public class SkyBox 
{
	public Texture texture;
	public File f;
	
	public SkyBox(File f)
	{
		this.f = f;
		texture = MyTextureLoader.getTexture(f.getPath());
	}
	
	public void render(Vector3f v)
	{
		float x = v.x; float y = v.y; float z = v.z;
		
		int boxSize = 1;
		
		glDisable(GL_LIGHTING);
		glDepthMask(false);
		texture.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		glBegin(GL_QUADS);
		{
			//TOP
			//glColor3f(1, 0, 0);
			glTexCoord2f(1, 1);
			glVertex3f(x+boxSize, y+boxSize, z+boxSize);
			glTexCoord2f(0, 1);
			glVertex3f(x-boxSize, y+boxSize, z+boxSize);
			glTexCoord2f(0, 0);
			glVertex3f(x-boxSize, y+boxSize, z-boxSize);
			glTexCoord2f(1, 0);
			glVertex3f(x+boxSize, y+boxSize, z-boxSize);
			
			//BOTTOM
			//glColor3f(0, 1, 0);
			glTexCoord2f(1, 1);
			glVertex3f(x+boxSize, y-boxSize, z+boxSize);
			glTexCoord2f(1, 0);
			glVertex3f(x+boxSize, y-boxSize, z-boxSize);
			glTexCoord2f(0, 0);
			glVertex3f(x-boxSize, y-boxSize, z-boxSize);
			glTexCoord2f(0, 1);
			glVertex3f(x-boxSize, y-boxSize, z+boxSize);
			
			//SIDE1
			//glColor3f(0, 0, 1);
			glTexCoord2f(0, 1);
			glVertex3f(x-boxSize, y-boxSize, z+boxSize);
			glTexCoord2f(0, 0);
			glVertex3f(x-boxSize, y-boxSize, z-boxSize);
			glTexCoord2f(1, 0);
			glVertex3f(x-boxSize, y+boxSize, z-boxSize);
			glTexCoord2f(1, 1);
			glVertex3f(x-boxSize, y+boxSize, z+boxSize);
			
			//SIDE2
			//glColor3f(1, 1, 0);
			glTexCoord2f(0, 1);
			glVertex3f(x+boxSize, y-boxSize, z+boxSize);
			glTexCoord2f(1, 1);
			glVertex3f(x+boxSize, y+boxSize, z+boxSize);
			glTexCoord2f(1, 0);
			glVertex3f(x+boxSize, y+boxSize, z-boxSize);
			glTexCoord2f(0, 0);
			glVertex3f(x+boxSize, y-boxSize, z-boxSize);
			
			//SIDE3
			//glColor3f(1, 0, 1);
			glTexCoord2f(0, 0);
			glVertex3f(x-boxSize, y-boxSize, z-boxSize);
			glTexCoord2f(1, 0);
			glVertex3f(x+boxSize, y-boxSize, z-boxSize);
			glTexCoord2f(1, 1);
			glVertex3f(x+boxSize, y+boxSize, z-boxSize);
			glTexCoord2f(0, 1);
			glVertex3f(x-boxSize, y+boxSize, z-boxSize);
			
			
			//SIDE4
			//glColor3f(0, 1, 1);
			glTexCoord2f(0, 0);
			glVertex3f(x-boxSize, y-boxSize, z+boxSize);
			glTexCoord2f(0, 1);
			glVertex3f(x-boxSize, y+boxSize, z+boxSize);
			glTexCoord2f(1, 1);
			glVertex3f(x+boxSize, y+boxSize, z+boxSize);
			glTexCoord2f(1, 0);
			glVertex3f(x+boxSize, y-boxSize, z+boxSize);
		}
		glEnd();
		glEnable(GL_LIGHTING);
		glDepthMask(true); 
	}
}
