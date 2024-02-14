package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.Utils.MessageUtils;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityInputDecibelBinding;

public class InputDecibelActivity extends AppCompatActivity {

    private ActivityInputDecibelBinding binding;
    private Button btnConfirm;
    private EditText edtStandard1, edtStandard2, edtStandard3, edtStandard4;

    private PrefUtils prefUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInputDecibelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Intent intent = getIntent();
        String instantDecibel = intent.getStringExtra("asd");
        String averageDecibel = intent.getStringExtra("asd2");*/

        initComponent();
        initValue();
        initListener();

        disableNavigationBar(this);

    }

    private void initComponent() {
        edtStandard1 = binding.edtStandard1;
        edtStandard2 = binding.edtStandard2;
        edtStandard3 = binding.edtStandard3;
        edtStandard4 = binding.edtStandard4;
        btnConfirm = binding.btnConfirm;
    }

    private void initListener() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputComplete()) {
                    MessageUtils.showToastMassage(getApplicationContext(), "항목을 모두 채워 주세요!");
                    return;
                }

                saveInputValue();

                Intent intent = new Intent(getApplicationContext(),DecibelIntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void saveInputValue() {
        String value1 = edtStandard1.getText().toString();
        String value2 = edtStandard2.getText().toString();
        String value3 = edtStandard3.getText().toString();
        String value4 = edtStandard4.getText().toString();

        prefUtils.saveString("standardInput1", value1);
        prefUtils.saveString("standardInput2", value2);
        prefUtils.saveString("standardInput3", value3);
        prefUtils.saveString("standardInput4", value4);
    }

    private void initValue() {
        prefUtils = new PrefUtils(getApplicationContext());

        edtStandard1.setText(prefUtils.getString("standardInput1"));
        edtStandard2.setText(prefUtils.getString("standardInput2"));
        edtStandard3.setText(prefUtils.getString("standardInput3"));
        edtStandard4.setText(prefUtils.getString("standardInput4"));
    }

    private boolean isInputComplete() {
        String value1 = edtStandard1.getText().toString();
        String value2 = edtStandard2.getText().toString();
        String value3 = edtStandard3.getText().toString();
        String value4 = edtStandard4.getText().toString();

        // 하나라도 비어 있다면 false 반환
        // 모두 값이 있다면 true 반환
        return !TextUtils.isEmpty(value1) && !TextUtils.isEmpty(value2) && !TextUtils.isEmpty(value3) && !TextUtils.isEmpty(value4);
    }
}
