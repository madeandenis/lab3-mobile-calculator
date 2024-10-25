package com.example.lab_java.utils;

import java.util.Stack;

public class ExpressionParser {

    private boolean isOperand(char c) {
        return (c >= '0' && c <= '9');
    }

    private int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    public String infixToPostfix(String expression) {
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the character is an operand (part of a number)
            if (isOperand(c)) {
                // Append numbers directly to the result
                result.append(c);
                // Check if next character continues the number (multi-digit)
                while (i + 1 < expression.length() && isOperand(expression.charAt(i + 1))) {
                    result.append(expression.charAt(i + 1));
                    i++;
                }
                result.append(" "); // Add space to separate numbers
            }
            // If '(' push to stack
            else if (c == '(') {
                stack.push(c);
            }
            // If ')' encountered, pop until '('
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop()).append(" ");
                }
                stack.pop(); // Remove '(' from stack
            }
            // If an operator, pop based on precedence and push current operator
            else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        // Pop remaining operators from the stack
        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }

        return result.toString().trim(); // Return postfix expression as a string
    }
}
