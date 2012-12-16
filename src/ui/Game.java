package ui;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.BufferUtils;

import ui.LWJGLWindow;
import ui.Util;
import rosick.jglsdk.glm.*;

import logic.*;

public class Game extends LWJGLWindow {
	
	Car first;
	Road second;
	
	@Override
	protected void init() {
		second = new Road(new Vec3(-7,-7,-20), new Vec3(7,7,-20));
		first = new Car(second);
		
		initializeProgram();
		initializeVertexBuffer(); 

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		int colorDataOffset = FLOAT_SIZE * 3 * numberOfVertices;
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorDataOffset);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);

		glBindVertexArray(0);
		
	    glEnable(GL_CULL_FACE);
	    glCullFace(GL_BACK);
	    glFrontFace(GL_CW);
	    
	    glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDepthFunc(GL_LEQUAL);
		glDepthRange(0.0f, 1.0f);
	}
	
	
	@Override
	protected void display() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glUseProgram(theProgram);
		
		glBindVertexArray(vao);
		
		float deltaT = getLastFrameDuration();
		
		first.update(deltaT);
		
		final Mat4 transformMatrix = first.constructMatrix();
		
		glUniformMatrix4(modelToCameraMatrixUnif, false, transformMatrix.fillAndFlipBuffer(mat4Buffer));
		glDrawElements(GL_TRIANGLES, indexData.length, GL_UNSIGNED_SHORT, 0);

		glBindVertexArray(0);
		glUseProgram(0);
	}

	
	@Override
	protected void reshape(int width, int height) {
		cameraToClipMatrix.set(0, 0, frustumScale / (width / (float) height));
		cameraToClipMatrix.set(1, 1, frustumScale);

		glUseProgram(theProgram);
		glUniformMatrix4(cameraToClipMatrixUnif, false, cameraToClipMatrix.fillAndFlipBuffer(mat4Buffer));
		glUseProgram(0);

		glViewport(0, 0, width, height);
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	private int theProgram;
	private int modelToCameraMatrixUnif, cameraToClipMatrixUnif;
	private int vao;
	
	private Mat4 cameraToClipMatrix = new Mat4(0.0f);
	
	private FloatBuffer mat4Buffer = BufferUtils.createFloatBuffer(Mat4.SIZE);
	
	
	private void initializeProgram() {	
		ArrayList<Integer> shaderList = new ArrayList<Integer>();
		shaderList.add(Util.loadShader(GL_VERTEX_SHADER, 	"PosColorLocalTransform.vert"));
		shaderList.add(Util.loadShader(GL_FRAGMENT_SHADER,	"ColorPassthrough.frag"));

		theProgram = Util.createProgram(shaderList);
			    
	    modelToCameraMatrixUnif = glGetUniformLocation(theProgram, "modelToCameraMatrix");
		cameraToClipMatrixUnif = glGetUniformLocation(theProgram, "cameraToClipMatrix");
		
		float zNear = 1.0f; float zFar = 45.0f;
		
		cameraToClipMatrix.set(0, 0, 	frustumScale);
		cameraToClipMatrix.set(1, 1, 	frustumScale);
		cameraToClipMatrix.set(2, 2,	(zFar + zNear) / (zNear - zFar));
		cameraToClipMatrix.set(2, 3,	-1.0f);
		cameraToClipMatrix.set(3, 2,	(2 * zFar * zNear) / (zNear - zFar));
		
		glUseProgram(theProgram);
		glUniformMatrix4(cameraToClipMatrixUnif, false, cameraToClipMatrix.fillAndFlipBuffer(mat4Buffer));
		glUseProgram(0);
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	private final int numberOfVertices = 8;
	
	private final float vertexData[] = {
			+1.0f, +1.0f, +1.0f,
			+1.0f, -1.0f, +1.0f,
			-1.0f, -1.0f, +1.0f,
			-1.0f, +1.0f, +1.0f,
	
			+1.0f, +1.0f, -1.0f,
			+1.0f, -1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
			-1.0f, +1.0f, -1.0f,
	
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
	
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f};
	
	private final short indexData[] = {
			0, 1, 2,
			0, 2, 3,
			
			4, 6, 5,
			4, 7, 6,
	
			0, 4, 5,
			0, 5, 1,
			
			1, 5, 6,
			1, 6, 2,
			
			3, 2, 6,
			3, 6, 7,
			
			3, 7, 4,
			3, 4, 0};

	private int vertexBufferObject, indexBufferObject;

	
	private void initializeVertexBuffer() {
		FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vertexDataBuffer.put(vertexData);
		vertexDataBuffer.flip();
		
        vertexBufferObject = glGenBuffers();	       
		glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
	    glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		ShortBuffer indexDataBuffer = BufferUtils.createShortBuffer(indexData.length);
		indexDataBuffer.put(indexData);
		indexDataBuffer.flip();
		
        indexBufferObject = glGenBuffers();	       
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);
	    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexDataBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	private final float frustumScale = calcFrustumScale(45.0f);

	
	private float calcFrustumScale(float fovDeg) {
		final float degToRad = 3.14159f * 2.0f / 360.0f;
		float fFovRad = fovDeg * degToRad;
		
		return 1.0f / (float) (Math.tan(fFovRad / 2.0f));
	}
}