package net.future;
import static org.lwjgl.opengl.GL11.*;
import net.future.audio.AudioManager;
import net.future.helper.FontHelper;
import net.future.material.MyTextureLoader;
import net.future.material.SkyBox;
import net.future.player.Player;
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
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.UnicodeFont;

public class GameLoop
{
	private World w;
	private Player p;
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
	public static float delta = getDelta();
	
	public SkyBox sky;

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
		
		//Set up skybox
		sky = new SkyBox(MyTextureLoader.getTexture("res/textures/stars.jpg"));
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
		//Instantiate the world
		w = new World();

		//Set Up Player / Camera
		p = new Player(w, (float) Display.getWidth() / Display.getHeight(), 70);
		w.add(p);
		w.moveObj(p, new Vector3f(81.3151f, -1.3f, -15.424f));
		p.cam.applyPerspectiveMatrix();

		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		p.cam.applyPerspectiveMatrix();
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
	 * Called every update
	 * Handles Rendering
	 */
	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		FontHelper.update(p, font, realFPS, orthographicProjectionMatrix, perspectiveProjectionMatrix);
		
		sky.render(p.position);
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
		
		//Update the player
		p.playerUpdate(delta);

		//Main update method for the world
		w.update();

		//Updates location of Orgin
		glLoadIdentity();
		
		//Moves the camera, Uses the player camera, ect.
		p.cameraUpdate();
		
		AudioManager.update();
	}
	
	private void cleanUp()
	{
		AudioManager.deleteAll();
		Display.destroy();
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
