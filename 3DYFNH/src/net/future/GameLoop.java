package net.future;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import net.future.audio.AudioManager;
import net.future.gameobject.GameObject;
import net.future.gui.GuiHelper;
import net.future.helper.FontHelper;
import net.future.model.Model;
import net.future.player.Player;
import net.future.save.SaveManager;
import net.future.world.World;
import org.lwjgl.opengl.GL12;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.UnicodeFont;

public class GameLoop
{
	private World w;
	private static int lastFPS=(int) getTime();
	public static boolean run;

	//Just a placeholder used by getFPS. Not actual FPS
	private static int fps = 0;

	//Actual FPS calculated every second
	private static int realFPS = 0;

	public UnicodeFont font;
	private static float lastFrame = 0;
	private FloatBuffer perspectiveProjectionMatrix = BufferUtils.createFloatBuffer(16);
	private FloatBuffer orthographicProjectionMatrix = BufferUtils.createFloatBuffer(16);
	public static float delta = getTime();

	/**
	 * Initial set-up, called once from Driver class
	 */
	public void initialize()
	{
		run = true;

		//Init the time variables
		lastFrame=getTime();
		getTime();
		getDelta();

		this.setUpDisplay();
		this.setUpLighting();
		this.setUpFog();
		this.setUpWorld();

		//Makes the default screen color black
		glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		glClearDepth(1.0f);

		//Make mouse invisible
		Mouse.setGrabbed(true);

		//Initialize and set up the font system
		this.font = FontHelper.getWhiteArial();
	}

