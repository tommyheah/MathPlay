package my.edu.utar.mathplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderActivity extends AppCompatActivity {

    private LinearLayout numbersContainer, dropArea;
    private Button checkBtn, ascendingBtn, descendingBtn, removeBtn;
    private TextView instructionText;
    private boolean isAscending = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        numbersContainer = findViewById(R.id.numbersContainer);
        dropArea = findViewById(R.id.dropArea);
        checkBtn = findViewById(R.id.checkBtn);
        ascendingBtn = findViewById(R.id.ascendingBtn);
        descendingBtn = findViewById(R.id.descendingBtn);
        removeBtn = findViewById(R.id.removeBtn);
        instructionText = findViewById(R.id.instruction);

        // Initialize the drag listener for the drop area
        dropArea.setOnDragListener(new MyDragListener());

        setupOrderMode(true);
        generateNumbers();

        ascendingBtn.setOnClickListener(v -> setupOrderMode(true));
        descendingBtn.setOnClickListener(v -> setupOrderMode(false));
        removeBtn.setOnClickListener(v -> removeLastNumber());
        checkBtn.setOnClickListener(v -> checkOrder());
    }

    private void setupOrderMode(boolean ascending) {
        isAscending = ascending;
        if(ascending) {
            instructionText.setText("Drag numbers in ASCENDING order");
            ascendingBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            descendingBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        } else {
            instructionText.setText("Drag numbers in DESCENDING order");
            descendingBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            ascendingBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        }

        dropArea.removeAllViews();
        generateNumbers();
    }

    private void removeLastNumber() {
        if (dropArea.getChildCount() > 0) {
            dropArea.removeViewAt(dropArea.getChildCount() - 1);
        } else {
            Toast.makeText(this, "No numbers to remove", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateNumbers() {
        numbersContainer.removeAllViews();
        Random random = new Random();
        List<Integer> numbers = new ArrayList<>();

        // Generate 5 unique random numbers
        for(int i=0; i<5; i++) {
            int num = random.nextInt(1000);
            while(numbers.contains(num)) {
                num = random.nextInt(1000);
            }
            numbers.add(num);
        }

        // Shuffle the numbers
        Collections.shuffle(numbers);

        // Create draggable number views
        for(int num : numbers) {
            TextView numberView = new TextView(this);
            numberView.setText(String.valueOf(num));
            numberView.setTextSize(24);
            numberView.setTextColor(ContextCompat.getColor(this, R.color.black));
            numberView.setPadding(16, 16, 16, 16);
            numberView.setBackground(ContextCompat.getDrawable(this, R.drawable.number_bg));

            // Set drag listener
            numberView.setOnTouchListener(new MyTouchListener());
            numbersContainer.addView(numberView);
        }
    }

    private void checkOrder() {
        boolean isCorrect = true;
        int previous = isAscending ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // First check if all numbers have been dragged over
        if (dropArea.getChildCount() != 5) {
            Toast.makeText(this, "Please arrange all 5 numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check the order
        for (int i = 0; i < dropArea.getChildCount(); i++) {
            TextView child = (TextView) dropArea.getChildAt(i);
            int current = Integer.parseInt(child.getText().toString());

            if ((isAscending && current < previous) || (!isAscending && current > previous)) {
                isCorrect = false;
                break;
            }
            previous = current;
        }

        if (isCorrect) {
            Toast.makeText(this, "Correct! Well done!", Toast.LENGTH_SHORT).show();

            // Clear the drop area with a slight delay
            new Handler().postDelayed(() -> {
                dropArea.removeAllViews();
                generateNumbers(); // Generate new numbers
            }, 1000); // 1 second delay before clearing

        } else {
            Toast.makeText(this, "Not quite right. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Create a shadow of the view being dragged
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                // Start the drag - the third parameter is the local state (the original view)
                view.startDrag(data, shadowBuilder, view, 0);

                // Make the original view invisible during drag
                view.setVisibility(View.INVISIBLE);
                return true;
            }
            return false;
        }
    }

    public void onDragNumber(View view) {
        dropArea.setOnDragListener(new MyDragListener());
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    // Get the original dragged view
                    View draggedView = (View) event.getLocalState();
                    TextView dropped = (TextView) draggedView;

                    // Make the original view visible again (since we hid it on drag start)
                    draggedView.setVisibility(View.VISIBLE);

                    // Create a new copy for the drop area
                    TextView copy = new TextView(OrderActivity.this);
                    copy.setText(dropped.getText());
                    copy.setTextSize(24);
                    copy.setPadding(16, 16, 16, 16);
                    copy.setBackground(ContextCompat.getDrawable(OrderActivity.this, R.drawable.number_bg));

                    // Don't set the touch listener on the copy - we don't want it draggable
                    dropArea.addView(copy);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    // If the drop wasn't handled, make sure the original view becomes visible again
                    if (!event.getResult()) {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                    }
                    return true;

                default:
                    return true;
            }
        }
    }
}