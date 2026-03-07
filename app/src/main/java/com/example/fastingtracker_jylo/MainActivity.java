package com.example.fastingtracker_jylo;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvFastingTimer, tvWaterStatus, tvQuote;
    private MaterialButton btnFasting;
    private ProgressBar waterProgress;
    
    private boolean isFasting = false;
    private long startTime = 0L;
    private Handler handler = new Handler();
    
    private int waterIntake = 0;
    private final int WATER_GOAL = 3000;

    private String[] quotes = {
        "“Stay anabolic, king.”",
        "“Hydration = gains.”",
        "“The pump is temporary, discipline is forever.”",
        "“Don't break the streak, champ.”",
        "“Your future self will thank you.”"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        tvFastingTimer = findViewById(R.id.tvFastingTimer);
        tvWaterStatus = findViewById(R.id.tvWaterStatus);
        tvQuote = findViewById(R.id.tvQuote);
        btnFasting = findViewById(R.id.btnFasting);
        waterProgress = findViewById(R.id.waterProgress);

        // Fasting Button Logic
        btnFasting.setOnClickListener(v -> toggleFasting());

        // Water Buttons Logic
        findViewById(R.id.btnAdd250).setOnClickListener(v -> addWater(250));
        findViewById(R.id.btnAdd500).setOnClickListener(v -> addWater(500));
        findViewById(R.id.btnAdd1000).setOnClickListener(v -> addWater(1000));

        updateQuote();
    }

    private void toggleFasting() {
        if (!isFasting) {
            isFasting = true;
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(timerRunnable, 0);
            btnFasting.setText("STOP FAST");
            btnFasting.setBackgroundTintList(getColorStateList(R.color.neon_pink));
        } else {
            isFasting = false;
            handler.removeCallbacks(timerRunnable);
            btnFasting.setText("START FAST");
            btnFasting.setBackgroundTintList(getColorStateList(R.color.neon_green));
            updateQuote();
        }
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millisecondTime = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (millisecondTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;

            tvFastingTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };

    private void addWater(int amount) {
        waterIntake += amount;
        if (waterIntake > WATER_GOAL) waterIntake = WATER_GOAL;
        
        waterProgress.setProgress(waterIntake);
        tvWaterStatus.setText(waterIntake + " / " + WATER_GOAL + " ml");
        
        if (waterIntake >= WATER_GOAL) {
            tvQuote.setText("“Hydration Beast status achieved!”");
        }
    }

    private void updateQuote() {
        Random random = new Random();
        tvQuote.setText(quotes[random.nextInt(quotes.length)]);
    }
}