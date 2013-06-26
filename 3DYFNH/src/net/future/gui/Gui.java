package net.future.gui;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;

import net.future.material.ShaderLoader;

public class Gui 
{
	public int vboVertID, vboColorID, shader;
	public FloatBuffer verticesBuffer, colorsBuffer;
	public float[] rawVertices, rawColors;
	
	public Gui(float[] vertices, float[] colors)
	{
		this.shader=ShaderLoader.loadShaderPair("res/shaders/pixelTexture.vert", "res/shaders/pixelTexture.frag");
		
		this.verticesBuffer = BufferUtils.createFloatBuffer(vertices.length*500);
		this.colorsBuffer = BufferUtils.createFloatBuffer(colors.length*500);
		
		this.vboVertID = GL15.glGenBuffers();
		this.vboColorID = GL15.glGenBuffers();
		
		verticesBuffer.flip();
		colorsBuffer.flip();
		{
			for(int i = 0; i < vertices.length; i++)
			{
				verticesBuffer.put(vertices[i]);
			}
			for(int i = 0; i < colors.length; i++)
			{
				colorsBuffer.put(colors[i]);
			}
		}
		verticesBuffer.flip();
		colorsBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		//glVertexPointer(2, GL_FLOAT, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboColorID);
		glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
		//glColorPointer(3, GL_FLOAT, 0, 0L);
	}
	
	public void draw()
	{
		//// Ready 2D ////
		glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    GLU.gluOrtho2D(0.0f, Display.getWidth(), Display.getHeight(), 0.0f);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	    glTranslatef(0.375f, 0.375f, 0.0f);
	    glDisable(GL_DEPTH_TEST);
	    
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
			glDrawArrays(GL11.GL_QUADS, 0, rawVertices.length);

		}
		//"Turn off" All Necessary client states
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		
		/*
		//// Ready 3D ////
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	    glMatrixMode(GL_PROJECTION);

	    glLoadIdentity();
	    //GLU.gluPerspective(45f, Display.getWidth() / Display.getHeight(), 0.1f);

	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();

	    glDepthFunc(GL_LEQUAL);
	    glEnable(GL_DEPTH_TEST);*/

	}
}

