package com.astromedicomp.multiple_rotating_sphere_with_light_material;

import com.astromedicomp.multiple_rotating_sphere_with_light_material.GLESMacros;
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
	
	private float light_diffuse[] = {0.0f,0.0f,0.0f,0.0f}; //************************ For 3 lights
	
    private float light_position[] = { 100.0f,100.0f,100.0f,0.0f }; //************************ For 3 lights
	
//***************************************  24 material ************************************************
private float  s1_ambient_material[] = { 0.0215f, 0.1745f, 0.0215f,1.0f };
private float  s1_difuse_material[] = { 0.07568f, 0.61424f, 0.07568f,1.0f };
private float  s1_specular_material[] = { 0.633f, 0.727811f, 0.633f,1.0f };
private float  s1_shininess =  0.6f *128.0f ;

private float  s2_ambient_material[] = { 0.135f, 0.2225f, 0.1575f,1.0f };
private float  s2_difuse_material[] = { 0.54f, 0.89f, 0.63f,1.0f };
private float  s2_specular_material[] = { 0.316228f, 0.316228f, 0.316228f,1.0f };
private float  s2_shininess=0.1f *128.0f ;

private float  s3_ambient_material[] = { 0.05375f, 0.05f, 0.06625f,1.0f };
private float  s3_difuse_material[] = { 0.18275f, 0.17f, 0.22525f,1.0f };
private float  s3_specular_material[] = { 0.332741f, 0.328634f, 0.346435f,1.0f };
private float  s3_shininess =  0.3f*128.0f;

private float  s4_ambient_material[] = { 0.25f, 0.20725f, 0.20725f,1.0f };
private float  s4_difuse_material[] = { 1.0f, 0.829f, 0.829f,1.0f };
private float  s4_specular_material[] = { 0.296648f, 0.296648f, 0.296648f,1.0f };
private float  s4_shininess = 0.088f*128.0f ;

private float  s5_ambient_material[] = { 0.1745f, 0.01175f, 0.01175f,1.0f };
private float  s5_difuse_material[] = { 0.61424f, 0.04136f, 0.04136f,1.0f };
private float  s5_specular_material[] = { 0.727811f, 0.626959f, 0.626959f,1.0f };
private float  s5_shininess = 0.6f *128.0f ;

private float  s6_ambient_material[] = { 0.1f, 0.18725f, 0.1745f,1.0f };
private float  s6_difuse_material[] = { 0.396f, 0.74151f, 0.69102f,1.0f };
private float  s6_specular_material[] = { 0.297254f, 0.30829f, 0.306678f,1.0f };
private float  s6_shininess = 0.1f *128.0f ;

private float  s7_ambient_material[] = { 0.329412f, 0.223529f, 0.027451f,1.0f };
private float  s7_difuse_material[] = { 0.780392f, 0.568627f, 0.113725f,1.0f };
private float  s7_specular_material[] = { 0.992157f, 0.941176f, 0.807843f,1.0f };
private float  s7_shininess = 0.21794872f *128.0f;

private float  s8_ambient_material[] = { 0.2125f, 0.1275f, 0.054f,1.0f };
private float  s8_difuse_material[] = { 0.714f, 0.4284f, 0.18144f,1.0f };
private float  s8_specular_material[] = { 0.393548f, 0.271906f, 0.166721f,1.0f };
private float  s8_shininess = 0.2f *128.0f ;

private float  s9_ambient_material[] = { 0.25f, 0.25f, 0.25f,1.0f };
private float  s9_difuse_material[] = { 0.4f, 0.4f, 0.4f,1.0f };
private float  s9_specular_material[] = { 0.774597f, 0.774597f, 0.774597f,1.0f };
private float  s9_shininess = 0.6f *128.0f ;

private float  s10_ambient_material[] = { 0.19125f, 0.0735f, 0.0225f,1.0f };
private float  s10_difuse_material[] = { 0.7038f, 0.27048f, 0.0828f,1.0f };
private float  s10_specular_material[] = { 0.256777f, 0.137622f, 0.086014f,1.0f };
private float  s10_shininess = 0.1f * 128.0f;

private float  s11_ambient_material[] = { 0.24725f, 0.1995f, 0.0745f,1.0f };
private float  s11_difuse_material[] = { 0.75164f, 0.60648f, 0.22648f,1.0f };
private float  s11_specular_material[] = { 0.628281f, 0.555802f, 0.366065f,1.0f };
private float  s11_shininess = 0.4f * 128.0f;

