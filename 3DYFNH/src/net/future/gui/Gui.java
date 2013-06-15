package net.future.gui;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import net.future.material.ShaderLoader;

public class Gui 
{
	public int vboVertID;
	public int vboColorID;
	public int shader;
	public FloatBuffer verticesBuffer;
	public FloatBuffer colorsBuffer;
	
	public Gui()
	{
		this.shader=ShaderLoader.loadShaderPair("res/shaders/pixelTexture.vert", "res/shaders/pixelTexture.frag");
		
		this.verticesBuffer = BufferUtils.createFloatBuffer(2 * 4 * 2);
		this.colorsBuffer = BufferUtils.createFloatBuffer(3 * 4);
		
		this.vboVertID = GL15.glGenBuffers();
		this.vboColorID = GL15.glGenBuffers();
		
		verticesBuffer.flip();
		colorsBuffer.flip();
		{
			verticesBuffer.put(0);
			verticesBuffer.put(0);
			
			verticesBuffer.put(0);
			verticesBuffer.put(100);
			
			verticesBuffer.put(100);
			verticesBuffer.put(100);
			
			verticesBuffer.put(100);
			verticesBuffer.put(0);
			
			
			colorsBuffer.put(1);
			colorsBuffer.put(0);
			colorsBuffer.put(0);
			
			colorsBuffer.put(0);
			colorsBuffer.put(1);
			colorsBuffer.put(0);
			
			colorsBuffer.put(0);
			colorsBuffer.put(0);
			colorsBuffer.put(1);
			
			colorsBuffer.put(1);
			colorsBuffer.put(1);
			colorsBuffer.put(1);
		}
		verticesBuffer.flip();
		colorsBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		glVertexPointer(2, GL_FLOAT, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
		glColorPointer(3, GL_FLOAT, 0, 0L);
	}
	
	public void draw()
	{
		//// Set Data for Vertices ////
		glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
		glVertexPointer(2, GL_FLOAT, 0, 0L);

		//// Set Data For Color
		glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
		glColorPointer(3, GL_FLOAT, 0, 0L);

		//Unbind GL_ARRAY_BUFFER
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		//"Turn on" All Necessary client states
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		{

			//Actually Draw the object
			glDrawArrays(GL11.GL_QUADS, 0, 2*4);

		}
		//"Turn off" All Necessary client states
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
	}
}

