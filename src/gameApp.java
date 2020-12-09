import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class gameApp extends JFrame{

    private static gameApp game_app;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image zhdun;
    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 500;
    private static int score;

    public static void main(String[] args) throws IOException{
        background = ImageIO.read(gameApp.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(gameApp.class.getResourceAsStream("game_over.png"));
        zhdun = ImageIO.read(gameApp.class.getResourceAsStream("zhdun.png"));
        game_app = new gameApp();
        game_app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_app.setLocation(200, 100);
        game_app.setSize(1280, 720);
        game_app.setResizable(false);
        last_frame_time = System.nanoTime();
        gameField game_field = new gameField();
        game_field.addMouseListener(new MouseInputAdapter(){  
            @Override
            public void mousePressed (MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + zhdun.getWidth(null);
                float drop_bottom = drop_top + zhdun.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y <= drop_bottom;
                if (is_drop) {
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - zhdun.getWidth(null)));
                    drop_v = drop_v +20;
                    score++;
                    game_app.setTitle("Score: " + score);
                }

            }
        });
        game_app.add(game_field);
        game_app.setVisible(true);
    }
    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        drop_top = drop_top + drop_v * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(zhdun, (int) drop_left, (int) drop_top, null);
        if (drop_top > game_app.getHeight()) g.drawImage(game_over, 280, 220, null);

    }
    private static class gameField extends JPanel{
        @Override
        protected void paintComponent (Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }

    }
}
