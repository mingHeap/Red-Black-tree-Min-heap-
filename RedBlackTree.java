
import java.util.ArrayList;
import java.util.List;


class TreeNode {
    Colour color;
    int key;
    Record value;
    TreeNode left;
    TreeNode right;
    TreeNode parent;

    public TreeNode(int key, Record value, Colour color) {
        this.key = key;
        this.value = value;
        this.color = color;
    }

}


public class RedBlackTree {
    private static final Colour BLACK = Colour.BLACK;
    private static final Colour RED = Colour.RED;
    private TreeNode root;

    public boolean contains(int buildingNum) {
        return get(buildingNum) != null;
    }

    private TreeNode search(int buildingNum, TreeNode node) {
        // not found
        if (node == null) {
            return null;
        }
        // found
        if (node.key == buildingNum) {
            return node;
        }
        // search in left child
        if (buildingNum < node.key) {
            return search(buildingNum, node.left);
        } else {
            // search in right child
            return search(buildingNum, node.right);
        }
    }

    public Record get(int buildingNum) {
        TreeNode node = search(buildingNum, root);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    public List<Record> getByInterval(int buildingNum1, int buildingNum2) {
        List<Record> res = new ArrayList<>();
        searchByInterval(buildingNum1, buildingNum2, root, res);
        return res;
    }

    private void searchByInterval(int start, int end, TreeNode node, List<Record> res) {
        if (node == null || start > end) {
            return;
        }
        if (end < node.key) {
            searchByInterval(start, node.key - 1, node.left, res);
        } else if (node.key >= start && node.key <= end) {
            searchByInterval(start, node.key - 1, node.left, res);
            res.add(node.value);
            searchByInterval(node.key + 1, end, node.right, res);
        } else {
            searchByInterval(node.key + 1, end, node.right, res);
        }
    }


    public void insert(Record record) {
        if (record == null) {
            return;
        }
        TreeNode node = new TreeNode(record.getBuildingNum(), record, BLACK);
        insert(node);
    }

    public void remove(Record key) {
        TreeNode node = search(key.getBuildingNum(), root);
        if (node != null) {
            remove(node);
        }
    }


    private TreeNode getParent(TreeNode node) {
        return node != null ? node.parent : null;
    }

    private boolean isRightChild(TreeNode node) {
        return node.parent.right == node;
    }

    private boolean isLeftChild(TreeNode node) {
        return node.parent.left == node;
    }

    private Colour getColor(TreeNode node) {
        return node != null ? node.color : BLACK;
    }

    private boolean isRed(TreeNode node) {
        return (node != null) && (node.color == RED);
    }

    private boolean isBlack(TreeNode node) {
        return !isRed(node);
    }

    private void setBlack(TreeNode node) {
        if (node != null) {
            node.color = BLACK;
        }
    }

    private void setRed(TreeNode node) {
        if (node != null) {
            node.color = RED;
        }
    }

    private void leftRotate(TreeNode midNode) {
        TreeNode nodeOfRight = midNode.right;
        midNode.right = nodeOfRight.left;
        if (nodeOfRight.left != null) {
            nodeOfRight.left.parent = midNode;
        }
        nodeOfRight.parent = midNode.parent;
        // update root if root node is null
        if (midNode.parent == null) {
            root = nodeOfRight;
        } else {
            // update child of parent of midNode
            if (isLeftChild(midNode)) {
                midNode.parent.left = nodeOfRight;
            } else {
                midNode.parent.right = nodeOfRight;
            }
        }
        nodeOfRight.left = midNode;
        midNode.parent = nodeOfRight;
    }

    private void rightRotate(TreeNode midNode) {
        TreeNode leftOfNode = midNode.left;
        midNode.left = leftOfNode.right;
        if (leftOfNode.right != null) {
            leftOfNode.right.parent = midNode;
        }

        leftOfNode.parent = midNode.parent;
        if (midNode.parent == null) {
            // update root if root node is null
            root = leftOfNode;
        } else {
            // update child of parent of midNode
            if (isLeftChild(midNode)) {
                midNode.parent.left = leftOfNode;
            } else {
                midNode.parent.right = leftOfNode;
            }
        }
        leftOfNode.right = midNode;
        midNode.parent = leftOfNode;
    }

    private void insert(TreeNode node) {
        TreeNode parent = getInsertPos(node);
        node.parent = parent;
        if (parent != null) {
            if (node.key < parent.key) {
                parent.left = node;
            } else {
                parent.right = node;
            }
        } else {
            root = node;
        }
        // readjust to red black tree
        insertFixUp(node);
    }

    private TreeNode getInsertPos(TreeNode node) {
        // parent of node need to inserted
        TreeNode preNode = null;
        TreeNode curNode = root;
        while (curNode != null) {
            preNode = curNode;
            if (node.key < curNode.key) {
                curNode = curNode.left;
            } else {
                curNode = curNode.right;
            }
        }
        return preNode;
    }

    private void insertFixUp(TreeNode node) {
        // Set the color of the newly inserted node to red
        node.color = RED;
        TreeNode parent, grandFather;
        while (getParent(node) != null && isRed(getParent(node))) {
            parent = getParent(node);
            grandFather = getParent(parent);

            // if parent is left child of grandfather node
            if (isLeftChild(parent)) {
                // if uncle node is red
                TreeNode uncleNode = grandFather.right;
                if ((uncleNode != null) && isRed(uncleNode)) {
                    setBlack(uncleNode);
                    setBlack(parent);
                    setRed(grandFather);
                    node = grandFather;
                } else {
                    // if uncle node is black
                    // if the current node is the right child
                    if (isRightChild(node)) {
                        leftRotate(parent);
                        setBlack(node);
                        node = parent;
                    } else {
                        setBlack(parent);
                    }
                    setRed(grandFather);
                    rightRotate(grandFather);
                }
                // if parent is right child of grandfather node
            } else {
                // uncle node is red and the current node is the left child
                TreeNode uncle = grandFather.left;
                if ((uncle != null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                //  uncle node is black and the current node is the left child
                if (parent.left == node) {
                    rightRotate(parent);
                    setBlack(node);
                    node = parent;
                } else {
                    setBlack(parent);
                }
                //  uncle node is black and the current node is the right child
                setRed(grandFather);
                leftRotate(grandFather);
            }
        }
        setBlack(root);
    }


    private void remove(TreeNode nodeToRemove) {
        // has only one child
        if (nodeToRemove.left != null && nodeToRemove.right != null) {
            TreeNode successor = getSuccessor(nodeToRemove);
            nodeToRemove.key = successor.key;
            nodeToRemove.value = successor.value;
            nodeToRemove = successor;
        }
        TreeNode child = null;
        TreeNode parent = null;
        if (nodeToRemove.left != null) {
            child = nodeToRemove.left;
        } else {
            child = nodeToRemove.right;
        }
        parent = nodeToRemove.parent;
        Colour color = nodeToRemove.color;

        if (child != null) {
            child.parent = parent;
        }
        // if node removed is root node
        if (parent == null) {
            root = child;
        } else {
            if (parent.left == nodeToRemove) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        }

        if (color == BLACK) {
            removeFixUp(child, parent);
        }
    }

    private TreeNode getSuccessor(TreeNode node) {
        if (node == null || node.right == null) {
            return null;
        }
        node = node.right;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    private void removeFixUp(TreeNode node, TreeNode parent) {
        while (isBlack(node) && node != this.root) {
            if (parent.left == node) {
                TreeNode sib = parent.right;
                // sib node is red
                if (isRed(sib)) {
                    setBlack(sib);
                    setRed(parent);
                    leftRotate(parent);
                    sib = parent.right;
                }

                if ((sib.left == null || isBlack(sib.left)) && (sib.right == null || isBlack(sib.right))) {
                    // if sid is black and its two child is black
                    setRed(sib);
                    node = parent;
                    parent = getParent(node);
                } else {
                    if (sib.right == null || isBlack(sib.right)) {
                        // if sid is black, its left child is red, its right child id black
                        setBlack(sib.left);
                        setRed(sib);
                        rightRotate(sib);
                        sib = parent.right;
                    }
                    // if sib isblack, its right child is red
                    sib.color = parent.color;
                    setBlack(parent);
                    setBlack(sib.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {
                TreeNode sib = parent.left;
                // if sid is red
                if (isRed(sib)) {
                    setBlack(sib);
                    setRed(parent);
                    rightRotate(parent);
                    sib = parent.left;
                }
                if ((sib.left == null || isBlack(sib.left)) && (sib.right == null || isBlack(sib.right))) {
                    // if sib is black and two child is black
                    setRed(sib);
                    node = parent;
                    parent = getParent(node);
                } else {

                    if (sib.left == null || isBlack(sib.left)) {
                        // if sib is black, its left child is red and its right child black
                        setBlack(sib.right);
                        setRed(sib);
                        leftRotate(sib);
                        sib = parent.left;
                    }
                    // if sib is black, its right child is red
                    sib.color = parent.color;
                    setBlack(parent);
                    setBlack(sib.left);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }

        if (node != null) {
            setBlack(node);
        }
    }
}
