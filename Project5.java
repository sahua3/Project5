package cmsc256;
import bridges.connect.Bridges;
import bridges.base.BinTreeElement;
import java.util.Stack;

/*
Ankita Sahu
CMSC256
project5 takes in an expression and creates an expression tree, evaluates the tree, and returns an equation
 */
public class Project5 {
    public static void main(String args[]) throws Exception{
        Bridges bridges = new Bridges(3, "sahua", "169589912133");
        bridges.setTitle("tree");
        String ourExpression = " ( ( 7 + 3 ) * ( 5 - 2 ) )";
        bridges.base.BinTreeElement<String> ourRoot = buildParseTree(ourExpression);
        bridges.setDataStructure(ourRoot);
        bridges.visualize();
    }
    static String equation = "";

    public static bridges.base.BinTreeElement<String> buildParseTree(String expression) {
        expression = expression.replace(" ", "");
        int index = 0;
        BinTreeElement<String> parseTree = new BinTreeElement<>();
        BinTreeElement<String> root = parseTree;
        Stack<BinTreeElement<String>> listed = new Stack<>();
        listed.push(parseTree);
        parseTree = buildTree(expression, parseTree, root, listed, index);
        return parseTree;
    }

    public static bridges.base.BinTreeElement<String> buildTree(String expression, BinTreeElement<String> parseTree, BinTreeElement<String> root, Stack<BinTreeElement<String>> listed, int index){
        while (index < expression.length()) {
            if (expression.charAt(index) == '(') { // issue w this is the fact that after calling it, it doesnt save my root
                bridges.base.BinTreeElement<String> left = new BinTreeElement<>();
                if(root.getLeft() == null) {
                    root.setLeft(left);
                    listed.push(left);
                    root = root.getLeft();
                    index++;
                    return buildTree(expression, parseTree, root, listed, index);
                } else {
                    root.setRight(left);
                    listed.push(left);
                    root = root.getRight();
                    index++;
                    return buildTree(expression, parseTree, root, listed, index);
                }
            } else if (Character.isDigit(expression.charAt(index))) {
                char insert = expression.charAt(index);
                String sInsert = Character.toString(insert);
                bridges.base.BinTreeElement<String> wowElement = new BinTreeElement<>(sInsert);
                if (root.getLeft() == null) {
                    root.setLeft(wowElement);
                    index++;
                    return buildTree(expression, parseTree, root, listed, index);
                } else if (root.getRight() == null) {
                    root.setRight(wowElement);
                    index++;
                    return buildTree(expression, parseTree, root, listed, index);
                }
                else if (isFullTree(root)){
                    listed.pop();
                    root = listed.peek();
                    return buildTree(expression, parseTree, root, listed, index);
                }
            } else if (isOperation(expression.charAt(index))) {
                char insert = expression.charAt(index);
                String sInsert = Character.toString(insert);
                root.setValue(sInsert);
                index++;
                return buildTree(expression, parseTree, root, listed, index);
            }
            else if (expression.charAt(index) == ')'){
                if (index == expression.length()-1){
                    if (parseTree.getValue() == null){
                        parseTree = root;
                        break;
                    }
                    break;
                }
                if (listed.size() > 1) {
                    bridges.base.BinTreeElement<String> temp = listed.pop();
                    root = listed.peek();
                    index++;
                    return buildTree(expression, parseTree, root, listed, index);
                }
                else{
                    break;
                }
            }
        }
        return parseTree;
    }
    public static boolean isFullTree(bridges.base.BinTreeElement<String> tree){
        return (tree.getLeft() != null && tree.getRight() != null);
    }
    public static boolean isLeaf(bridges.base.BinTreeElement<String> tree){
        return (tree.getLeft() == null && tree.getRight() == null);
    }
    public static boolean isOperation(char s){
        if (s == '^'){
            throw new IllegalArgumentException("This operation is not valid.");
        }
        if (s == '+' || s == '-' || s == '*' || s == '/' || s == '%'){
            return true;
        }
        return false;
    }
    public static double evaluate(bridges.base.BinTreeElement<String> tree){
        if (tree == null){
            return 0;
        }
        if (isLeaf(tree)) {
            return Double.valueOf(tree.getValue());
        }
        double leftTree = evaluate(tree.getLeft());
        double rightTree = evaluate(tree.getRight());

        return equ(tree.getValue(), leftTree, rightTree);
    }
    public static double equ(String op, double LeftTree, double RightTree)
    {
        if (op.equals("+")) {
            return LeftTree + RightTree;
        }
        if (op.equals("-")) {
            return LeftTree - RightTree;
        }
        if (op.equals("*")) {
            return LeftTree * RightTree;
        }
        if (op.equals("/")) {
            return LeftTree / RightTree;
        }

        return 0;
    }

    public static String getEquation(bridges.base.BinTreeElement<String> tree){
        if (tree == null){
            return equation;
        }
        if (tree.getLeft() != null){
            equation = equation + "( ";
        }
        equation = getEquation(tree.getLeft());
        equation = equation + tree.getValue() + " ";
        equation = getEquation(tree.getRight());
        if (tree.getRight() != null){
            equation = equation + ") ";
        }
        return equation;
    }


}