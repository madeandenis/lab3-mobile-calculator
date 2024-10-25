package com.example.lab_java.utils;

import java.util.Stack;
import java.util.regex.Pattern;

public class ExpressionEvaluator {

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public int evaluatePostfixExp(String expression) {
        Stack<Integer> stack = new Stack<>();

        // Split the expression by spaces to handle multi-digit numbers
        String[] tokens = expression.trim().split("\\s+");

        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Integer.parseInt(token));
            } else if (token.length() == 1) {
                char operator = token.charAt(0);

                try {
                    int x = stack.pop();
                    int y = stack.pop();

                    switch (operator) {
                        case '+':
                            stack.push(y + x);
                            break;
                        case '-':
                            stack.push(y - x);
                            break;
                        case '*':
                            stack.push(y * x);
                            break;
                        case '/':
                            stack.push(y / x);
                            break;
                        default:
                            throw new IllegalArgumentException("Illegal operator: " + operator);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid expression format", e);
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression format");
        }

        return stack.pop();
    }
}