private float  s12_ambient_material[] = { 0.19225f, 0.19225f, 0.19225f,1.0f };
private float  s12_difuse_material[] = { 0.50754f, 0.50754f, 0.50754f,1.0f };
private float  s12_specular_material[] = { 0.508273f, 0.508273f, 0.508273f,1.0f };
private float  s12_shininess = 0.4f * 128.0f;

private float  s13_ambient_material[] = { 0.0f, 0.0f, 0.0f,1.0f };
private float  s13_difuse_material[] = { 0.01f, 0.01f, 0.01f,1.0f };
private float  s13_specular_material[] = { 0.50f, 0.50f, 0.50f,1.0f };
private float  s13_shininess = 0.25f * 128.0f;

private float  s14_ambient_material[] = { 0.0f, 0.1f, 0.06f,1.0f };
private float  s14_difuse_material[] = { 0.0f, 0.50980392f, 0.50980392f,1.0f };
private float  s14_specular_material[] = { 0.50196078f, 0.50196078f, 0.50196078f,1.0f };
private float  s14_shininess = 0.25f * 128.0f;

private float  s15_ambient_material[] = { 0.0f, 0.0f, 0.0f,1.0f };
private float  s15_difuse_material[] = { 0.1f, 0.35f, 0.1f,1.0f };
private float  s15_specular_material[] = { 0.45f, 0.55f, 0.45f,1.0f };
private float  s15_shininess = 0.25f * 128.0f;

private float  s16_ambient_material[] = { 0.0f, 0.0f, 0.0f,1.0f };
private float  s16_difuse_material[] = { 0.5f, 0.0f, 0.0f,1.0f };
private float  s16_specular_material[] = { 0.7f, 0.6f, 0.6f,1.0f };
private float  s16_shininess = 0.25f * 128.0f;

private float  s17_ambient_material[] = { 0.0f, 0.0f, 0.0f,1.0f };
private float  s17_difuse_material[] = { 0.55f, 0.55f, 0.55f,1.0f };
private float  s17_specular_material[] = { 0.70f, 0.70f, 0.70f,1.0f };
private float  s17_shininess = 0.25f * 128.0f;

private float  s18_ambient_material[] = { 0.0f, 0.0f, 0.0f,1.0f };
private float  s18_difuse_material[] = { 0.5f, 0.5f, 0.0f,1.0f };
private float  s18_specular_material[] = { 0.60f, 0.60f, 0.50f,1.0f };
private float  s18_shininess = 0.25f * 128.0f;

private float  s19_ambient_material[] = { 0.02f, 0.02f, 0.02f,1.0f };
private float  s19_difuse_material[] = { 0.01f, 0.01f, 0.01f,1.0f };
private float  s19_specular_material[] = { 0.4f, 0.4f, 0.4f,1.0f };
private float  s19_shininess = 0.078125f * 128.0f;

private float  s20_ambient_material[] = { 0.0f, 0.05f, 0.05f,1.0f };
private float  s20_difuse_material[] = { 0.4f, 0.5f, 0.5f,1.0f };
private float  s20_specular_material[] = { 0.04f, 0.7f, 0.7f,1.0f };
private float  s20_shininess = 0.078125f * 128.0f;

private float  s21_ambient_material[] = { 0.0f, 0.05f, 0.0f,1.0f };
private float  s21_difuse_material[] = { 0.4f, 0.5f, 0.4f,1.0f };
private float  s21_specular_material[] = { 0.04f, 0.7f, 0.04f,1.0f };
private float  s21_shininess = 0.078125f  * 128.0f;

private float  s22_ambient_material[] = { 0.05f, 0.0f, 0.0f,1.0f };
private float  s22_difuse_material[] = { 0.5f, 0.4f, 0.4f,1.0f };
private float  s22_specular_material[] = { 0.7f, 0.04f, 0.04f,1.0f };
private float  s22_shininess = 0.078125f * 128.0f;

private float  s23_ambient_material[] = { 0.05f, 0.05f, 0.05f,1.0f };
private float  s23_difuse_material[] = { 0.5f, 0.5f, 0.5f,1.0f };
private float  s23_specular_material[] = { 0.7f, 0.7f, 0.7f,1.0f };
private float  s23_shininess = 0.078125f * 128.0f;

private float  s24_ambient_material[] = { 0.05f, 0.05f, 0.0f,1.0f };
private float  s24_difuse_material[] = { 0.5f, 0.5f, 0.4f,1.0f };
private float  s24_specular_material[] = { 0.7f, 0.7f, 0.04f,1.0f };
private float  s24_shininess = 0.078125f  * 128.0f;

