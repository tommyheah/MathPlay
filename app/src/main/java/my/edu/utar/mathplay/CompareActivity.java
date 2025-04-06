package my.edu.utar.mathplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class CompareActivity extends AppCompatActivity {


    private TextView num1Text, num2Text, resultText;
    private Button greaterBtn, lessBtn;
    private int num1, num2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        num1Text = findViewById(R.id.num1);
        num2Text = findViewById(R.id.num2);
        resultText = findViewById(R.id.result);
        greaterBtn = findViewById(R.id.greaterBtn);
        lessBtn = findViewById(R.id.lessBtn);

        generateNewNumbers();

        greaterBtn.setOnClickListener(v -> checkAnswer(true));
        lessBtn.setOnClickListener(v -> checkAnswer(false));
    }

    private void generateNewNumbers() {
        Random random = new Random();
        num1 = random.nextInt(1000);
        num2 = random.nextInt(1000);

        // Ensure numbers are different
        while(num1 == num2) {
            num2 = random.nextInt(1000);
        }

        num1Text.setText(String.valueOf(num1));
        num2Text.setText(String.valueOf(num2));
        resultText.setText("");
    }

    private void checkAnswer(boolean isGreater) {
        boolean correct = (isGreater && num1 > num2) || (!isGreater && num1 < num2);

        if(correct) {
            resultText.setText("Correct! Good job!");
            resultText.setTextColor(ContextCompat.getColor(this, R.color.green));

            // Delay before next question
            new Handler().postDelayed(this::generateNewNumbers, 1500);
        } else {
            resultText.setText("Try again!");
            resultText.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
    }
}