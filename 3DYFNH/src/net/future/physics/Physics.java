package net.future.physics;
import net.future.gameobject.GameObject;
import net.future.model.Face;
import net.future.model.Model;
import org.lwjgl.util.vector.Vector3f;

public class Physics 
{
	public float gravity;
	
	public Physics(float gravity)
	{
		this.gravity = gravity;
	}
	
	/**
	 * Determines whether the two objects would
	 * be intersecting if the first object was moved to
	 * the specified position.
	 * 
	 * @param mover - The GameObject that will be moved
	 * @param collide - The GameObject to test intersection against
	 * @param newPos - The new position to place mover
	 * @return <b>true</b> if the two GameObjects will intersect, <b>false</b> if not
	 */
	public static boolean willIntersect(GameObject mover, GameObject collide, Vector3f newPos)
	{
		GameObject newOne = new GameObject(mover);
		
		newOne.position = newPos;

		return intersecting(newOne, collide);
	}

	/**
	 * Checks if the AABBs of 2 GameObjects are intersecting
	 */
	public static boolean intersecting(GameObject obj1, GameObject obj2)
	{
		Model m2=obj2.model;
		Model m1=obj1.model;
		
		//Whether to just check for intersection of each model, or to check
		//for intersection of each face on each model
		boolean specificCollision = true;

		if(!specificCollision)
		{
			AABB adjAB1 = new AABB(obj1.model.boundingBox, obj1.position);
			AABB adjAB2 = new AABB(obj2.model.boundingBox, obj2.position);
			
			if(adjAB1.intersecting(adjAB2))
			{
				return true;
			}
		}
		else
		{
			AABB adjMAB1 = new AABB(m1.boundingBox, obj1.position);
			AABB adjMAB2 = new AABB(m2.boundingBox, obj2.position);
			
			//If the bounding boxes of the 2 models are not colliding,then 
			//none of the faces are, so return false
			if(!adjMAB1.intersecting(adjMAB2))
				return false;
			
			
			
			//Loop through the faces of the first object's model
			for (int i = 0; i < m1.faces.size(); i++)
			{
				Face f1 = m1.faces.get(i);
				AABB adjAB1 = new AABB(f1.boundingBox, obj1.position);

				//Loop through the faces of the second object's model
				for(int j = 1; j < m2.faces.size(); j++)
				{
					Face f2 = m2.faces.get(j);
					AABB adjAB2 = new AABB(f2.boundingBox, obj2.position);

					//for some reason the first face's AABB is basically the AABB of the whole model
					//So don't check when j==0
					if(adjAB1.intersecting(adjAB2) && j!=0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
