import java.util.ArrayList;

public interface Tree {

    void addNode(int value);

    void deleteNode(int value);

    Node getRootNode();

    ArrayList<Node> getTraversalInorder();

    ArrayList<Node> getTraversalPreorder();

    ArrayList<Node> getTraversalPostorder();
}
