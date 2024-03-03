package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Button btnReplaceCal, btnReplaceSelectDevice;
    private PrefUtils prefUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUtils = new PrefUtils(getApplicationContext());

        initBinding();
        initViews();
        initListener();

        disableNavigationBar(this);
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
                prefUtils.setHighestDecibel(String.valueOf(0));

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
