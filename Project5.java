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
        String ourExpression = "( ( 7 + 3 ) * ( 5 / 0 ) )";
        bridges.base.BinTreeElement<String> ourRoot = buildParseTree(ourExpression);
        bridges.setDataStructure(ourRoot);
        bridges.visualize();
        double eval = evaluate(buildParseTree(ourExpression));
        System.out.println(eval);
        String equation = getEquation(buildParseTree(ourExpression));
        System.out.println("a" + equation + "a");
    }
    public static bridges.base.BinTreeElement<String> buildParseTree(String expression){
        if (expression == null || expression.length() == 0) {
            return null;
        }
        String[] stringArr = expression.split(" ");
        Stack<BinTreeElement<String>> nodeStack = new Stack<>();
        int numAdded = 0;
        for (int i = 0; i < stringArr.length; i++) {
            if (stringArr[i].equalsIgnoreCase("(")) {
            } else if (isOperation(stringArr[i])) {
                bridges.base.BinTreeElement<String> newNode = new BinTreeElement<>();
                newNode.setValue(stringArr[i]);
                nodeStack.push(newNode);
            } else if (stringArr[i].equalsIgnoreCase(")")) {
                bridges.base.BinTreeElement<String> rightNode = nodeStack.pop();
                bridges.base.BinTreeElement<String> root = nodeStack.pop();
                bridges.base.BinTreeElement<String> leftNode = nodeStack.pop();
                root.setLeft(leftNode);
                root.setRight(rightNode);
                nodeStack.push(root);
            } else { // assumes that it has to be a num val
                bridges.base.BinTreeElement<String> newNode = new BinTreeElement<>();
                newNode.setValue(stringArr[i]);
                nodeStack.push(newNode);
            }
        }
        bridges.base.BinTreeElement<String> parsedTree = new BinTreeElement<>();
        if (nodeStack.size() == 1){
            parsedTree = nodeStack.pop();
        }
        else {
            while (!nodeStack.isEmpty()) {
                if (isOperation(nodeStack.peek().getValue())) {
                    bridges.base.BinTreeElement<String> popped = nodeStack.pop();
                    String theVal = popped.getValue();
                    parsedTree.setValue(theVal);
                }
                if (parsedTree.getRight() == null) {
                    parsedTree.setRight(nodeStack.pop());
                } else {
                    parsedTree.setLeft(nodeStack.pop());
                }
            }
        }
        return parsedTree;
    }
    private static boolean isOperation(String s){
        if (s.equalsIgnoreCase("^")){
            throw new IllegalArgumentException("This operation is not valid.");
        }
        return (s.equalsIgnoreCase("+") || s.equalsIgnoreCase("-") || s.equalsIgnoreCase("*") || s.equalsIgnoreCase("/") || s.equalsIgnoreCase("%"));
    }
    public static boolean isLeaf(bridges.base.BinTreeElement<String> tree){
        return tree.getLeft() == null && tree.getRight() == null;
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
    private static double equ(String op, double LeftTree, double RightTree)
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
            if (RightTree == 0){
                throw new ArithmeticException();
            }
            return LeftTree / RightTree;
        }
        if (op.equals("%")) {
            return LeftTree % RightTree;
        }

        return 0;
    }
    public static String getEquation(bridges.base.BinTreeElement<String> tree){
        String equation = "";
        equation = buildEquation(tree, equation);
        equation = equation.substring(0, equation.length()-1);
        return equation;
    }
    private static String buildEquation (bridges.base.BinTreeElement<String> tree, String equation){
        if (tree == null){
            return equation;
        }
        if (tree.getLeft() != null){
            equation = equation + "( ";
        }
        equation = buildEquation(tree.getLeft(), equation);
        equation = equation + tree.getValue() + " ";
        equation = buildEquation(tree.getRight(), equation);
        if (tree.getRight() != null){
            equation = equation + ") ";
        }
        return equation;
    }
}
