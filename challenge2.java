import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class ArithmeticExpressionSolver {
    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outputFile = "output.txt";
        BufferedReader reader;
        BufferedWriter writer;
        
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(outputFile));
            String line = reader.readLine();
            
            while (line != null) {
                String[] tokens = line.split(" ");
                Stack<Double> operands = new Stack<Double>();
                Stack<Character> operators = new Stack<Character>();
                
                for (String token : tokens) {
                    char c = token.charAt(0);
                    if (Character.isDigit(c) || c == '.') {
                        operands.push(Double.parseDouble(token));
                    } else if (isOperator(c)) {
                        while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                            double result = applyOperation(operators.pop(), operands.pop(), operands.pop());
                            operands.push(result);
                        }
                        operators.push(c);
                    } else if (c == '(' || c == '[' || c == '{') {
                        operators.push(c);
                    } else if (c == ')' || c == ']' || c == '}') {
                        while (!operators.isEmpty() && !isOpeningBracket(operators.peek())) {
                            double result = applyOperation(operators.pop(), operands.pop(), operands.pop());
                            operands.push(result);
                        }
                        operators.pop();
                    }
                }
                
                while (!operators.isEmpty()) {
                    double result = applyOperation(operators.pop(), operands.pop(), operands.pop());
                    operands.push(result);
                }
                
                writer.write(line + " " + operands.pop());
                writer.newLine();
                line = reader.readLine();
            }
            
            reader.close();
            writer.close();
            System.out.println("Successfully solved arithmetic expressions and generated output file.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    
    private static boolean isOpeningBracket(char c) {
        return c == '(' || c == '[' || c == '{';
    }
    
    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == '[' || op2 == '{') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        if (op1 == '^' && (op2 == '+' || op2 == '-' || op2 == '*' || op2 == '/')) {
            return false;
        }
        return true;
    }
    
    private static double applyOperation(char operator, double operand2, double operand1) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero.");
                }
                return operand1 / operand2;
            case '^':
                return Math.pow(operand1, operand2);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
