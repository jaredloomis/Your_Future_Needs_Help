package net.future.player;
import net.future.GameLoop;
import net.future.audio.AudioManager;
import net.future.gameobject.GameObject;
import net.future.helper.Input;
import net.future.physics.physpack.PhysPlayer;
import net.future.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Player extends GameObject
{
	private Input in;
	public Camera cam;
	public boolean debugMenu;
	
	//Controls
	public int debugButton = Keyboard.KEY_I;
	public int music = Keyboard.KEY_M;
	public int close = Keyboard.KEY_X;
	public int pause = Keyboard.KEY_ESCAPE;
	public int fly = Keyboard.KEY_F;

	public Player(World w, float aspect, float fov)
	{
		super(w);
		this.grounded = false;
		this.in = new Input();
		this.cam = new Camera(w, this, aspect, fov);
		this.debugMenu = false;
		this.cam.applyPerspectiveMatrix();
		this.physics = new PhysPlayer();
		
		AudioManager.epic.toggle();
	}

	public void playerUpdate(float delta)
	{
		//Updates which keys are down, pressed, ect
		in.update();
		
		boolean pausePressed = in.getKeypress(pause);

		//Player must always be able to pause / unpause
		if(pausePressed)
		{
			if(Mouse.isGrabbed())
			{
				this.debugMenu = false;
				this.world.paused = true;
				Mouse.setGrabbed(false);
			}
			else
			{
				this.world.paused = false;
				Mouse.setGrabbed(true);
			}
		}
		//Close screen
		if(in.getKeypress(close))
		{
			GameLoop.run = false;
		}

		//If game is paused, we can't move or perform actions
		if(!this.world.paused)
		{
			cam.processMouse();
			cam.processKeyboard(delta);
			
			this.handleInput();
		}
	}
	
	private void handleInput()
	{
		//Toggle debug screen
		if(in.getKeypress(debugButton))
		{	
			debugMenu=!debugMenu;
		}
		if(in.getKeypress(music))
		{
			AudioManager.epic.toggle();
		}
		//Toggle flying
		if(in.getKeypress(fly))
		{
			this.cam.fly=!this.cam.fly;
			this.grounded=false;
		}
		
		//Set position of light 0 of the world to player position
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) 
		{
            this.world.lights[0].lightPosition.flip();
            this.world.lights[0].lightPosition.clear();
            this.world.lights[0].lightPosition.put(new float[]{this.position.x, this.position.y, this.position.z, 1});
            this.world.lights[0].lightPosition.flip();
        }
	}

	public void cameraUpdate()
	{
		//Use the player's camera
		this.cam.applyTranslations();
	}

	@Override
	public String getName()
	{
		return "Player";
	}

}