package com.astromedicomp.light_3d_cube;

import com.astromedicomp.light_3d_cube.GLESMacros;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class GLESView extends GLSurfaceView implements GLSurfaceView.Renderer, OnGestureListener, OnDoubleTapListener {

	private final Context context; // global final variable must be initialize in constructor

	private GestureDetector guestureDetector;

	private int vertexShaderObject;
	private int fragmentShaderObject;
	private int shaderProgramObject;

	private int[] vao_quads = new int[1];
	private int[] vbo_quads = new int[1];
	private int[] vbo_quads_normals = new int[1];
	
    private int  modelViewMatrixUniform, projectionMatrixUniform;
    private int  ldUniform, kdUniform, lightPositionUniform;
	
    private int doubleTapUniform;
	
	private float angleRotate=0.0f;
	
	private int singleTap;
	private int doubleTap;

	private int gModelViewMatrixUniform, gProjectionMatrixUniform;
	private int gLdUniform, gKdUniform, gLightPositionUniform;


	private float perspectiveProjectionMatrix[] = new float[16]; // 4x4 matrix of orthogrpahic projection make it 0

	public GLESView(Context drawingContext) {
		super(drawingContext);
		context = drawingContext;
		setEGLContextClientVersion(3);
		setRenderer(this);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		guestureDetector = new GestureDetector(context, this, null, false);

		guestureDetector.setOnDoubleTapListener(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		String version = gl.glGetString(GL10.GL_VERSION);
		System.out.println("VVZ: OpenGL Version" + version);

		String glsVersion = gl.glGetString(GLES32.GL_SHADING_LANGUAGE_VERSION);
		System.out.println("VVZ: GLSI Version = " + glsVersion);

		initialize(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		resize(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		update();
		draw();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		if (!guestureDetector.onTouchEvent(event))
			super.onTouchEvent(event);
		return (true);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		System.out.println("VVZ: Single tap...");
		
		singleTap++;
        if(singleTap > 1)
            singleTap=0;
        return(true);

	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		System.out.println("VVZ: Double tap...");

		doubleTap++;
        if(doubleTap > 1)
            doubleTap=0;

		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {

		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		System.out.println("VVZ: scroll... System closing...");
		System.exit(0);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		System.out.println("VVZ: Long press...");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		System.out.println("VVZ: Fling...");
		return true;
	}

    private void initialize(GL10 gl)
    {
        // ************************ Vertex Shader *********************** 
        
		vertexShaderObject=GLES32.glCreateShader(GLES32.GL_VERTEX_SHADER);
        
        final String vertexShaderSourceCode =String.format
			(
			"#version 320 es"+
					 "\n"+
					 "in vec4 vPosition;"+
					 "in vec3 vNormal;"+
					 "uniform mat4 u_model_view_matrix;"+
					 "uniform mat4 u_projection_matrix;"+
					 "uniform mediump int u_double_tap;"+
					 "uniform vec3 u_Ld;"+
					 "uniform vec3 u_Kd;"+
					 "uniform vec4 u_light_position;"+
					 "out vec3 diffuse_light;"+
					 "void main(void)"+
					 "{"+
					 "if (u_double_tap == 1)"+
					 "{"+
					 "vec4 eyeCoordinates = u_model_view_matrix * vPosition;"+
					 "vec3 tnorm = normalize(mat3(u_model_view_matrix) * vNormal);"+
					 "vec3 s = normalize(vec3(u_light_position - eyeCoordinates));"+
					 "diffuse_light = u_Ld * u_Kd * max(dot(s, tnorm), 0.0);"+
					 "}"+
					 "gl_Position = u_projection_matrix * u_model_view_matrix * vPosition;"+
					 "}"
						);
        
        GLES32.glShaderSource(vertexShaderObject,vertexShaderSourceCode);
        
        GLES32.glCompileShader(vertexShaderObject);
        int[] iShaderCompiledStatus = new int[1];
        int[] iInfoLogLength = new int[1];
        String szInfoLog=null;
        GLES32.glGetShaderiv(vertexShaderObject, GLES32.GL_COMPILE_STATUS, iShaderCompiledStatus, 0);
        if (iShaderCompiledStatus[0] == GLES32.GL_FALSE)
        {
            GLES32.glGetShaderiv(vertexShaderObject, GLES32.GL_INFO_LOG_LENGTH, iInfoLogLength, 0);
            if (iInfoLogLength[0] > 0)
            {
                szInfoLog = GLES32.glGetShaderInfoLog(vertexShaderObject);
                System.out.println("VVZ: Vertex shader compile log = "+szInfoLog);
                uninitialize();
                System.exit(0);
           }
        }

        // ********************       Fragment Shader *****************************
        fragmentShaderObject=GLES32.glCreateShader(GLES32.GL_FRAGMENT_SHADER);
        
        final String fragmentShaderSourceCode =String.format
        (
        "#version 320 es"+
         "\n"+
         "precision highp float;"+
         "in vec3 diffuse_light;"+
         "out vec4 FragColor;"+
         "uniform int u_double_tap;"+
         "void main(void)"+
         "{"+
         "vec4 color;"+
         "if (u_double_tap == 1)"+
         "{"+
         "color = vec4(diffuse_light,1.0);"+
         "}"+
         "else"+
         "{"+
         "color = vec4(1.0, 1.0, 1.0, 1.0);"+
         "}"+
         "FragColor = color;"+
         "}"
        );

        GLES32.glShaderSource(fragmentShaderObject,fragmentShaderSourceCode);
     
        GLES32.glCompileShader(fragmentShaderObject);
        iShaderCompiledStatus[0] = 0;
        iInfoLogLength[0] = 0;
        szInfoLog=null;
        GLES32.glGetShaderiv(fragmentShaderObject, GLES32.GL_COMPILE_STATUS, iShaderCompiledStatus, 0);
        if (iShaderCompiledStatus[0] == GLES32.GL_FALSE)
        {
				GLES32.glGetShaderiv(fragmentShaderObject, GLES32.GL_INFO_LOG_LENGTH, iInfoLogLength, 0);
            if (iInfoLogLength[0] > 0)
            {
                szInfoLog = GLES32.glGetShaderInfoLog(fragmentShaderObject);
                System.out.println("VVZ:Fragment Shader Compilation = "+szInfoLog);
                uninitialize();
                System.exit(0);
            }
        }
        
        shaderProgramObject=GLES32.glCreateProgram();
        
        GLES32.glAttachShader(shaderProgramObject,vertexShaderObject); 
        GLES32.glAttachShader(shaderProgramObject,fragmentShaderObject);
        
        GLES32.glBindAttribLocation(shaderProgramObject,GLESMacros.VVZ_ATTRIB_VERTEX,"vPosition");
		GLES32.glBindAttribLocation(shaderProgramObject,GLESMacros.VVZ_ATTRIB_NORMAL,"vNormal");
        
        GLES32.glLinkProgram(shaderProgramObject);
        int[] iShaderProgramLinkStatus = new int[1];
        iInfoLogLength[0] = 0;
        szInfoLog=null;
        GLES32.glGetProgramiv(shaderProgramObject, GLES32.GL_LINK_STATUS, iShaderProgramLinkStatus, 0);
        if (iShaderProgramLinkStatus[0] == GLES32.GL_FALSE)
        {
            GLES32.glGetProgramiv(shaderProgramObject, GLES32.GL_INFO_LOG_LENGTH, iInfoLogLength, 0);
            if (iInfoLogLength[0] > 0)
            {
                szInfoLog = GLES32.glGetProgramInfoLog(shaderProgramObject);
                System.out.println("VVZ: Program linking status= "+szInfoLog);
                uninitialize();
                System.exit(0);
            }
        }

        modelViewMatrixUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_model_view_matrix");
        projectionMatrixUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_projection_matrix");
        
        doubleTapUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_double_tap");
        
        ldUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ld");
        kdUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Kd");
        lightPositionUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_light_position");;

// ************************************************ Quads Block ***********************************************        
        final float quadsVertices[]= new float[]
        	{	
		        // top surface
            1.0f, 1.0f,-1.0f,  // top-right of top
            -1.0f, 1.0f,-1.0f, // top-left of top
            -1.0f, 1.0f, 1.0f, // bottom-left of top
            1.0f, 1.0f, 1.0f,  // bottom-right of top
            
            // bottom surface
            1.0f,-1.0f, 1.0f,  // top-right of bottom
            -1.0f,-1.0f, 1.0f, // top-left of bottom
            -1.0f,-1.0f,-1.0f, // bottom-left of bottom
            1.0f,-1.0f,-1.0f,  // bottom-right of bottom
            
            // front surface
            1.0f, 1.0f, 1.0f,  // top-right of front
            -1.0f, 1.0f, 1.0f, // top-left of front
            -1.0f,-1.0f, 1.0f, // bottom-left of front
            1.0f,-1.0f, 1.0f,  // bottom-right of front
            
            // back surface
            1.0f,-1.0f,-1.0f,  // top-right of back
            -1.0f,-1.0f,-1.0f, // top-left of back
            -1.0f, 1.0f,-1.0f, // bottom-left of back
            1.0f, 1.0f,-1.0f,  // bottom-right of back
            
            // left surface
            -1.0f, 1.0f, 1.0f, // top-right of left
            -1.0f, 1.0f,-1.0f, // top-left of left
            -1.0f,-1.0f,-1.0f, // bottom-left of left
            -1.0f,-1.0f, 1.0f, // bottom-right of left
            
            // right surface
            1.0f, 1.0f,-1.0f,  // top-right of right
            1.0f, 1.0f, 1.0f,  // top-left of right
            1.0f,-1.0f, 1.0f,  // bottom-left of right
            1.0f,-1.0f,-1.0f,  // bottom-right of right
    
	};

	// If above -1.0f Or +1.0f Values Make Cube Much Larger Than Pyramid,
        // then follow the code in following loop which will convertt all 1s And -1s to -0.75 or +0.75
        for(int i=0;i<72;i++)
        {
            if(quadsVertices[i]<0.0f)
                quadsVertices[i]=quadsVertices[i]+0.25f;
            else if(quadsVertices[i]>0.0f)
                quadsVertices[i]=quadsVertices[i]-0.25f;
            else
                quadsVertices[i]=quadsVertices[i]; // no change
        }
 
        GLES32.glGenVertexArrays(1,vao_quads,0);
        GLES32.glBindVertexArray(vao_quads[0]);

        GLES32.glGenBuffers(1,vbo_quads,0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo_quads[0]);
        
         ByteBuffer byteBuffer=ByteBuffer.allocateDirect(quadsVertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer verticesBuffer=byteBuffer.asFloatBuffer();
        verticesBuffer.put(quadsVertices);
        verticesBuffer.position(0);
        
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,
                            quadsVertices.length * 4,
                            verticesBuffer,
                            GLES32.GL_STATIC_DRAW);
        
        GLES32.glVertexAttribPointer(GLESMacros.VVZ_ATTRIB_VERTEX,
                                     3,
                                     GLES32.GL_FLOAT,
                                     false,0,0);
        
        GLES32.glEnableVertexAttribArray(GLESMacros.VVZ_ATTRIB_VERTEX);
		
		System.out.println("VVZ: Quads bind done");
		
		final float cubeNormals[]= new float[]
		{
				0.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f,

		0.0f, -1.0f, 0.0f,
		0.0f, -1.0f, 0.0f,
		0.0f, -1.0f, 0.0f,
		0.0f, -1.0f, 0.0f,

		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,

		0.0f, 0.0f, -1.0f,
		0.0f, 0.0f, -1.0f,
		0.0f, 0.0f, -1.0f,
		0.0f, 0.0f, -1.0f,

		-1.0f, 0.0f, 0.0f,
		-1.0f, 0.0f, 0.0f,
		-1.0f, 0.0f, 0.0f,
		-1.0f, 0.0f, 0.0f,

		1.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f
		};
		
		
		GLES32.glGenBuffers(1,vbo_quads_normals,0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo_quads_normals[0]);
        
        byteBuffer=ByteBuffer.allocateDirect(cubeNormals.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        verticesBuffer=byteBuffer.asFloatBuffer();
        verticesBuffer.put(cubeNormals);
        verticesBuffer.position(0);
        
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,
                            cubeNormals.length * 4,
                            verticesBuffer,
                            GLES32.GL_STATIC_DRAW);
        
        GLES32.glVertexAttribPointer(GLESMacros.VVZ_ATTRIB_NORMAL,
                                     3,
                                     GLES32.GL_FLOAT,
                                     false,0,0);
        
        GLES32.glEnableVertexAttribArray(GLESMacros.VVZ_ATTRIB_NORMAL);
        
        
		
		GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0);
        GLES32.glBindVertexArray(0);

		
		System.out.println("VVZ: Quads color bind done");
		
        
// ************************************************ Quads Block ***********************************************

        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

        GLES32.glDepthFunc(GLES32.GL_LEQUAL);

        //GLES32.glEnable(GLES32.GL_CULL_FACE);
        
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		singleTap=0;
        doubleTap=0;
        
        Matrix.setIdentityM(perspectiveProjectionMatrix,0);
    }

	private void resize(int width, int height) {
		GLES32.glViewport(0, 0, width, height);
		
            Matrix.perspectiveM(perspectiveProjectionMatrix,0,45.0f,(float)width/(float)height,0.1f,100.0f);

	}

	private void draw() {
		GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);
		
		GLES32.glUseProgram(shaderProgramObject);

		if(doubleTap==1)
        {
            GLES32.glUniform1i(doubleTapUniform, 1);
            
            // setting light properties
            GLES32.glUniform3f(ldUniform, 1.0f, 1.0f, 1.0f); // diffuse intensity of light
            GLES32.glUniform3f(kdUniform, 0.5f, 0.5f, 0.5f); // diffuse reflectivity of material
            float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};
            GLES32.glUniform4fv(lightPositionUniform, 1, lightPosition,0); // light position
        }
        else
        {
            GLES32.glUniform1i(doubleTapUniform, 0);
        }
		
		float modelMatrix[]=new float[16];
        float modelViewMatrix[]=new float[16];
        float modelViewProjectionMatrix[]=new float[16];
		float rotationMatrix[]=new float[16];

//**************************** Quads draw ***********************************************************
		
		Matrix.setIdentityM(modelMatrix,0);
        Matrix.setIdentityM(modelViewMatrix,0);
        Matrix.setIdentityM(rotationMatrix,0);


        Matrix.translateM(modelMatrix,0,0.0f,0.0f,-5.0f);
        
        Matrix.setRotateM(rotationMatrix,0,angleRotate,1.0f,1.0f,1.0f); // ALL axes rotation by angleRotate angle
        Matrix.multiplyMM(modelViewMatrix,0,modelMatrix,0,rotationMatrix,0);
        
        GLES32.glUniformMatrix4fv(modelViewMatrixUniform,1,false,modelViewMatrix,0);

        GLES32.glUniformMatrix4fv(projectionMatrixUniform,1,false,perspectiveProjectionMatrix,0);
        

        GLES32.glBindVertexArray(vao_quads[0]);

        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,0,4);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,4,4);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,8,4);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,12,4);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,16,4);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,20,4);
        

        GLES32.glBindVertexArray(0);

        GLES32.glUseProgram(0);

        if(singleTap==1)
        {
            angleRotate = angleRotate - 0.75f;
        }

        requestRender();
	}
	
	void uninitialize()
    {

        if(shaderProgramObject != 0)
        {
            if(vertexShaderObject != 0)
            {
                GLES32.glDetachShader(shaderProgramObject, vertexShaderObject);
                GLES32.glDeleteShader(vertexShaderObject);
             
                vertexShaderObject = 0;
            }
            
            if(fragmentShaderObject != 0)
            {
                GLES32.glDetachShader(shaderProgramObject, fragmentShaderObject);
                GLES32.glDeleteShader(fragmentShaderObject);
                
                fragmentShaderObject = 0;
            }
        }

        if(shaderProgramObject != 0)
        {
            GLES32.glDeleteProgram(shaderProgramObject);
            shaderProgramObject = 0;
        }
    }
	
	private void update()
	{
		angleRotate=angleRotate+1.0f;
		if(angleRotate>360.0f)
			angleRotate=0.0f;
	}
}
	