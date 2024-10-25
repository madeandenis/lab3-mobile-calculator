package com.example.lab_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_java.utils.ExpressionEvaluator;
import com.example.lab_java.utils.ExpressionParser;

public class MainActivity extends AppCompatActivity {

    final ExpressionParser expressionParser = new ExpressionParser();
    final ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    private void onNumpadButtonClick(View view) {
        Button numpadBtn = (Button) view;
        String numpadBtnText = numpadBtn.getText().toString();
        resultDisplay.append(numpadBtnText);
    }

    private void onOperatorButtonClick(View view) {
        Button operatorBtn = (Button) view;
        String operatorBtnText = operatorBtn.getText().toString();

        if(operatorBtnText.equalsIgnoreCase("c")){
            resultDisplay.setText("");
        }
        // Parse and evaluate the expression from the display
        else if(operatorBtnText.equals("=")){
            String expression = resultDisplay.getText().toString();
            try {
                int result = expressionEvaluator.evaluatePostfixExp(expressionParser.infixToPostfix(expression));
                resultDisplay.setText(String.valueOf(result));
            } catch (Exception e) {
                String errMessage = "Error: " + e;
                resultDisplay.setText(errMessage);
            }
        }
        else {
            String expression = resultDisplay.getText().toString();

            // Return if the display is empty (no expression to operate on)
            if (expression.isEmpty()) {
                return;
            }

            // Checks if the last character in the display is a number
            char lastChar = expression.charAt(expression.length() - 1);
            if (!expressionEvaluator.isNumeric(String.valueOf(lastChar))){
                // Replaces the last operator with the new operator if the last character is not a number
                String replacedOperator = expression.substring(0, expression.length() - 1) + operatorBtnText;
                resultDisplay.setText(replacedOperator);
                return;
            }

            // Appends the operator to the display if the last character is a number
            resultDisplay.append(operatorBtnText);
        }
    }

    private TextView resultDisplay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Applies padding to the main view to avoid system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultDisplay = findViewById(R.id.resultDisplay);

        // Set click listeners for each number button
        int[] numpadIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
        };
        for(int id : numpadIds) {
            findViewById(id).setOnClickListener(this::onNumpadButtonClick);
        }

        int[] operatorIds = {
                R.id.button_add, R.id.button_subtract, R.id.button_multiply, R.id.button_divide,
                R.id.button_clear, R.id.button_equals
        };
        for(int id : operatorIds) {
            findViewById(id).setOnClickListener(this::onOperatorButtonClick);
        }
    }
}
