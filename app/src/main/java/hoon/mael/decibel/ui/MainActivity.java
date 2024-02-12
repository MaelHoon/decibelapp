package hoon.mael.decibel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Button btnReplaceCal, btnReplaceSelectDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBinding();
        initViews();
        initListener();

        setContentView(binding.getRoot());
    }

    private void initViews() {

    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(LayoutInflater.from(getApplicationContext()));

        btnReplaceCal = binding.btnReplaceCalcuate;
        btnReplaceSelectDevice = binding.btnReplaceSelectDevice;
    }

    private void initListener() {
        btnReplaceSelectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(DeviceSelectActivity.class);
            }
        });
        btnReplaceCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(CalculateActivity.class);
            }
        });
    }

    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        startActivity(intent);
    }
}
