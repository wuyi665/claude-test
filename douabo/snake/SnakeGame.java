import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

// 主类，创建游戏窗口
public class SnakeGame extends JFrame {
    public SnakeGame() {
        setTitle("贪吃蛇游戏");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // 添加游戏面板
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        
        pack();
        setLocationRelativeTo(null); // 窗口居中
        setVisible(true);
    }
    
    public static void main(String[] args) {
        // 在事件调度线程中启动游戏，确保UI线程安全
        SwingUtilities.invokeLater(() -> new SnakeGame());
    }
}

// 游戏面板，负责游戏逻辑和绘制
class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 20; // 每个格子的大小
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100; // 游戏速度，数值越小速度越快
    
    private final int[] x = new int[GAME_UNITS]; // 蛇身体各部分的x坐标
    private final int[] y = new int[GAME_UNITS]; // 蛇身体各部分的y坐标
    private int bodyParts = 6; // 初始身体长度
    private int applesEaten; // 吃到的苹果数量
    private int appleX; // 苹果的x坐标
    private int appleY; // 苹果的y坐标
    private char direction = 'R'; // 初始方向：右
    private boolean running = false; // 游戏是否运行
    private Timer timer; // 控制游戏循环的计时器
    private Random random; // 用于随机生成苹果位置
    
    public GamePanel() {
        random = new Random();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }
    
    // 开始游戏
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    // 绘制游戏元素
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    // 具体绘制方法
    public void draw(Graphics g) {
        if (running) {
            // 绘制食物
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            
            // 绘制蛇
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) { // 蛇头
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else { // 蛇身
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            
            // 绘制分数
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("分数: " + applesEaten, 
                         (WIDTH - metrics.stringWidth("分数: " + applesEaten)) / 2, 
                         g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }
    
    // 生成新的苹果
    public void newApple() {
        appleX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        
        // 确保苹果不会出现在蛇身上
        for (int i = 0; i < bodyParts; i++) {
            if (x[i] == appleX && y[i] == appleY) {
                newApple();
                break;
            }
        }
    }
    
    // 移动蛇
    public void move() {
        // 移动身体部分
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        
        // 移动头部
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    
    // 检查是否吃到苹果
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    // 检查碰撞
    public void checkCollisions() {
        // 检查是否撞到自己
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        
        // 检查是否撞到左边界
        if (x[0] < 0) {
            running = false;
        }
        
        // 检查是否撞到右边界
        if (x[0] >= WIDTH) {
            running = false;
        }
        
        // 检查是否撞到上边界
        if (y[0] < 0) {
            running = false;
        }
        
        // 检查是否撞到下边界
        if (y[0] >= HEIGHT) {
            running = false;
        }
        
        if (!running) {
            timer.stop();
        }
    }
    
    // 游戏结束画面
    public void gameOver(Graphics g) {
        // 绘制分数
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("分数: " + applesEaten, 
                     (WIDTH - metrics1.stringWidth("分数: " + applesEaten)) / 2, 
                     g.getFont().getSize());
        
        // 绘制游戏结束文本
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("游戏结束", 
                     (WIDTH - metrics2.stringWidth("游戏结束")) / 2, 
                     HEIGHT / 2);
        
        // 绘制重新开始提示
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("按 R 键重新开始", 
                     (WIDTH - metrics3.stringWidth("按 R 键重新开始")) / 2, 
                     HEIGHT / 2 + 50);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') { // 不能直接向左转（如果当前向右）
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') { // 不能直接向右转（如果当前向左）
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') { // 不能直接向上转（如果当前向下）
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') { // 不能直接向下转（如果当前向上）
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_R:
                if (!running) {
                    // 重置游戏
                    resetGame();
                }
                break;
        }
    }
    
    // 重置游戏
    private void resetGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        // 清空蛇的位置
        for (int i = 0; i < GAME_UNITS; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}
