package pl.edu.agh.miss.visualisation;

import au.edu.federation.caliko.FabrikBone2D;
import au.edu.federation.caliko.FabrikChain2D;
import au.edu.federation.caliko.FabrikStructure2D;
import au.edu.federation.caliko.visualisation.Camera;
import au.edu.federation.caliko.visualisation.FabrikLine2D;
import au.edu.federation.caliko.visualisation.Point2D;
import au.edu.federation.utils.Colour4f;
import au.edu.federation.utils.Utils;
import au.edu.federation.utils.Vec2f;
import au.edu.federation.utils.Vec3f;

/**
 * Hello world!
 *
 */
public class CalikoDemo2D extends CalikoDemo{
    /** Each demo works with a single structure composed of one or more IK chains. */
    static FabrikStructure2D mStructure;

    // Set yo a camera which we'll use to navigate. Params: location, orientation, width and height of window.
    static Camera camera = new Camera(new Vec3f(0.0f, 00.0f, 150.0f), new Vec3f(), Application.windowWidth, Application.windowHeight);


    /** The target is drawn at the mouse cursor location and is updated to the cursor location when the LMB is held down. */
    private static Point2D mTargetPoint = new Point2D();

    /** Define world-space cardinal axes. */
    private static final Vec2f UP    = new Vec2f( 0.0f, 1.0f);
    private static final Vec2f LEFT  = new Vec2f(-1.0f, 0.0f);
    private static final Vec2f RIGHT = new Vec2f( 1.0f, 0.0f);

    /**
     * Length of constraint lines to draw in pixels.
     *
     * @default 10.0f
     */
    private float mConstraintLineLength = 10.0f;

    /**
     * Width of constraint lines to draw in pixels.
     *
     * @default 2.0f
     */
    private float mConstraintLineWidth  = 2.0f;

    /** Offset amount used by demos 7 and 8. */
    private Vec2f mRotatingOffset       = new Vec2f(30.0f, 0.0f);

    /** Targets and offsets for chains with embedded targets in demo 7. */
    private Vec2f mSmallRotatingTargetLeft  = new Vec2f(-70.0f, 40.0f);
    private Vec2f mSmallRotatingTargetRight = new Vec2f( 50.0f, 20.0f);
    private Vec2f mSmallRotatingOffsetLeft  = new Vec2f( 25.0f, 0.0f);
    private Vec2f mSmallRotatingOffsetRight = new Vec2f(  0.0f, 30.0f);

    /** Base location used by demos 7 and 8. */
    private Vec2f mOrigBaseLocation = new Vec2f(0.0f, -80.0f);

    /**
     * Constructor
     *
     */
    public CalikoDemo2D() { setup(); }

    public CalikoDemo2D(FabrikChain2D chain) {
        setup(chain);
    }

    public void setup(FabrikChain2D chain) {
        String demoName = "Lynxmotion6DOF demo.";
        Application.window.setWindowTitle(demoName);
        mStructure = new FabrikStructure2D();
        mStructure.addChain(chain);
    }

    /**
     * Set up a demo consisting of an arrangement of 2D IK chain(s).
     *
     */
    public void setup()
    {
        // Update window title
        String demoName = "Lynxmotion6DOF demo.";
        Application.window.setWindowTitle(demoName);

        // Instantiate our FabrikStructure2D
        mStructure = new FabrikStructure2D();

        // Create a new chain
        FabrikChain2D chain = new FabrikChain2D();

        float boneLength = 40.0f;

        // Create and add first bone - 25 clockwise, 90 anti-clockwise
        FabrikBone2D basebone;
        basebone = new FabrikBone2D(new Vec2f(0.0f, -12f), new Vec2f(0.0f, 0.0f) );
        basebone.setClockwiseConstraintDegs(90.0f);
        basebone.setAnticlockwiseConstraintDegs(90.0f);
        chain.addBone(basebone);

        // Fix the base bone to its current location, and constrain it to the positive Y-axis
        chain.setFixedBaseMode(true);
        chain.setBaseboneConstraintType(FabrikChain2D.BaseboneConstraintType2D.GLOBAL_ABSOLUTE);
        chain.setBaseboneConstraintUV( new Vec2f(0.0f, 1.0f) );

        // Create and add the second bone - 50 clockwise, 90 anti-clockwise
//				chain.addConsecutiveConstrainedBone(new Vec2f(0.0f, 1.0f), 12f, 75f, 90f);

        // Create and add the third bone - 75 clockwise, 90 anti-clockwise
        chain.addConsecutiveConstrainedBone(new Vec2f(0.0f, 1.0f), 12f, 165f, 0f);
        chain.addConsecutiveConstrainedBone(new Vec2f(0.0f, 1.0f), 7f, 140f, 90f);
        chain.addConsecutiveConstrainedBone(new Vec2f(0.0f, 1.0f), 7f, 0f, 0f);

        // Finally, add the chain to the structure
        mStructure.addChain(chain);
        mStructure.solveForTarget(new Vec2f());
    }

