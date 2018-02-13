package com.astromedicomp.smily_texture_quads;

import com.astromedicomp.smily_texture_quads.GLESMacros;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.BitmapFactory; // for texture factory
import android.graphics.Bitmap; // for PNG image file to user
import android.opengl.GLUtils; // for texImage2D function


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
	private int[] vbo_quads_texture = new int[1];

	private int[] textureSmily=new int[1];
	
	private int mvpUniform;
	private int mvpTextureSamplerUniform;
	
	private float angleRotate;

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
		//update();
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
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		System.out.println("VVZ: Double tap...");
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
			 "in vec2 vTexture;"+
			 "out vec2 out_texture;"+
			 "uniform mat4 u_mvp_matrix;"+
			 "void main(void)"+
			 "{"+
			 "gl_Position = u_mvp_matrix * vPosition;"+
			 "out_texture=vTexture;"+
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
		 "in vec2 out_texture;"+
         "out vec4 FragColor;"+
		 "uniform highp sampler2D u_texture0_sampler;"+
         "void main(void)"+
         "{"+
         "FragColor = texture(u_texture0_sampler,out_texture);"+//vec4(1.0, 1.0, 1.0, 1.0);"+
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
		GLES32.glBindAttribLocation(shaderProgramObject,GLESMacros.VVZ_ATTRIB_TEXTURE,"vTexture");
        
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

        mvpUniform=GLES32.glGetUniformLocation(shaderProgramObject,"u_mvp_matrix");
		mvpTextureSamplerUniform=GLES32.glGetUniformLocation(shaderProgramObject,"u_texture0_sampler");
		
		textureSmily[0]=loadGLTexture(R.raw.smily);
		
// ************************************************ Quads Block ***********************************************        
        final float quadsVertices[]= new float[]
        	{	

		1.0f, 1.0f, 1.0f, //right-top corner of front face
		-1.0f, 1.0f, 1.0f, //left-top corner of front face
		-1.0f, -1.0f, 1.0f, //left-bottom corner of front face
		1.0f, -1.0f, 1.0f, //right-bottom corner of front face

	};

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
		
		final float quadTexture[]= new float[]
		{
			0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,1.0f,
		};
		
		
        GLES32.glGenBuffers(1,vbo_quads_texture,0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo_quads_texture[0]);
        
        ByteBuffer  byteTextureBuffer=ByteBuffer.allocateDirect(quadTexture.length * 4);
        byteTextureBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer verticesTextureBuffer=byteTextureBuffer.asFloatBuffer();
        verticesTextureBuffer.put(quadTexture);
        verticesTextureBuffer.position(0);
        
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,
                            quadTexture.length * 4,
                            verticesTextureBuffer,
                            GLES32.GL_STATIC_DRAW);
        
        GLES32.glVertexAttribPointer(GLESMacros.VVZ_ATTRIB_TEXTURE,
                                     2,
                                     GLES32.GL_FLOAT,
                                     false,0,0);
        
        GLES32.glEnableVertexAttribArray(GLESMacros.VVZ_ATTRIB_TEXTURE);
        
        
		
		GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0);
        GLES32.glBindVertexArray(0);

		
		System.out.println("VVZ: Quads texture bind done");
		
        
// ************************************************ Quads Block ***********************************************

        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

        GLES32.glDepthFunc(GLES32.GL_LEQUAL);

        //GLES32.glEnable(GLES32.GL_CULL_FACE);
        
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        Matrix.setIdentityM(perspectiveProjectionMatrix,0);
    }
	
	private int loadGLTexture(int imageFileResourceID)
	{
		
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        
        // read in the resource, resource are found in context, and which resource needed is parameterized to function, and while getting resuouce, we can provide options
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageFileResourceID, options);
        
        int[] texture=new int[1];
        
        // create a texture object to apply to model
        GLES32.glGenTextures(1, texture, 0);
        
        // indicate that pixel rows are tightly packed (defaults to stride of 4 which is kind of only good for RGBA or FLOAT data types)
        GLES32.glPixelStorei(GLES32.GL_UNPACK_ALIGNMENT,1);
        
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,texture[0]);
        
        // set up filter and wrap modes for this texture object
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MAG_FILTER,GLES32.GL_LINEAR);
        GLES32.glTexParameteri(GLES32.GL_TEXTURE_2D,GLES32.GL_TEXTURE_MIN_FILTER,GLES32.GL_LINEAR_MIPMAP_LINEAR);
        
        // load the bitmap into the bound texture
        GLUtils.texImage2D(GLES32.GL_TEXTURE_2D, 0, bitmap, 0);
        
        // generate mipmap
        GLES32.glGenerateMipmap(GLES32.GL_TEXTURE_2D);
        
        return(texture[0]);
	}


	private void resize(int width, int height) {
		GLES32.glViewport(0, 0, width, height);
		
            Matrix.perspectiveM(perspectiveProjectionMatrix,0,45.0f,(float)width/(float)height,0.1f,100.0f);

	}

	private void draw() {
		GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);
		
		GLES32.glUseProgram(shaderProgramObject);

        float modelViewMatrix[]=new float[16];
        float modelViewProjectionMatrix[]=new float[16];
		float rotationMatrix[]=new float[16];

//**************************** Quads draw ***********************************************************
		Matrix.setIdentityM(modelViewMatrix,0);
        Matrix.translateM(modelViewMatrix,0,(float)0.0f, (float)0.0f, (float)-4.0f);
        
		
        Matrix.setIdentityM(modelViewProjectionMatrix,0);
        
        Matrix.multiplyMM(modelViewProjectionMatrix,0,perspectiveProjectionMatrix,0,modelViewMatrix,0);
        
        GLES32.glUniformMatrix4fv(mvpUniform,1,false,modelViewProjectionMatrix,0);
        
        GLES32.glBindVertexArray(vao_quads[0]);
        
		GLES32.glActiveTexture(GLES32.GL_TEXTURE0);
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,textureSmily[0]);
        GLES32.glUniform1i(mvpTextureSamplerUniform, 0);
		
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,0,4);
        
		GLES32.glBindTexture(GLES32.GL_TEXTURE_2D,0);
        GLES32.glBindVertexArray(0);



        GLES32.glUseProgram(0);

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
	