private float  s25_ambient_material[] = { 0.05f, 0.00f, 0.4f,1.0f };
private float  s25_difuse_material[] = { 0.4f, 0.5f, 0.4f,1.0f };
private float  s25_specular_material[] = { 0.7f, 0.7f, 0.07f,1.0f };
private float  s25_shininess = 0.078125f  * 128.0f;

//***********************************************************************************************************
	
	
	
	
	
	

	
    private int  modelMatrixUniform, viewMatrixUniform, projectionMatrixUniform;
    private int  laUniform, lsUniform;
	
	private int lightPositionUniform; //48************************ For 3 lights
	private int  ldUniform	;  //************************ For 3 lights
    private int  kaUniform, kdUniform, ksUniform, materialShininessUniform;
	
	private int numElements;
    private int numVertices;
	
	private float angleRotateRed=0.0f;
	private float angleRotateGreen=90.0f;
	private float angleRotateBlue=180.0f;
	
	private int singleTap=0;
	private int doubleTap;
	private int doubleTapUniform;

	private int WIN_WIDTH, WIN_HEIGHT;
	
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
		WIN_WIDTH = width;
		WIN_HEIGHT=height;
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
		System.out.println("VVZ: Width - "+WIN_WIDTH);
		System.out.println("VVZ: Heigh - "+WIN_HEIGHT);
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
					 "uniform vec4 u_light_position;"+
					 "out vec3 transformed_normals;"+
					 "out vec3 light_direction;"+
					 "out vec3 viewer_vector;"+
					 "void main(void)"+
					 "{"+
						 "if (u_double_tap == 1)"+
						 "{"+
							 "vec4 eye_coordinates=u_view_matrix * u_model_matrix * vPosition;"+
							 "transformed_normals=mat3(u_view_matrix * u_model_matrix) * vNormal;"+
							 "light_direction = vec3(u_light_position) - eye_coordinates.xyz;"+
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
         "in vec3 light_direction;"+
         "in vec3 viewer_vector;"+
         "out vec4 FragColor;"+
         "uniform vec3 u_La;"+
         "uniform vec3 u_Ld;"+
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
				 "vec3 normalized_light_direction=normalize(light_direction);"+
				 "vec3 normalized_viewer_vector=normalize(viewer_vector);"+
				 "vec3 ambient = u_La * u_Ka;"+
				 "float tn_dot_ld = max(dot(normalized_transformed_normals, normalized_light_direction),0.0);"+
				 "vec3 diffuse = u_Ld * u_Kd * tn_dot_ld;"+
				 "vec3 reflection_vector = reflect(-normalized_light_direction, normalized_transformed_normals);"+
				 "vec3 specular = u_Ls * u_Ks * pow(max(dot(reflection_vector, normalized_viewer_vector), 0.0), u_material_shininess);"+
				 "phong_ads_color=ambient + diffuse + specular;"+		 
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
        ldUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ld");
        lsUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_Ls");
        lightPositionUniform = GLES32.glGetUniformLocation(shaderProgramObject, "u_light_position");
		
	
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

		
		float modelMatrix[]=new float[16];
        float viewMatrix[]=new float[16];
      
if(doubleTap==1)
	GLES32.glUniform1i(doubleTapUniform, 1);
else
	  GLES32.glUniform1i(doubleTapUniform, 0);
  
//**************************** Sphere draw ***********************************************************
		
		Matrix.setIdentityM(modelMatrix,0);
        Matrix.setIdentityM(viewMatrix,0);


        Matrix.translateM(modelMatrix,0,-1.0f,0.0f,-2.5f);
        
        GLES32.glUniformMatrix4fv(modelMatrixUniform,1,false,modelMatrix,0);
        GLES32.glUniformMatrix4fv(viewMatrixUniform,1,false,viewMatrix,0);
        GLES32.glUniformMatrix4fv(projectionMatrixUniform,1,false,perspectiveProjectionMatrix,0);
        
		GLES32.glBindVertexArray(vao_sphere[0]);
