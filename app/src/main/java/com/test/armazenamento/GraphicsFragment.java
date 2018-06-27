package com.test.armazenamento;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GraphicsFragment extends Fragment {


    public GraphicsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GLSurfaceView glSurfaceView = new GLSurfaceView(getContext());
        glSurfaceView.setRenderer(new CubeRenderer(getContext()));

        return glSurfaceView;
    }

}
