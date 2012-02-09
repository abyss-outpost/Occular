package com.parkour.occular;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import android.util.Log;

/**
 * This class is an object representation of 
 * a Square containing the vertex information
 * and drawing functionality, which is called 
 * by the renderer.
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */

public class Shape {

	protected FloatBuffer vertexBuffer;
	/** The initial vertex definition */
	private ByteBuffer indexBuffer;
	/** The buffer holding the normals */
	private FloatBuffer normalBuffer;

	private float normals[] = {
			// Normals
			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
			0.0f,

			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
			0.0f,

			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
			0.0f,

			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
			0.0f,

			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
			0.0f,

			0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f,
			0.0f, };

	protected float vertices[] = {
			-1.0f,
			-1.0f,
			0.0f, // Bottom Left
			1.0f,
			-1.0f,
			0.0f, // Bottom Right
			-1.0f,
			1.0f,
			0.0f, // Top Left
			1.0f,
			1.0f,
			0.0f, // Top Right

			1.0f,
			-1.0f,
			1.0f, // ...
			1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f,

			1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f,
			1.0f, -1.0f,

			-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
			1.0f, 1.0f,

			-1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
			-1.0f, 1.0f,

			-1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f,
			1.0f, -1.0f,

	};
	final static float camTexCoords[] = new float[] {
			// Camera preview
			0.0f, 0.0f, 0.9375f, 0.0f, 0.0f, 0.625f, 0.9375f, 0.625f,

			// BACK
			0.9375f, 0.0f, 0.9375f, 0.625f, 0.0f, 0.0f, 0.0f, 0.625f,
			// LEFT
			0.9375f, 0.0f, 0.9375f, 0.625f, 0.0f, 0.0f, 0.0f, 0.625f,
			// RIGHT
			0.9375f, 0.0f, 0.9375f, 0.625f, 0.0f, 0.0f, 0.0f, 0.625f,
			// TOP
			0.0f, 0.0f, 0.9375f, 0.0f, 0.0f, 0.625f, 0.9375f, 0.625f,
			// BOTTOM
			0.9375f, 0.0f, 0.9375f, 0.625f, 0.0f, 0.0f, 0.0f, 0.625f };

	private byte indices[] = {
			// Faces definition
			0, 1, 3, 0, 3,
			2, // Face front
			4, 5, 7, 4, 7,
			6, // Face right
			8, 9, 11, 8, 11,
			10, // ...
			12, 13, 15, 12, 15, 14, 16, 17, 19, 16, 19, 18, 20, 21, 23, 20, 23,
			22, };

	/**
	 * The Square constructor.
	 * 
	 * Initiate the buffers.
	 */

	public Shape() {

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);

	}

	public void draw(GL10 gl, float xangle, float yangle, float zangle) {
		// Set the face rotation
		//gl.glRotatef(xangle, 0, 1, 0);
		//gl.glRotatef(yangle, 0, 0, 1);
		//gl.glRotatef(zangle, 1, 0, 0);

		gl.glTranslatef(0.0f, -1.2f, -18.0f);

		gl.glFrontFace(GL10.GL_CW);

		// for (int i = 0; i < vertices.length; i++) {
		// Log.d("Vertex: ", ((i % 4 == 0) ? "\n" : "") + ", index" + i + ": " +
		// vertices[i]);

		// }
		// Enable vertex buffer
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);

		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		// Draw the vertices as triangle strip
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		// Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

}
