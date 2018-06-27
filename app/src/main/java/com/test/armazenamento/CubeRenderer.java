package com.test.armazenamento;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {

    private Cube cube = new Cube();
    private Context context;

    private static float cubeSpeed = 2.5f;
    private static float cubeAngle = 0;


    public CubeRenderer(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        cube.loadTexture(gl,context);
        gl.glEnable(GL10.GL_TEXTURE_2D);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        gl.glScalef(1f, 1f, 1f);
        gl.glRotatef(cubeAngle,1.0f,1.0f,1.0f);

        cube.drawAnimation(gl);

        cubeAngle += cubeSpeed;
    }
}
