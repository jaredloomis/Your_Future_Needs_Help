package net.future.math;
import net.future.model.Face;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GeometryHelper 
{
	/**
	 * @deprecated Not necessary to use, don't know if it is accurate <br/><br/>
	 * Determines the slope of specified face
	 * 
	 * @param f - The face
	 * 
	 * @return A Vector3f with the change of the x, y, and z axes
	 */
	@Deprecated
	public static Vector3f getSlope(Face f)
	{
		Vector3f p1 = f.points[0];
		Vector3f p2 = f.points[1];
		Vector3f p3 = f.points[2];

		//Vectors from point1 to point2 and from
		//point1 to point3
		Vector3f a = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
		Vector3f b = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.y);

		//Vector perpindiculat to the plane
		Vector3f E = Vector3f.cross(a, b, null);

		float B = E.y;
		float C = E.z;
		float x = p1.x;
		float y = p1.y;
		float z = p1.z;
		//float Ax = E.x*p1.x;
		//float By = E.y*p1.y;
		//float Cz = E.z*p1.z;

		//plane equation
		//float PlaneEquation = A*x + B*y + C*z;

		//X value of point intersecting
		//The yz coordinate plane
		float yzIntercept = B*y + C*z;

		Vector3f yzInt = new Vector3f(yzIntercept, 0, 0);

		float deltaX = x - yzInt.x;
		float deltaY = y - yzInt.y;
		float deltaZ = z - yzInt.z;


		return new Vector3f(deltaX, deltaY, deltaZ);
	}

	/**
	 * Returns the distance between two 3D points
	 * @param v1 - The first vector
	 * @param v2 - The second vector
	 */
	public static float distance(Vector3f v1, Vector3f v2)
	{
		return (float)Math.abs(Math.sqrt(
				Math.pow((v2.x-v1.x), 2) +
				Math.pow((v2.y-v1.y), 2) +
				Math.pow((v2.z-v1.z), 2)));
	}

	/**
	 * Returns the distance between two 2D points
	 * @param v1 - The first vector
	 * @param v2 - The second vector
	 */
	public static float distance(Vector2f v1, Vector2f v2)
	{
		return (float)Math.abs(Math.sqrt(
				Math.pow((v2.x-v1.x), 2) +
				Math.pow((v2.y-v1.y), 2)));
	}
}