	/**
	 * Initializes the main window
	 */
	private void setUpDisplay()
	{
		//Sets up a window
		try
		{
			Display.setDisplayMode(new DisplayMode(1280,720));
			//TODO Allow VSync to be enabled / disabled
			Display.setVSyncEnabled(true);
			Display.setTitle("Your Future Needs Help v0.3");
			Display.setResizable(false);
			Display.create();
		}
		catch (LWJGLException ex)
		{
			Logger.getLogger(YFNH.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Enables the lighting system and sets up other
	 * display features of OpenGL
	 */
	private void setUpLighting() 
	{
		//Makes lighting smooth instead of flat
		//Not needed when using shaders
		//glShadeModel(GL_SMOOTH);

		//Allow alpha in textures
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//Automatically normalize normals
		glEnable(GL12.GL_RESCALE_NORMAL);

		//Tell OpenGL to use the most correct, or highest quality, option when deciding
		//on Gl_PERSEPECTIVE_CORRECTION details. Choices are GL_FASTEST and GL_DONT_CARE, GL_NICEST
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);

		//Enables 3D and depth. Also enables
		//Hardware acceleration
		glEnable(GL_DEPTH_TEST);

		//Store values in pixel data if depth
		//is less than or equal.
		glDepthFunc(GL_LEQUAL);

		//Enable lighting in general
		glEnable(GL_LIGHTING);

		//Do not render the backs of faces.
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		//Enable colors on faces / objects, only needed for
		//immediate mode (@deprecated)
		//glEnable(GL_COLOR_MATERIAL);
		//glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	private void setUpFog()
	{
		glEnable(GL_FOG); 
		{
			FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
			fogColor.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();

			int fogMode = GL_EXP;
			glFogi(GL_FOG_MODE, fogMode);
			glFog(GL_FOG_COLOR, fogColor);
			glFogf(GL_FOG_DENSITY, 0.01f);
			glHint(GL_FOG_HINT, GL_FASTEST);
			glFogf(GL_FOG_START, 1.0f);
			glFogf(GL_FOG_END, 5.0f);
		}
	}

	private void setUpWorld()
	{
		w = SaveManager.loadSave("res/saves/saveOne.dat");

		w.player.cam.applyPerspectiveMatrix();

		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		w.player.cam.applyPerspectiveMatrix();
	}

	/**
	 * Function called from driver class
	 */
	public void gameLoop()
	{
		//Main Update Loop continues until the window is closed.
		while(run && !Display.isCloseRequested())
		{
			this.render();
			this.update();

			//Sync the GPU with the CPU, basically required
			Display.update();
		}
		cleanUp();
	}

	/**
	 * Main method of the game.
	 * Moves and draws all objects.
	 */
	public void worldUpdate()
	{	
		//Update the single player
		if(w.player!=null)
			w.player.playerUpdate(delta);

		for(int i = 0; i < w.objects.size(); i++)
		{
			GameObject cur = w.objects.get(i);

			glPushMatrix();
			{
				if(cur.model != null)
				{
					//Moves the obj
					if(cur instanceof Player)
						;//glTranslatef(cur.position.x, cur.position.y, cur.position.z);
					else
					{
						//WHAT WAY TO RENDER
						int renderType = 1;

						//renderType 0 means use display lists (DEPRECATED) NOT EVEN POSSIBLE ANYMORE
						if(renderType==0)
						{
						}
						//renderType 1 means use VBOs (Faster, better, cooler)
						else if(renderType==1)
						{	
							Model m = cur.model;

							//Move the object
							glTranslatef(cur.position.x, cur.position.y, cur.position.z);

							glRotatef(cur.rotation.x, 1, 0, 0);
							glRotatef(cur.rotation.y, 0, 1, 0);
							glRotatef(cur.rotation.z, 0, 0, 1);

							//Set the shininess
							glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);

							//Set the color
							//glColor3f(cur.color[0], cur.color[1], cur.color[2]);

							//Use the model's shader
							glUseProgram(m.shader);


							int texIDVar = -1;

							if(m.textures!=null)
							{
								//OpenGL can't handle more than 7 GL_TEXTUREX s using this method
								//if(m.textures.size()<=7)
								for(int j = 0; j < m.textures.size(); j++)
								{
									GL13.glActiveTexture(GL13.GL_TEXTURE0+j);
									GL11.glBindTexture(GL11.GL_TEXTURE_2D, m.textures.get(j).getTextureID());
								}

								//Set the shader's "textures[10]" array equal to {0, 1, 2, 3, 4, ect.}
								//Cannot set as constant in shader because sampler2D s must be set by
								//Java code as a Uniform
								for(int j = 0; j < m.textures.size(); j++)
								{
									//Find the "memory address" of textures sampler2D's specified index uniform in shader
									int loc2 = glGetUniformLocation(m.shader, "textures["+j+"]");

									//Set it to current number
									glUniform1i(loc2, j);
								}

								//Find the "memory address" of textureID attribute in shader
								texIDVar = glGetAttribLocation(m.shader, "textureID");

								//// Set Data for Texture IDs ////
								glBindBuffer(GL_ARRAY_BUFFER, m.vboTexIDHandle);
								glVertexAttribPointer(texIDVar, 1, GL_FLOAT, false, 0, 0);

								//Enable the VBO to use this attribute
								glEnableVertexAttribArray(texIDVar);

								//// Set Data for Texture Coordinates ////
								glBindBuffer(GL_ARRAY_BUFFER, m.vboTexHandle);
								glTexCoordPointer(2, GL_FLOAT, 0, 0L);

								//Enable the VBO to use texture coords
								glEnableClientState(GL_TEXTURE_COORD_ARRAY);
							}

							//// Set Data for Vertices ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboVertexHandle);
							glVertexPointer(3, GL_FLOAT, 0, 0L);

							//// Set Data for Normals ////
							glBindBuffer(GL_ARRAY_BUFFER, m.vboNormalHandle);
							glNormalPointer(GL_FLOAT, 0, 0L);

							//// Set Data For Color
							glBindBuffer(GL_ARRAY_BUFFER, m.vboColorHandle);
							glColorPointer(3, GL_FLOAT, 0, 0L);

							//Unbind GL_ARRAY_BUFFER
							glBindBuffer(GL_ARRAY_BUFFER, 0);

							//"Turn on" All Necessary client states
							glEnableClientState(GL_VERTEX_ARRAY);
							glEnableClientState(GL_COLOR_ARRAY);
							glEnableClientState(GL_NORMAL_ARRAY);
							{

								//Actually Draw the object
								glDrawArrays(GL_TRIANGLES, 0, m.faces.size() * 3);

							}
							//"Turn off" All Necessary client states
							glDisableClientState(GL_VERTEX_ARRAY);
							if(m.textures!=null)
							{
								glDisableClientState(GL_TEXTURE_COORD_ARRAY);
								glDisableVertexAttribArray(texIDVar);
							}
							glDisableClientState(GL_COLOR_ARRAY);
							glDisableClientState(GL_NORMAL_ARRAY);

							//Unbind all textures
							//if(m.textures.size()<=7)
							for(int j = 0; j < m.textures.size(); j++)
							{
								GL13.glActiveTexture(GL13.GL_TEXTURE0+j);
								GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
							}
						}

						//Resets the array buffer, shader, texture, and color to default values
						glBindBuffer(GL_ARRAY_BUFFER, 0);
						glUseProgram(0);
						glBindTexture(GL_TEXTURE_2D, 0);
						glColor4f(1, 1, 1, 1);

						glLoadIdentity();
					}
				}
			}
			glPopMatrix();

			//If game is paused do not perform any logic updates
			if(!w.paused)
				cur.update();
		}

		//Render all players
		for(int i = 0; i < w.players.size(); i++)
		{
			GameObject cur = w.players.get(i);

			glPushMatrix();
			{
				//renderType 1 means use VBOs (Faster, better, cooler)
				{	
					Model m = cur.model;

					//Move the object
					glTranslatef(cur.position.x, cur.position.y, cur.position.z);

					glRotatef(cur.rotation.x, 1, 0, 0);
					glRotatef(cur.rotation.y, 0, 1, 0);
					glRotatef(cur.rotation.z, 0, 0, 1);

					//Set the shininess
					glMaterialf(GL_FRONT, GL_SHININESS, m.shininess);

					//Set the color
					//glColor3f(cur.color[0], cur.color[1], cur.color[2]);

					//Use the model's shader
					glUseProgram(m.shader);


					int texIDVar = -1;

					if(m.textures!=null)
					{
						//OpenGL can't handle more than 7 GL_TEXTUREX s using this method
						//if(m.textures.size()<=7)
						for(int j = 0; j < m.textures.size(); j++)
						{
							GL13.glActiveTexture(GL13.GL_TEXTURE0+j);
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, m.textures.get(j).getTextureID());
						}

						//Set the shader's "textures[10]" array equal to {0, 1, 2, 3, 4, ect.}
						//Cannot set as constant in shader because sampler2D s must be set by
						//Java code as a Uniform
						for(int j = 0; j < m.textures.size(); j++)
						{
							//Find the "memory address" of textures sampler2D's specified index uniform in shader
							int loc2 = glGetUniformLocation(m.shader, "textures["+j+"]");

							//Set it to current number
							glUniform1i(loc2, j);
						}

						//Find the "memory address" of textureID attribute in shader
						texIDVar = glGetAttribLocation(m.shader, "textureID");

						//// Set Data for Texture IDs ////
						glBindBuffer(GL_ARRAY_BUFFER, m.vboTexIDHandle);
						glVertexAttribPointer(texIDVar, 1, GL_FLOAT, false, 0, 0);

						//Enable the VBO to use this attribute
						glEnableVertexAttribArray(texIDVar);

						//// Set Data for Texture Coordinates ////
						glBindBuffer(GL_ARRAY_BUFFER, m.vboTexHandle);
						glTexCoordPointer(2, GL_FLOAT, 0, 0L);

						//Enable the VBO to use texture coords
						glEnableClientState(GL_TEXTURE_COORD_ARRAY);
					}

					//// Set Data for Vertices ////
					glBindBuffer(GL_ARRAY_BUFFER, m.vboVertexHandle);
					glVertexPointer(3, GL_FLOAT, 0, 0L);

					//// Set Data for Normals ////
					glBindBuffer(GL_ARRAY_BUFFER, m.vboNormalHandle);
					glNormalPointer(GL_FLOAT, 0, 0L);

					//// Set Data For Color
					glBindBuffer(GL_ARRAY_BUFFER, m.vboColorHandle);
					glColorPointer(3, GL_FLOAT, 0, 0L);

					//Unbind GL_ARRAY_BUFFER
					glBindBuffer(GL_ARRAY_BUFFER, 0);

					//"Turn on" All Necessary client states
					glEnableClientState(GL_VERTEX_ARRAY);
					glEnableClientState(GL_COLOR_ARRAY);
					glEnableClientState(GL_NORMAL_ARRAY);
					{

						//Actually Draw the object
						glDrawArrays(GL_TRIANGLES, 0, m.faces.size() * 3);

					}
					//"Turn off" All Necessary client states
					glDisableClientState(GL_VERTEX_ARRAY);
					if(m.textures!=null)
					{
						glDisableClientState(GL_TEXTURE_COORD_ARRAY);
						glDisableVertexAttribArray(texIDVar);
					}
					glDisableClientState(GL_COLOR_ARRAY);
					glDisableClientState(GL_NORMAL_ARRAY);

					//Unbind all textures
					//if(m.textures.size()<=7)
					for(int j = 0; j < m.textures.size(); j++)
					{
						GL13.glActiveTexture(GL13.GL_TEXTURE0+j);
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
					}
				}

				//Resets the array buffer, shader, texture, and color to default values
				glBindBuffer(GL_ARRAY_BUFFER, 0);
				glUseProgram(0);
				glBindTexture(GL_TEXTURE_2D, 0);
				glColor4f(1, 1, 1, 1);

				glLoadIdentity();
			}
			glPopMatrix();
		}


		//Call the update method for all lights in scene
		for(int i =0; i < w.lights.length; i++)
		{
			if(w.lights[i]!=null)
				w.lights[i].update();
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		//if(w.player!=null)
		//w.player.cameraUpdate();
	}


	/**
	 * Called every update
	 * Handles Rendering
	 */
	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		FontHelper.update(w.player, font, realFPS, orthographicProjectionMatrix, perspectiveProjectionMatrix);

		if(w.sky != null)
			w.sky.render(w.player.position);

		GuiHelper.update();
		w.player.cam.applyPerspectiveMatrix();
	}

	/**
	 * Called every update
	 * handles everything that the
	 * render function does not handle.
	 */
	private void update()
	{	
		delta = getDelta();
		getFPS();

		//Main update method for the world and game
		worldUpdate();

		//Updates location of Orgin
		glLoadIdentity();

		//Moves the camera, Uses the player camera, ect.
		w.player.cameraUpdate();

		//Update Audio
		AudioManager.update();
	}

	private void cleanUp()
	{
		SaveManager.saveGame(w, "res/saves/saveOne.dat");
		AudioManager.deleteAll();
		Display.destroy();
		System.exit(0);
	}

	/**
	 * Returns the time the game has been running
	 * (In ticks)
	 */

	public static long getTime()
	{
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static float getDelta()
	{
		long time = getTime();
		float delta = time - lastFrame;
		lastFrame = time;

		return delta;
	}

	/**
	 * Calculate the FPS
	 */
	public static float getFPS() 
	{
		if (getTime() - lastFPS > 1000) 
		{
			realFPS = fps;
			//Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
		return fps;
	}
}
