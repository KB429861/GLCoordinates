import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class MainFrame extends JFrame {

    private GLU glu;

    private MainFrame() {
        super("GLCoordinates");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(capabilities);

        canvas.addGLEventListener(new GLEventListener() {
            public void init(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                glu = new GLU();
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glClearDepth(1.0f);
                gl.glEnable(GL_DEPTH_TEST);
                gl.glDepthFunc(GL_LEQUAL);
                gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
                gl.glShadeModel(GL_SMOOTH);
            }

            public void dispose(GLAutoDrawable glAutoDrawable) {
            }

            public void display(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();

                gl.glTranslatef(0.0f, 0.0f, -6.0f);
                gl.glBegin(GL_TRIANGLES);
                gl.glVertex3f(0.0f, 1.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, 0.0f);
                gl.glEnd();
            }

            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                GL2 gl = drawable.getGL().getGL2();

                if (height == 0) height = 1;
                float aspect = (float) width / height;

                gl.glViewport(0, 0, width, height);

                gl.glMatrixMode(GL_PROJECTION);
                gl.glLoadIdentity();
                glu.gluPerspective(45.0, aspect, 0.1, 100.0);

                gl.glMatrixMode(GL_MODELVIEW);
                gl.glLoadIdentity();
            }
        });

        add(canvas, BorderLayout.CENTER);
        setBackground(Color.WHITE);
        setSize(800, 600);
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
