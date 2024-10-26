package com.example.lab_java;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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

    private TextView resultDisplay = null;

    private void clearResultExpression(TextView resultDisplay) {
        String resultText = resultDisplay.getText().toString();
        if (resultText.contains("=")) {
            int equalIndex = resultText.indexOf("=");
            String expressionOnly = resultText.substring(equalIndex + 1).trim();
            resultDisplay.setText(expressionOnly);
        }
    }

    private void onNumpadButtonClick(View view) {
        Button numpadBtn = (Button) view;
        String numpadBtnText = numpadBtn.getText().toString();
        clearResultExpression(resultDisplay);
        resultDisplay.append(numpadBtnText);
    }

    private void onOperatorButtonClick(View view) {
        Button operatorBtn = (Button) view;
        String operatorBtnText = operatorBtn.getText().toString();
        clearResultExpression(resultDisplay);

        if (operatorBtnText.equalsIgnoreCase("c")) {
            resultDisplay.setText("");  // Clears the entire display
        }
        else if (operatorBtnText.equals("=")) {
            String expression = resultDisplay.getText().toString();
            try {
                int result = expressionEvaluator.evaluatePostfixExp(expressionParser.infixToPostfix(expression));

                // Displaying the expression in smaller, semi-transparent text, with the result below
                String formattedResult = "<small><font color='#99000000'>" + expression + " = " + "</font></small><br><font color='#000000'>" + result + "</font>";
                Spanned spannedResult = Html.fromHtml(formattedResult, Html.FROM_HTML_MODE_LEGACY);
                resultDisplay.setText(spannedResult, TextView.BufferType.SPANNABLE);

            } catch (Exception e) {
                String errMessage = "Error: " + e.getMessage();
                resultDisplay.setText(errMessage);
            }
        }
        else {
            String expression = resultDisplay.getText().toString();

            // Only append the operator if the last character is a number
            if (!expression.isEmpty()) {
                char lastChar = expression.charAt(expression.length() - 1);
                if (!expressionEvaluator.isNumeric(String.valueOf(lastChar))) {
                    String replacedOperator = expression.substring(0, expression.length() - 1) + operatorBtnText;
                    resultDisplay.setText(replacedOperator);
                    return;
                }
            }
            resultDisplay.append(operatorBtnText);
        }
    }

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

        int[] numpadIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
        };
        for (int id : numpadIds) {
            findViewById(id).setOnClickListener(this::onNumpadButtonClick);
        }

        int[] operatorIds = {
                R.id.button_add, R.id.button_subtract, R.id.button_multiply, R.id.button_divide,
                R.id.button_clear, R.id.button_equals
        };
        for (int id : operatorIds) {
            findViewById(id).setOnClickListener(this::onOperatorButtonClick);
        }
    }
}
