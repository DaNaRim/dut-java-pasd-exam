import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class SwingController extends JFrame {

    private final Tree tree = new TreeImpl();

    private final JButton insertButton = new JButton("Insert");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton inorderButton = new JButton("Show inorder");
    private final JButton preorderButton = new JButton("Show preorder");
    private final JButton postorderButton = new JButton("Show postorder");

    private final JLabel traversalLabel = new JLabel();

    private final TraversalAnimation traversalAnimation = new TraversalAnimation();

    public SwingController() {
        setTitle("Binary Search Tree");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setResizable(false); // Change this to make the window resizable or not
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                displayTree(g);
            }
        };
        add(treePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(50, 25));
        buttonPanel.add(new JLabel("Enter a key:"));
        buttonPanel.add(inputField);

        insertButton.addActionListener(e -> {
            // Stop the animation if it's running
            //if (traversalAnimation.isRunning()) {
            //    traversalAnimation.stop();
            //}
            String keyString = inputField.getText();
            if (keyString.isEmpty()) {
                return;
            }
            int key = Integer.parseInt(keyString);
            tree.addNode(key);
            inputField.setText("");
            inputField.grabFocus();
            treePanel.repaint();
        });
        buttonPanel.add(insertButton);

        deleteButton.addActionListener(e -> {
            // Stop the animation if it's running
            //if (traversalAnimation.isRunning()) {
            //    traversalAnimation.stop();
            //}
            String keyString = inputField.getText();
            if (keyString.isEmpty()) {
                return;
            }
            int key = Integer.parseInt(keyString);
            tree.deleteNode(key);
            inputField.setText("");
            inputField.grabFocus();
            treePanel.repaint();
        });
        buttonPanel.add(deleteButton);

        inorderButton.addActionListener(e -> startTraversal(tree.getTraversalInorder(), "Inorder"));
        buttonPanel.add(inorderButton);

        preorderButton.addActionListener(e -> startTraversal(tree.getTraversalPreorder(), "Preorder"));
        buttonPanel.add(preorderButton);

        postorderButton.addActionListener(e -> startTraversal(tree.getTraversalPostorder(), "Postorder"));
        buttonPanel.add(postorderButton);

        add(buttonPanel, BorderLayout.SOUTH);

        traversalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        traversalLabel.setVerticalAlignment(SwingConstants.TOP);
        traversalLabel.setOpaque(true);
        traversalLabel.setText(" ");
        add(traversalLabel, BorderLayout.NORTH);

        setVisible(true);
    }

    protected void disableButtons() {
        insertButton.setEnabled(false);
        deleteButton.setEnabled(false);
        inorderButton.setEnabled(false);
        preorderButton.setEnabled(false);
        postorderButton.setEnabled(false);
    }

    protected void enableButtons() {
        insertButton.setEnabled(true);
        deleteButton.setEnabled(true);
        inorderButton.setEnabled(true);
        preorderButton.setEnabled(true);
        postorderButton.setEnabled(true);
    }

    private void startTraversal(ArrayList<Node> traversal, String traversalName) {
        //Needed to stop animation properly because of delay between checking
        if (traversalAnimation.isRunning()) {
            traversalAnimation.stop();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        int[] traversalValues = traversal.stream().mapToInt(Node::getValue).toArray();
        traversalLabel.setText("Tree traversal [" + traversalName + "] is: " + Arrays.toString(traversalValues));
        traversalAnimation.setTraversal(traversal);
        //        disableButtons();

        new Thread(traversalAnimation).start();
    }

    private void displayTree(Graphics graphics) {
        Node root = tree.getRootNode();
        if (root == null) {
            return;
        }
        int x = getWidth() / 2;
        int y = 50;
        int level = 1;

        drawTree(graphics, root, x, y, level);
    }

    private void drawTree(Graphics graphics, Node node, int x, int y, int level) {
        if (node == null) {
            return;
        }
        drawNode(graphics, node, x, y);

        if (node.getLeftChild() != null) {
            int leftX = x - getWidth() / (int) Math.pow(2, (level + 1));
            int leftY = y + 50;
            drawEdge(graphics, x, y, leftX, leftY);
            drawTree(graphics, node.getLeftChild(), leftX, leftY, level + 1);
        }
        if (node.getRightChild() != null) {
            int rightX = x + getWidth() / (int) Math.pow(2, level + 1);
            int rightY = y + 50;
            drawEdge(graphics, x, y, rightX, rightY);
            drawTree(graphics, node.getRightChild(), rightX, rightY, level + 1);
        }
    }

    private void drawEdge(Graphics g, int x1, int y1, int x2, int y2) {
        // Calculate intersection point of the line with the circle. Needed to draw the line properly
        int dx = x2 - x1;
        int dy = y2 - y1;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double ddx = dx * 20 / distance; //If you change the circle radius, you have to change this too
        double ddy = dy * 20 / distance;

        int intersectionX1 = (int) (x1 + ddx);
        int intersectionY1 = (int) (y1 + ddy);
        int intersectionX2 = (int) (x2 - ddx);
        int intersectionY2 = (int) (y2 - ddy);

        g.setColor(Color.GRAY);
        g.drawLine(intersectionX1, intersectionY1, intersectionX2, intersectionY2);
    }

    private void drawNode(Graphics graphics, Node node, int x, int y) {
        int circleRadius = 20;
        int fontSize = 20;

        int textOffsetX = String.valueOf(node.getValue()).length() * 5;

        //1 - Color.RED, null, default, 3, Font.PLAIN
        //2 - Color.RED, new Color(252, 72, 72, 255), default, 3, Font.PLAIN
        //3 - Color.RED, Color.RED, Color.WHITE, 1, Font.BOLD
        Color highlightCircleColor = Color.RED;
        Color highlightBgColor = Color.RED;
        Color highlightTextColor = Color.WHITE;
        int highlightedCircleThickness = 1;
        int highlightedTextStyle = Font.BOLD;

        //1 - Color.GRAY, null, Color.GRAY
        //2 - Color.BLACK, null, Color.BLACK
        //3 - Color.GRAY, Color.GRAY, Color.BLACK
        Color standardCircleColor = Color.GRAY;
        Color standardBgColor = Color.GRAY;
        Color standardTextColor = Color.BLACK;

        //1 - Color.BLACK, null, Color.BLACK
        //2 - Color.GRAY, Color.GRAY, Color.BLACK
        //3 - Color.GRAY, null, Color.GRAY
        Color visitedCircleColor = Color.BLACK;
        Color visitedBgColor = standardBgColor;
        Color visitedTextColor = Color.BLACK;

        //1 - Color.RED, default, default, 3
        //2 - Color.RED, Color.RED, default, 1
        //3 - default, default, Color.RED, 1
        Color alertCircleColor = standardCircleColor;
        Color alertBgColor = standardBgColor;
        Color alertTextColor = Color.RED;
        int alertCircleThickness = 1;

        if (node.isAlert()) {
            Graphics2D graphics2d = (Graphics2D) graphics;

            graphics.setColor(alertCircleColor);
            graphics2d.setStroke(new BasicStroke(alertCircleThickness));
            graphics2d.drawOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);
            graphics2d.setStroke(new BasicStroke(1));

            graphics.setColor(alertBgColor);
            graphics.fillOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);

            graphics.setColor(alertTextColor);
            graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
        } else if (node.isHighlighted()) {
            graphics.setColor(highlightCircleColor);
            Graphics2D graphics2d = (Graphics2D) graphics;
            graphics2d.setStroke(new BasicStroke(highlightedCircleThickness));

            graphics.drawOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);
            graphics.setColor(highlightBgColor);
            graphics.fillOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);

            graphics2d.setStroke(new BasicStroke(1));

            graphics.setColor(highlightTextColor);
            graphics.setFont(new Font("Arial", highlightedTextStyle, fontSize));
        } else if (node.isVisited()) {
            graphics.setColor(visitedCircleColor);
            graphics.drawOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);

            graphics.setColor(visitedBgColor);
            graphics.fillOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);

            graphics.setColor(visitedTextColor);
            graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
        } else {
            graphics.setColor(standardCircleColor);
            graphics.drawOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);

            graphics.setColor(standardBgColor);
            graphics.fillOval(x - circleRadius, y - circleRadius, 2 * circleRadius, 2 * circleRadius);

            graphics.setColor(standardTextColor);
            graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
        }
        graphics.drawString(String.valueOf(node.getValue()), x - textOffsetX, y + 5);
    }

    private class TraversalAnimation implements Runnable {

        private ArrayList<Node> traversal;
        private boolean isRunning;

        public void setTraversal(ArrayList<Node> traversal) {
            this.traversal = traversal;
        }

        public boolean isRunning() {
            return isRunning;
        }

        @Override
        public void run() {
            if (isRunning) {
                return;
            }

            isRunning = true;
            for (Node node : traversal) {
                node.setHighlighted(true);
                repaint();
                wait(500);
                node.setHighlighted(false);
                node.setVisited(true);
                repaint();
                if (!isRunning) {
                    clear();
                    return;
                }
            }
            repaint();
            for (Node node : traversal) {
                node.setVisited(false);
                node.setAlert(true);
            }
            repaint();
            wait(1000);
            clear();
            repaint();
            isRunning = false;
            //enableButtons();
        }

        public void stop() {
            isRunning = false;
        }

        public void clear() {
            for (Node node : traversal) {
                node.setHighlighted(false);
                node.setVisited(false);
                node.setAlert(false);
            }
        }

        private void wait(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
