package snakepaula;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 *
 * @author Pauli
 */
public class GamePanel extends JPanel implements ActionListener {

    //LAS CARACTERISTICAS DEL JUEGO
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE);
    static final int DELAY = 75;
    //ARRAYS PARA LAS COORDENADAS
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int foodEaten;
    //coodernada X
    int foodX;
    //coordenada Y donde se encuentra 
    int foodY;
    //Right, Left, Up, Down  son las direcciones que puede tomar la serpiente
    char direction = 'R';
    //booleano para dar inicio al juego
    boolean running = false;
    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            //PARA CREAR UNA CUADRICULA 
            /**for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                //LINEAS VERTICALES (si aumentamos unit_size son cuadros mas grandes)
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                //LINEAS HORIZONTALES
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }*/
            //AHORA DIBUJAMOS LO QUE DEBERIA APARECEER
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            //dibujamos los movimientos
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                   //para que la serpiente tenga distintos colores  
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Agency FB", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + foodEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    //para iniciar
    public void newFood() {
        //para que aparezca a lo ancho
        foodX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

    }

    //para las funciones de movimiento
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            //UP
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            //DOWN
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            //LEFT
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            //RIGTH
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    //la serpiente come y crece
    public void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    // si choca, pierde
    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // si la cabeza toca los bordes izquierdos
        if (x[0] < 0) {
            running = false;
        }
        // si la cabeza toca los bordes derechos
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // si la cabeza toca los bordes arriba
        if (y[0] < 0) {
            running = false;
        }
        // si la cabeza toca los bordes de abajo
        if (x[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (running == false) {
            timer.stop();
        }

    }

    //vamos a necesitar graficas para cuando perdamos
    public void gameOver(Graphics g) {
        //Fuente, medidas
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Agency FB", Font.BOLD, 35));
        g.drawString("Score:" + foodEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score:" + foodEaten)) / 2, g.getFont().getSize());

        //GameOver
        g.setColor(Color.red);
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.setFont(new Font("Agency FB", Font.BOLD, 75));
        //para centrar el texto
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

            }
        }

    }
}
