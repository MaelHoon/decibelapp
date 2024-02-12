package hoon.mael.decibel.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.Utils.CalculateUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityCalculateBinding;

public class CalculateActivity extends AppCompatActivity {

    private ActivityCalculateBinding binding;
    private Button btnInit, btnCal;
    private EditText edtBgDecibel, edtMsDecibel;
    private TextView tvInterval, tvCorrection, tvCalResult, tvSum;

    private double interval, correction, sum, result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBinding();
        initViews();
        initListener();
    }

    private void initListener() {
        btnInit.setOnClickListener(view -> {
            tvInterval.setText("");
            tvCorrection.setText("");
            tvCalResult.setText("");
            tvSum.setText("");
        });
        btnCal.setOnClickListener(view -> {
            if (edtBgDecibel.getText().equals("") || edtMsDecibel.getText().equals("")) {
                Toast.makeText(getApplicationContext(), "값을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            calValues();

            tvInterval.setText(String.valueOf(interval));
            tvCorrection.setText(String.valueOf(correction));
            tvSum.setText(String.valueOf(sum));
            tvCalResult.setText(String.valueOf(result));
        });
    }

    private void calValues() {
        double bgDecibel, measureDecibel;
        bgDecibel = Double.parseDouble(edtBgDecibel.getText().toString());
        measureDecibel = Double.parseDouble(edtMsDecibel.getText().toString());

        interval = Math.round((measureDecibel - bgDecibel)*10)/10.0;
        correction = CalculateUtil.calculateCorrection(measureDecibel, bgDecibel);
        sum = measureDecibel + correction;
        result = Math.round(sum);
    }

    private void initViews() {
    }

    private void initBinding() {
        binding = ActivityCalculateBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(binding.getRoot());

        btnInit = binding.btnInit;
        btnCal = binding.btnCal;
        edtBgDecibel = binding.edtBgDecibel;
        edtMsDecibel = binding.edtMeasureDecibel;
        tvInterval = binding.tvInterval;
        tvCorrection = binding.tvCorrection;
        tvCalResult = binding.tvCalResult;
        tvSum = binding.tvSum;
    }
}
