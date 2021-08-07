package display;

import input.Input;
import state.State;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Display extends JFrame {

    private Canvas canvas;
    private Renderer renderer;

    public Display(int width, int height, Input input) {
        setTitle("Runner Man");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);

        renderer = new Renderer();

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(false);
        canvas.addMouseListener(input);
        add(canvas);
        addKeyListener(input);
        pack();

        canvas.createBufferStrategy(2);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void render(State state) {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        renderer.render(state, graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

}
