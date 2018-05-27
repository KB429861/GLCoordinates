package com.example;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.*;

public class MainFrame extends JFrame {

    private GLCanvas canvas;
    private GLU glu;
    private MouseEvent mouse;

    private float positionX = 0f;
    private float positionY = 0f;
    private float positionZ = 1f;

    private int width = 800;
    private int height = 600;

    private float zoom = 0.5f;

    private MainFrame() {
        super("GLCoordinates");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        canvas = new GLCanvas(capabilities);

        canvas.addGLEventListener(new GLEventListener() {
            @Override
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

            @Override
            public void dispose(GLAutoDrawable glAutoDrawable) {
            }

            @Override
            public void display(GLAutoDrawable drawable) {
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                gl.glMatrixMode(GL_PROJECTION);
                gl.glLoadIdentity();
                System.out.println("w:" + width + " h:" + height);
//                glu.gluPerspective(45.0, aspect, 0.1, 100.0);
                gl.glOrtho(-width / 2f * zoom, width / 2f * zoom, -height / 2f * zoom, height / 2f * zoom, -1.0, 1.0);

                glu.gluLookAt(positionX, positionY, positionZ, positionX, positionY, 0, 0, 1, 0);

                int viewport[] = new int[4];
                double mvmatrix[] = new double[16];
                double projmatrix[] = new double[16];
                int realy = 0;// GL y coord pos
                double wcoord[] = new double[4];// wx, wy, wz;// returned xyz coords

                if (mouse != null) {
                    int x = mouse.getX(), y = mouse.getY();
                    switch (mouse.getButton()) {
                        case MouseEvent.BUTTON1:
                            gl.glGetIntegerv(GL_VIEWPORT, viewport, 0);
                            gl.glGetDoublev(GL_MODELVIEW_MATRIX, mvmatrix, 0);
                            gl.glGetDoublev(GL_PROJECTION_MATRIX, projmatrix, 0);
                            /* note viewport[3] is height of window in pixels */
                            realy = viewport[3] - (int) y - 1;
                            System.out.println("Coordinates at cursor are (" + x + ", " + realy);
                            glu.gluUnProject((double) x, (double) realy, 0.0, //
                                    mvmatrix, 0,
                                    projmatrix, 0,
                                    viewport, 0,
                                    wcoord, 0);
                            System.out.println("World coords at z=0.0 are ( " //
                                    + wcoord[0] + ", " + wcoord[1] + ", " + wcoord[2]
                                    + ")");
                            glu.gluUnProject((double) x, (double) realy, 1.0, //
                                    mvmatrix, 0,
                                    projmatrix, 0,
                                    viewport, 0,
                                    wcoord, 0);
                            System.out.println("World coords at z=1.0 are (" //
                                    + wcoord[0] + ", " + wcoord[1] + ", " + wcoord[2]
                                    + ")");
                            break;
                        case MouseEvent.BUTTON2:
                            break;
                        default:
                            break;
                    }
                }

                gl.glMatrixMode(GL_MODELVIEW);
                gl.glLoadIdentity();

                gl.glBegin(GL_TRIANGLES);
                gl.glVertex3f(0.0f, 1.0f, 0.0f);
                gl.glVertex3f(-1.0f, -1.0f, 0.0f);
                gl.glVertex3f(1.0f, -1.0f, 0.0f);
                gl.glEnd();

            }

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
                GL2 gl = drawable.getGL().getGL2();

                width = w;
                height = h;

                gl.glViewport(0, 0, width, height);

            }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                float speed = 0.5f;
                switch (c) {
                    case 'w':
                        positionY += speed;
                        break;
                    case 'a':
                        positionX -= speed;
                        break;
                    case 's':
                        positionY -= speed;
                        break;
                    case 'd':
                        positionX += speed;
                        break;
                    case '+':
                        zoom -= 0.01f;
                        break;
                    case '-':
                        zoom += 0.01f;
                        break;
                }
                System.out.println("translate:[" + positionX + "," + positionY + "," + zoom + "]");
                canvas.display();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouse = e;
                canvas.display();
            }
        });

        add(canvas, BorderLayout.CENTER);
        setBackground(Color.WHITE);
        setSize(width, height);
    }

    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
}