//*************************** 1 line ******************************************************************************
	//--------------- Light common to all sphere -------- (Tried by giving light to individual spheres as well)
			GLES32.glUniform3fv(laUniform, 1, light_ambient, 0);
            GLES32.glUniform3fv(lsUniform, 1, light_specular, 0);
				light_position[0]=(float)Math.sin(angleRotateRed)*100;  //X Axis rotation
				light_position[1]=0;  //Y Axis rotation
				light_position[2]=(float)Math.cos(angleRotateRed)*100;  //Z Axis rotation
			GLES32.glUniform3fv(ldUniform, 1, light_diffuse, 0);
            GLES32.glUniform4fv(lightPositionUniform, 1, light_position, 0);
		
			
		GLES32.glViewport(0, 864, WIN_WIDTH/4, WIN_HEIGHT/4);        	
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
			GLES32.glUniform3fv(kaUniform, 1, s1_ambient_material, 0);
			GLES32.glUniform3fv(kdUniform, 1, s1_difuse_material, 0);
			GLES32.glUniform3fv(ksUniform, 1, s1_specular_material, 0);
			GLES32.glUniform1f(materialShininessUniform, s1_shininess);
					
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);
//-----
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(384, 864, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s2_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s2_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s2_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s2_shininess);
		       
	   GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
	   		GLES32.glBindVertexArray(0);
		
//-----
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(768, 864, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s3_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s3_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s3_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s3_shininess);
		
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
				GLES32.glBindVertexArray(0);
//-----
				GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1156, 864, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);

		GLES32.glUniform3fv(kaUniform, 1, s4_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s4_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s4_specular_material, 0);
		GLES32.glUniform1f(materialShininessUniform, s4_shininess);


        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
				GLES32.glBindVertexArray(0);
//-----		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1536, 864, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s5_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s5_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s5_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s5_shininess);


	   GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//*************************** 2 line ******************************************************************************

		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(0, 648, WIN_WIDTH/4, WIN_HEIGHT/4);        
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s25_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s25_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s25_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s25_shininess);        
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);		
//------
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(384, 648, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s6_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s6_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s6_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s6_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);		
//-------
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(768, 648, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s7_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s7_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s7_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s7_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//-------
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1156, 648, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s8_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s8_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s8_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s8_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//-------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1536, 648, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s9_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s9_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s9_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s9_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//*************************** 3 line ******************************************************************************
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(0, 432, WIN_WIDTH/4, WIN_HEIGHT/4);        
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);

		GLES32.glUniform3fv(kaUniform, 1, s10_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s10_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s10_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s10_shininess);		
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);		
//--------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(384, 432, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s11_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s11_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s11_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s11_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);
//-------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(768, 432, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s12_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s12_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s12_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s12_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//-------
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1156, 432, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s13_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s13_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s13_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s13_shininess);		
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//-------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1536, 432, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s14_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s14_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s14_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s14_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//*************************** 4 line ******************************************************************************
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(0, 216, WIN_WIDTH/4, WIN_HEIGHT/4);        
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s15_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s15_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s15_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s15_shininess);		
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);
//-----		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(384, 216, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
       
		GLES32.glUniform3fv(kaUniform, 1, s16_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s16_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s16_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s16_shininess);
		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);
//------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(768, 216, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s17_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s17_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s17_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s17_shininess);
		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);		
//------
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1156, 216, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s18_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s18_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s18_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s18_shininess);
		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);		
//------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1536, 216, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s19_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s19_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s19_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s19_shininess);		
		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//*************************** 5 line 
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(0, 0, WIN_WIDTH/4, WIN_HEIGHT/4);        
        GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
		
		GLES32.glUniform3fv(kaUniform, 1, s20_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s20_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s20_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s20_shininess);
		
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);		
//-------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(384, 0, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s21_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s21_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s21_specular_material, 0);
        GLES32.glUniform1f(materialShininessUniform, s21_shininess);		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);
		GLES32.glBindVertexArray(0);		
//------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(768, 0, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s22_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s22_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s22_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s22_shininess);
		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);
//------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1156, 0, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s23_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s23_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s23_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s23_shininess);
		
		GLES32.glDrawElements(GLES32.GL_TRIANGLES, numElements, GLES32.GL_UNSIGNED_SHORT, 0);		
		GLES32.glBindVertexArray(0);		
//------		
		GLES32.glBindVertexArray(vao_sphere[0]);
		GLES32.glViewport(1536, 0, WIN_WIDTH/4, WIN_HEIGHT/4);        
		GLES32.glBindBuffer(GLES32.GL_ELEMENT_ARRAY_BUFFER, vbo_sphere_element[0]);
        
		GLES32.glUniform3fv(kaUniform, 1, s24_ambient_material, 0);
        GLES32.glUniform3fv(kdUniform, 1, s24_difuse_material, 0);
        GLES32.glUniform3fv(ksUniform, 1, s24_specular_material, 0);		
        GLES32.glUniform1f(materialShininessUniform, s24_shininess);
		
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
		angleRotateRed=angleRotateRed+0.05f;
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
	