import java.util.ArrayList;

public class TreeImpl implements Tree {

    private Node rootNode;

    @Override
    public void addNode(int value) {
        if (rootNode == null) {
            rootNode = new Node(value);
            return;
        }

        Node currentNode = rootNode;
        Node parentNode;
        while (true) {
            parentNode = currentNode;
            if (value == currentNode.getValue()) {
                return;
            }
            if (value < currentNode.getValue()) {
                currentNode = currentNode.getLeftChild();
                if (currentNode == null) {
                    parentNode.setLeftChild(new Node(value));
                    return;
                }
            } else {
                currentNode = currentNode.getRightChild();
                if (currentNode == null) {
                    parentNode.setRightChild(new Node(value));
                    return;
                }
            }
        }
    }

    @Override
    public void deleteNode(int value) {
        rootNode = deleteNode(rootNode, value);
    }

    @Override
    public Node getRootNode() {
        return rootNode;
    }

    @Override
    public ArrayList<Node> getTraversalInorder() {
        return getTraversalInorder(rootNode, new ArrayList<>());
    }

    @Override
    public ArrayList<Node> getTraversalPreorder() {
        return getTraversalPreorder(rootNode, new ArrayList<>());
    }

    @Override
    public ArrayList<Node> getTraversalPostorder() {
        return getTraversalPostorder(rootNode, new ArrayList<>());
    }

    private Node deleteNode(Node root, int value) {
        if (root == null) {
            return root;
        }

        if (value < root.getValue()) {
            root.setLeftChild(deleteNode(root.getLeftChild(), value));
        } else if (value > root.getValue()) {
            root.setRightChild(deleteNode(root.getRightChild(), value));
        } else {
            if (root.getLeftChild() == null) {
                return root.getRightChild();
            } else if (root.getRightChild() == null) {
                return root.getLeftChild();
            }
            root.setValue(getMinValue(root.getRightChild()));
            root.setRightChild(deleteNode(root.getRightChild(), root.getValue()));
        }

        return root;
    }

    private int getMinValue(Node root) {
        Node current = root;
        int minValue = current.getValue();
        while (current.getLeftChild() != null) {
            minValue = current.getLeftChild().getValue();
            current = current.getLeftChild();
        }
        return minValue;
    }

    private ArrayList<Node> getTraversalInorder(Node node, ArrayList<Node> result) {
        if (node != null) {
            getTraversalInorder(node.getLeftChild(), result);
            result.add(node);
            getTraversalInorder(node.getRightChild(), result);
        }
        return result;
    }

    private ArrayList<Node> getTraversalPreorder(Node node, ArrayList<Node> result) {
        if (node != null) {
            result.add(node);
            getTraversalPreorder(node.getLeftChild(), result);
            getTraversalPreorder(node.getRightChild(), result);
        }
        return result;
    }

    private ArrayList<Node> getTraversalPostorder(Node node, ArrayList<Node> result) {
        if (node != null) {
            getTraversalPostorder(node.getLeftChild(), result);
            getTraversalPostorder(node.getRightChild(), result);
            result.add(node);
        }
        return result;
    }
}
