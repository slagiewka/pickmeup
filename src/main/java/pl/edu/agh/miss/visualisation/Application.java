package pl.edu.agh.miss.visualisation;
import au.edu.federation.caliko.FabrikChain2D;
import au.edu.federation.utils.Vec3f;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

/**
 * An example application to demonstrate the Caliko library in both 2D and 3D modes.
 *
 * Use up/down cursors to change between 2D/3D mode and left/right cursors to change demos.
 * In 2D mode clicking using the left mouse button (LMB) changes the target location, and you can click and drag.
 * In 3D mode, use W/S/A/D to move the camera and the mouse with LMB held down to look.
 *
 * See the README.txt for further documentation and controls.
 *
 * @author	Al Lansley
 * @version 1.0 - 31/01/2016
 */
public class Application
{
	// Define cardinal axes
	static final Vec3f X_AXIS = new Vec3f(1.0f, 0.0f, 0.0f);
	static final Vec3f Y_AXIS = new Vec3f(0.0f, 1.0f, 0.0f);
	static final Vec3f Z_AXIS = new Vec3f(0.0f, 0.0f, 1.0f);

	// State tracking variables
	static boolean use3dDemo           = false;
	static int     demoNumber          = 1;
	static boolean fixedBaseMode       = true;
	static boolean rotateBasesMode     = false;
	static boolean drawLines           = true;
	static boolean drawAxes            = false;
	static boolean drawModels          = true;
	static boolean drawConstraints     = true;
	static boolean leftMouseButtonDown = false;
	static boolean paused              = true;

	// Create our window and OpenGL context
	static int windowWidth     = 800;
	static int windowHeight    = 600;
	static OpenGLWindow window = new OpenGLWindow(Application.windowWidth, Application.windowHeight);

	// Declare a CalikoDemo object which can run our 3D and 2D demonstration scenarios
	static CalikoDemo demo;


	public static void drawChain(FabrikChain2D chain){
		try
		{
			// Instantiate the relevant demo type and enter the main loop
			Application.demo = new CalikoDemo2D(chain);
			Application.mainLoop();
		}
		catch (Exception e) { e.printStackTrace();          }
		finally             { Application.window.cleanup();	}
	}

	private static void mainLoop()
	{
		// Run the rendering loop until the user closes the window or presses Escape
		while (glfwWindowShouldClose(Application.window.mWindowId) == GL_FALSE)
		{
			// Clear the screen and depth buffer then draw the demo
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Application.demo.draw();

			// Swap the front and back colour buffers and poll for events
			Application.window.swapBuffers();
			glfwPollEvents();
		}
	}

} // End of Application class