    /**
     * Create and return random FabrikChain2D.
     *
     * @return A random FabrikChain2D.
     */
    private FabrikChain2D createRandomChain()
    {
        float boneLength           = 20.0f;
        float boneLengthRatio      = 0.8f;
        float constraintAngleDegs  = 20.0f;
        float constraintAngleRatio = 1.4f;

        // ----- Vertical chain -----
        FabrikChain2D chain = new FabrikChain2D();
        chain.setFixedBaseMode(true);

        FabrikBone2D basebone = new FabrikBone2D( new Vec2f(), UP, boneLength);
        basebone.setClockwiseConstraintDegs(constraintAngleDegs);
        basebone.setAnticlockwiseConstraintDegs(constraintAngleDegs);
        chain.setBaseboneConstraintType(FabrikChain2D.BaseboneConstraintType2D.LOCAL_RELATIVE);
        chain.addBone(basebone);

        int numBones = 6;
        float perturbLimit = 0.4f;
        for (int boneLoop = 0; boneLoop < numBones; boneLoop++)
        {
            boneLength          *= boneLengthRatio;
            constraintAngleDegs *= constraintAngleRatio;
            Vec2f perturbVector  = new Vec2f( Utils.randRange(-perturbLimit, perturbLimit), Utils.randRange(-perturbLimit, perturbLimit) );

            chain.addConsecutiveConstrainedBone( UP.plus(perturbVector), boneLength, constraintAngleDegs, constraintAngleDegs );
        }

        chain.setColour( Colour4f.randomOpaqueColour() );

        return chain;
    }

    /** Set all chains in the structure to be in fixed-base mode whereby the base locations cannot move. */
    public void setFixedBaseMode(boolean value) { mStructure.setFixedBaseMode(value); }

    /** Dummy method to set the base locations of any chains in the structure to rotate about the origin. */
    public void rotateBaseLocations() { }

    /** Dummy method to handle the movement of the camera using the W/S/A/D keys - as this is 2D we aren't actually doing any camera movement. */
    public void handleCameraMovement(int key, int action) { }

    /** Draw the currentstate of this demo / FabrikStructure2D and any contained IK chains. */
    public void draw()
    {
        // Demo 7 or 8? Offset the base location...
        if (Application.demoNumber == 7 || Application.demoNumber == 8)
        {
            // Rotate offset and apply to base location of first chain
            mRotatingOffset = Vec2f.rotateDegs(mRotatingOffset, 1.0f);
            mStructure.getChain(0).setBaseLocation( mOrigBaseLocation.plus(mRotatingOffset) );

            if (Application.demoNumber == 7)
            {
                // Rotate offsets for left and right chains
                mSmallRotatingOffsetLeft  = Vec2f.rotateDegs(mSmallRotatingOffsetLeft, -1.0f);
                mSmallRotatingOffsetRight = Vec2f.rotateDegs(mSmallRotatingOffsetRight, 2.0f);

                Vec2f newEmbeddedTargetLoc = new Vec2f();
                newEmbeddedTargetLoc.set(mStructure.getChain(1).getEmbeddedTarget() );
                mStructure.getChain(1).updateEmbeddedTarget( mSmallRotatingTargetLeft.plus(mSmallRotatingOffsetLeft)   );
                mStructure.getChain(2).updateEmbeddedTarget( mSmallRotatingTargetRight.plus(mSmallRotatingOffsetRight) );
            }

            // Update the structure. Even though we're not moving the target, we ARE moving the
            // base location, so this forces the IK chain to be resolved for the new base location.
            mStructure.solveForTarget( OpenGLWindow.worldSpaceMousePos );
        }

        // Draw our structure
        FabrikLine2D.draw( mStructure, 4.0f, Application.window.getMvpMatrix() );

        // Draw bone constraints if the draw constraints flag is true
        if (Application.drawConstraints)
        {
            //FabrikLine2D.drawConstraints( mStructure, mCurrentConstraintLineLength, mCurrentConstraintLineWidth, Application.window.getProjectionMatrixMatrix() );
            FabrikLine2D.drawConstraints( mStructure, mConstraintLineLength, mConstraintLineWidth, Application.window.getMvpMatrix() );
        }

        // Draw our target as a yellow point
        mTargetPoint.draw( OpenGLWindow.worldSpaceMousePos, Utils.YELLOW, 5.0f, Application.window.getMvpMatrix() );

    } // End of draw method
}
