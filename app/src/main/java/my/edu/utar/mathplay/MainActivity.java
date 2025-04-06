package my.edu.utar.mathplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCompare = findViewById(R.id.btnCompare);
        Button btnOrder = findViewById(R.id.btnOrder);
        Button btnCompose = findViewById(R.id.btnCompose);

        btnCompare.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CompareActivity.class);
            startActivity(intent);
        });

        btnOrder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        btnCompose.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ComposeActivity.class);
            startActivity(intent);
        });
    }
}