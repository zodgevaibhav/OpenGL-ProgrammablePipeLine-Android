package com.astromedicomp.three_rotating_phong_light_steady_sphere_fragment;

import com.astromedicomp.three_rotating_phong_light_steady_sphere_fragment.GLESMacros;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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

private int[] vao_sphere = new int[1];
    private int[] vbo_sphere_position = new int[1];
    private int[] vbo_sphere_normal = new int[1];
    private int[] vbo_sphere_element = new int[1];

    private float light_ambient[] = {0.0f,0.0f,0.0f,1.0f};
    private float light_specular[] = {1.0f,1.0f,1.0f,1.0f};
	
	private float light_diffuse_red[] = {1.0f,0.0f,0.0f,1.0f}; //************************ For 3 lights
	private float light_diffuse_green[] = {0.0f,1.0f,0.0f,1.0f}; //************************ For 3 lights
	private float light_diffuse_blue[] = {0.0f,0.0f,1.0f,1.0f}; //************************ For 3 lights
	
    private float light_position_red[] = { 0.0f,0.0f,0.0f,0.0f }; //************************ For 3 lights
    private float light_position_green[] = { 0.0f,0.0f,0.0f,0.0f }; //************************ For 3 lights
	private float light_position_blue[] = { 0.0f,0.0f,0.0f,0.0f }; //************************ For 3 lights
	
    private float material_ambient[] = {0.0f,0.0f,0.0f,1.0f};
    private float material_diffuse[] = {1.0f,1.0f,1.0f,1.0f};
    private float material_specular[] = {1.0f,1.0f,1.0f,1.0f};
    private float material_shininess = 50.0f;

	
    private int  modelMatrixUniform, viewMatrixUniform, projectionMatrixUniform;
    private int  laUniform, lsUniform;
	
	private int lightPositionUniform_red, lightPositionUniform_green, lightPositionUniform_blue; //************************ For 3 lights
	private int  ldUniform_red, ldUniform_green,  ldUniform_blue;  //************************ For 3 lights
    private int  kaUniform, kdUniform, ksUniform, materialShininessUniform;
	
	private int numElements;
    private int numVertices;
	
	private float angleRotateRed=0.0f;
	private float angleRotateGreen=90.0f;
	private float angleRotateBlue=180.0f;
	
	private int singleTap;
	private int doubleTap;
	private int doubleTapUniform;

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
					 "uniform mat4 u_model_matrix;"+
					 "uniform mat4 u_view_matrix;"+
					 "uniform mat4 u_projection_matrix;"+
					 "uniform mediump int u_double_tap;"+
					 "uniform vec4 u_light_position_red;"+
					 "uniform vec4 u_light_position_green;"+
					 "uniform vec4 u_light_position_blue;"+
					 "out vec3 transformed_normals;"+
					 "out vec3 light_direction_red;"+
					 "out vec3 light_direction_green;"+
					 "out vec3 light_direction_blue;"+
					 "out vec3 viewer_vector;"+
					 "void main(void)"+
					 "{"+
						 "if (u_double_tap == 1)"+
						 "{"+
							 "vec4 eye_coordinates=u_view_matrix * u_model_matrix * vPosition;"+
							 "transformed_normals=mat3(u_view_matrix * u_model_matrix) * vNormal;"+
							 "light_direction_red = vec3(u_light_position_red) - eye_coordinates.xyz;"+
							 "light_direction_green = vec3(u_light_position_green) - eye_coordinates.xyz;"+
							 "light_direction_blue = vec3(u_light_position_blue) - eye_coordinates.xyz;"+
							 "viewer_vector = -eye_coordinates.xyz;"+
						 "}"+
						 "gl_Position=u_projection_matrix * u_view_matrix * u_model_matrix * vPosition;"+
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
         "in vec3 transformed_normals;"+
         "in vec3 light_direction_red;"+
		 "in vec3 light_direction_green;"+
		 "in vec3 light_direction_blue;"+
         "in vec3 viewer_vector;"+
         "out vec4 FragColor;"+
         "uniform vec3 u_La;"+
         "uniform vec3 u_Ld_red;"+
		 "uniform vec3 u_Ld_green;"+
		 "uniform vec3 u_Ld_blue;"+
         "uniform vec3 u_Ls;"+
         "uniform vec3 u_Ka;"+
         "uniform vec3 u_Kd;"+
         "uniform vec3 u_Ks;"+
         "uniform float u_material_shininess;"+
         "uniform int u_double_tap;"+
         "void main(void)"+
         "{"+
			 "vec3 phong_ads_color;"+
			 "if(u_double_tap==1)"+
			 "{"+
				 "vec3 normalized_transformed_normals=normalize(transformed_normals);"+
				 "vec3 normalized_light_direction=normalize(light_direction_red);"+
				 "vec3 normalized_viewer_vector=normalize(viewer_vector);"+
				 "vec3 ambient = u_La * u_Ka;"+
				 "float tn_dot_ld = max(dot(normalized_transformed_normals, normalized_light_direction),0.0);"+
				 "vec3 diffuse = u_Ld_red * u_Kd * tn_dot_ld;"+
				 "vec3 reflection_vector = reflect(-normalized_light_direction, normalized_transformed_normals);"+
				 "vec3 specular = u_Ls * u_Ks * pow(max(dot(reflection_vector, normalized_viewer_vector), 0.0), u_material_shininess);"+
				 "phong_ads_color=ambient + diffuse + specular;"+
				 
				 "normalized_light_direction=normalize(light_direction_blue);"+
				 "tn_dot_ld = max(dot(normalized_transformed_normals, normalized_light_direction),0.0);"+
				 "diffuse = u_Ld_blue * u_Kd * tn_dot_ld;"+
				 "reflection_vector = reflect(-normalized_light_direction, normalized_transformed_normals);"+
				 "specular = u_Ls * u_Ks * pow(max(dot(reflection_vector, normalized_viewer_vector), 0.0), u_material_shininess);"+
				 "phong_ads_color=phong_ads_color+(ambient + diffuse + specular);"+
				 
				 "normalized_light_direction=normalize(light_direction_green);"+
				 "tn_dot_ld = max(dot(normalized_transformed_normals, normalized_light_direction),0.0);"+
				 "diffuse = u_Ld_green * u_Kd * tn_dot_ld;"+
				 "reflection_vector = reflect(-normalized_light_direction, normalized_transformed_normals);"+
				 "specular = u_Ls * u_Ks * pow(max(dot(reflection_vector, normalized_viewer_vector), 0.0), u_material_shininess);"+
				 "phong_ads_color=phong_ads_color+(ambient + diffuse + specular);"+
				 
			 "}"+
			 "else"+
			 "{"+
				"phong_ads_color = vec3(1.0, 1.0, 1.0);"+
			 "}"+
			 "FragColor = vec4(phong_ads_color, 1.0);"+
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

        modelMatrixUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_model_matrix");
        viewMatrixUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_view_matrix");
        projectionMatrixUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_projection_matrix");
        
        doubleTapUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_double_tap");
        
        laUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_La");
        ldUniform_red = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ld_red");
        lsUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ls");
        lightPositionUniform_red = GLES32.glGetUniformLocation(shaderProgramObject, "u_light_position_red");
		
		laUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_La");
        ldUniform_green = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ld_green");
        lsUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ls");
        lightPositionUniform_green = GLES32.glGetUniformLocation(shaderProgramObject, "u_light_position_green");;
		
		laUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_La");
        ldUniform_blue = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ld_blue");
        lsUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ls");
        lightPositionUniform_blue = GLES32.glGetUniformLocation(shaderProgramObject, "u_light_position_blue");;

        kaUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ka");
        kdUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Kd");
        ksUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ks");
        materialShininessUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_material_shininess");;



        Sphere sphere=new Sphere();
        float sphere_vertices[]=new float[1146];
        float sphere_normals[]=new float[1146];
        float sphere_textures[]=new float[764];
        short sphere_elements[]=new short[2280];
        sphere.getSphereVertexData(sphere_vertices, sphere_normals, sphere_textures, sphere_elements);
        numVertices = sphere.getNumberOfSphereVertices();
        numElements = sphere.getNumberOfSphereElements();
 
        GLES32.glGenVertexArrays(1,vao_sphere,0);
        GLES32.glBindVertexArray(vao_sphere[0]);
        
        // position vbo
        GLES32.glGenBuffers(1,vbo_sphere_position,0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo_sphere_position[0]);
        
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(sphere_vertices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer verticesBuffer=byteBuffer.asFloatBuffer();
        verticesBuffer.put(sphere_vertices);
        verticesBuffer.position(0);
        
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,
                            sphere_vertices.length * 4,
                            verticesBuffer,
                            GLES32.GL_STATIC_DRAW);
        
        GLES32.glVertexAttribPointer(GLESMacros.VVZ_ATTRIB_VERTEX,
                                     3,
                                     GLES32.GL_FLOAT,
                                     false,0,0);
        
        GLES32.glEnableVertexAttribArray(GLESMacros.VVZ_ATTRIB_VERTEX);
        
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0);
        
        // normal vbo
        GLES32.glGenBuffers(1,vbo_sphere_normal,0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,vbo_sphere_normal[0]);
        
        byteBuffer=ByteBuffer.allocateDirect(sphere_normals.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        verticesBuffer=byteBuffer.asFloatBuffer();
        verticesBuffer.put(sphere_normals);
        verticesBuffer.position(0);
        
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,
                            sphere_normals.length * 4,
                            verticesBuffer,
                            GLES32.GL_STATIC_DRAW);
        
        GLES32.glVertexAttribPointer(GLESMacros.VVZ_ATTRIB_NORMAL,
                                     3,
                                     GLES32.GL_FLOAT,
                                     false,0,0);
        
        GLES32.glEnableVertexAttribArray(GLESMacros.VVZ_ATTRIB_NORMAL);
        
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER,0);
        
        // element vbo
        GLES32.glGenBuffers(1,vbo_sphere_element,0);
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,vbo_sphere_element[0]);
        
        byteBuffer=ByteBuffer.allocateDirect(sphere_elements.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer elementsBuffer=byteBuffer.asShortBuffer();
        elementsBuffer.put(sphere_elements);
        elementsBuffer.position(0);
        
        GLES32.glBufferData(GLES32.GL_ELEMENT_ARRAY_BUFFER,
                            sphere_elements.length * 2,
                            elementsBuffer,
                            GLES32.GL_STATIC_DRAW);
        
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER,0);

		
        GLES32.glBindVertexArray(0);
		
        
