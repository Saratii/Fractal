import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;

public class Fractal extends JPanel{
    static int MAX_ITERATIONS = 100;
    static int FRACTAL_WIDTH = 2560;
    static int FRACTAL_HEIGHT = 1440;
    static int centerX = 230;
    static int centerY = -25;
    static double[][][] image;
    
    public static double[][][] render(){
        double zoom = 1.3;
        double[][][] buffer = new double[FRACTAL_WIDTH][FRACTAL_HEIGHT][3];
        for(int x = 0; x<FRACTAL_WIDTH; x++){
            for(int y = 0; y<FRACTAL_HEIGHT;y++){
                double cx = zoom*(-FRACTAL_WIDTH+(x-centerX)*2)/FRACTAL_HEIGHT;
                double cy = zoom*(-FRACTAL_HEIGHT+(y-centerY)*2)/FRACTAL_HEIGHT; 
                int i = countIterations(cx, cy);             
                if(i < MAX_ITERATIONS){
                    buffer[x][y][0] = 0.5 + 2 * Math.cos(3 + i * 0.15 + 1.0);
                    buffer[x][y][1] = 0.5 + 2 * Math.cos(3 + i * 0.15 + 0.6);
                    buffer[x][y][2] = 0.5 + 2 * Math.cos(3 + i * 0.15 + 0.0);
                }
            }
        }
        return buffer;
    }
    static int countIterations(double cx, double cy){
        int i;
        double zr = 0.00000000000000000000000000000000001; 
        double zi = 0; 
        double threshhold = 6;
        for(i = 0; i < MAX_ITERATIONS; i++){
            double arg = Math.atan(zi/zr);
            double mod = Math.pow(zr, 2)+Math.pow(zi, 2);
            zr = (mod*Math.cos(arg*2))+cx;
            zi = (mod*Math.sin(arg*2))+cy;
            if(((zr * zr)+(zi * zi)) >= threshhold){
                break;
            }
        }
        return i;
    }
    
    static JPanel jf = new JPanel() {
        RenderingHints QUALITY_RENDER = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        RenderingHints ANTIALIASING = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        protected void paintComponent( Graphics g) {
            Graphics2D gx = (Graphics2D)g;
            gx.addRenderingHints(this.ANTIALIASING);
            gx.addRenderingHints(this.QUALITY_RENDER);
            for(int x = 0; x < FRACTAL_WIDTH; x++) {
                for(int y = 0; y<FRACTAL_HEIGHT;y++){
                    float colr = (float)Math.max(0, Math.min(1, image[x][y][0]));
                    float colg = (float)Math.max(0, Math.min(1, image[x][y][1]));
                    float colb = (float)Math.max(0, Math.min(1, image[x][y][2]));
                    gx.setColor(new Color(colr, colg, colb));
                    gx.fillRect(x, y, 1, 1);
                }                
            }
        }
    };  
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setSize(FRACTAL_WIDTH, FRACTAL_HEIGHT);
        frame.setContentPane(jf);
        image = render();
        jf.repaint();
        frame.setVisible(true);
        jf.addMouseListener(new MouseInputAdapter(){
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                centerX += FRACTAL_WIDTH/2 - e.getX();
                centerY += FRACTAL_HEIGHT/2 - e.getY();
                image = render();
                jf.repaint();
            }
        });
    }
}