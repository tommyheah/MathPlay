package my.edu.utar.mathplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComposeActivity extends AppCompatActivity {

    private TextView targetNumberText;
    private LinearLayout selectedNumbersContainer;
    private TextView resultText;
    private Button checkBtn, newNumberBtn, clearBtn;
    private int targetNumber;
    private List<Integer> selectedNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        targetNumberText = findViewById(R.id.targetNumber);
        selectedNumbersContainer = findViewById(R.id.selectedNumbers);
        resultText = findViewById(R.id.result);
        clearBtn = findViewById(R.id.clearBtn);
        checkBtn = findViewById(R.id.checkBtn);
        newNumberBtn = findViewById(R.id.newNumberBtn);

        generateNewTarget();
        setupNumberButtons();


        clearBtn.setOnClickListener(v -> {
            selectedNumbers.clear();
            updateSelectedNumbersDisplay();
        });
        checkBtn.setOnClickListener(v -> checkCombination());
        newNumberBtn.setOnClickListener(v -> generateNewTarget());
    }

    private void generateNewTarget() {
        Random random = new Random();
        targetNumber = random.nextInt(30) + 10; // 10-39
        targetNumberText.setText("Make: " + targetNumber);

        selectedNumbers.clear();
        selectedNumbersContainer.removeAllViews();
        resultText.setText("");
    }

    private void setupNumberButtons() {
        LinearLayout numbersContainer = findViewById(R.id.numbersContainer);
        numbersContainer.removeAllViews();

        // Create buttons for numbers 1-9 with better styling
        for(int i=1; i<=9; i++) {
            Button numBtn = new Button(this);
            numBtn.setText(String.valueOf(i));
            numBtn.setTag(i);

            // Set button styling
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    (int) getResources().getDimension(R.dimen.number_button_width),
                    (int) getResources().getDimension(R.dimen.number_button_height));
            params.setMargins(4, 0, 4, 0);
            numBtn.setLayoutParams(params);

            numBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.number_button_bg));
            numBtn.setTextColor(ContextCompat.getColor(this, R.color.white));
            numBtn.setTextSize(18);

            numBtn.setOnClickListener(v -> {
                int num = (int) v.getTag();
                selectedNumbers.add(num);
                updateSelectedNumbersDisplay();
            });
            numbersContainer.addView(numBtn);
        }
    }

    private void updateSelectedNumbersDisplay() {
        selectedNumbersContainer.removeAllViews();

        for(int num : selectedNumbers) {
            TextView numView = new TextView(this);
            numView.setText(String.valueOf(num));
            numView.setTextSize(20);
            numView.setPadding(8, 8, 8, 8);
            selectedNumbersContainer.addView(numView);

            // Add "+" between numbers except after last
            if(selectedNumbers.indexOf(num) < selectedNumbers.size()-1) {
                TextView plusView = new TextView(this);
                plusView.setText("+");
                plusView.setTextSize(20);
                plusView.setPadding(8, 8, 8, 8);
                selectedNumbersContainer.addView(plusView);
            }
        }
    }

    private void checkCombination() {
        if(selectedNumbers.isEmpty()) {
            resultText.setText("Please select some numbers!");
            resultText.setTextColor(ContextCompat.getColor(this, R.color.red));
            return;
        }

        int sum = 0;
        for(int num : selectedNumbers) {
            sum += num;
        }

        if(sum == targetNumber) {
            resultText.setText("Perfect! " + getEquationText() + " = " + targetNumber);
            resultText.setTextColor(ContextCompat.getColor(this, R.color.green));

            // Delay before next question
            new Handler().postDelayed(() -> {
                generateNewTarget();
                setupNumberButtons();
            }, 2000);
        } else {
            resultText.setText(getEquationText() + " = " + sum + " (Try again!)");
            resultText.setTextColor(ContextCompat.getColor(this, R.color.red));
        }
    }

    private String getEquationText() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<selectedNumbers.size(); i++) {
            sb.append(selectedNumbers.get(i));
            if(i < selectedNumbers.size()-1) {
                sb.append(" + ");
            }
        }
        return sb.toString();
    }
}