// ************************************************ Quads Block ***********************************************

        GLES32.glEnable(GLES32.GL_DEPTH_TEST);

        GLES32.glDepthFunc(GLES32.GL_LEQUAL);

        //GLES32.glEnable(GLES32.GL_CULL_FACE);
        
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		

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
			
			
			 
			 light_position_red[0] = (float)Math.cos(angleRotateRed)*100.0f;
			 light_position_red[1]=0.0f;
			 light_position_red[2]=(float)Math.sin(angleRotateRed)*100.0f;
			 light_position_red[3]=100.0f ;

			 light_position_green[0] = 0.0f;
			 light_position_green[1]=(float)Math.cos(angleRotateGreen)*100.0f;
			 light_position_green[2]=(float)Math.sin(angleRotateGreen)*100.0f;
			 light_position_green[3]=100.0f ;

			 light_position_blue[0] = -(float)Math.cos(angleRotateBlue)*100.0f;
			 light_position_blue[1]=0.0f;
			 light_position_blue[2]=(float)Math.sin(angleRotateBlue)*100.0f;
			 light_position_blue[3]=100.0f ;			 
			 
			 
			
	
			GLES32.glUniform1i(doubleTapUniform, 1);
            
            //********* setting light properties
            GLES32.glUniform3fv(laUniform, 1, light_ambient, 0);
            GLES32.glUniform3fv(lsUniform, 1, light_specular, 0);
			
			GLES32.glUniform3fv(ldUniform_red, 1, light_diffuse_red, 0);
            GLES32.glUniform4fv(lightPositionUniform_red, 1, light_position_red, 0);
			
			GLES32.glUniform3fv(ldUniform_green, 1, light_diffuse_green, 0);
            GLES32.glUniform4fv(lightPositionUniform_green, 1, light_position_green, 0);
			
			GLES32.glUniform3fv(ldUniform_blue, 1, light_diffuse_blue, 0);
            GLES32.glUniform4fv(lightPositionUniform_blue, 1, light_position_blue, 0);
        
			// Material properties
			
			GLES32.glUniform3fv(kaUniform, 1, material_ambient, 0);
            GLES32.glUniform3fv(kdUniform, 1, material_diffuse, 0);
            GLES32.glUniform3fv(ksUniform, 1, material_specular, 0);
            GLES32.glUniform1f(materialShininessUniform, material_shininess);
        }
        else
        {
            GLES32.glUniform1i(doubleTapUniform, 0);
        }
		
		float modelMatrix[]=new float[16];
        float viewMatrix[]=new float[16];
      

//**************************** Sphere draw ***********************************************************
		
		Matrix.setIdentityM(modelMatrix,0);
        Matrix.setIdentityM(viewMatrix,0);


        Matrix.translateM(modelMatrix,0,0.0f,0.0f,-1.5f);
        
        GLES32.glUniformMatrix4fv(modelMatrixUniform,1,false,modelMatrix,0);
        GLES32.glUniformMatrix4fv(viewMatrixUniform,1,false,viewMatrix,0);
        GLES32.glUniformMatrix4fv(projectionMatrixUniform,1,false,perspectiveProjectionMatrix,0);
        
		GLES32.glBindVertexArray(vao_sphere[0]);
        
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		
		
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
		angleRotateRed=angleRotateRed+0.09f;
		if(angleRotateRed>360.0f)
			angleRotateRed=0.0f;
		
		
			angleRotateGreen=angleRotateGreen+0.09f;
		if(angleRotateGreen>360.0f)
			angleRotateGreen=0.0f;
		
		
			angleRotateBlue=angleRotateBlue+0.09f;
		if(angleRotateBlue>360.0f)
			angleRotateBlue=0.0f;
	}
}
	