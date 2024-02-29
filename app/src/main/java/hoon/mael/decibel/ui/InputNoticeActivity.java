package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Constants.PAGE_CHANGE_INTERVAL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.Utils.BluetoothStateUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityInputNoticeBinding;

public class InputNoticeActivity extends AppCompatActivity {
    private ActivityInputNoticeBinding binding;
    private PrefUtils prefUtils;

    private EditText edtNoticeTitle, edtNoticeContent;
    private TextView tvPoliceName;

    private int TimerCount = 1;

    private Handler TimerCountHandler = new Handler(
            Looper.getMainLooper()
    );
    private Thread TimerCountRunnable = new Thread() {
        @Override
        public void run() {
            if (BluetoothStateUtil.getBleState() == BluetoothStateUtil.BLE_STATE_STOP) {
                TimerCount++;
                if (TimerCount >= PAGE_CHANGE_INTERVAL) {
                    TimerCount = 0;
                    finish();
                    Intent intent = new Intent(getApplicationContext(), DecibelPageActivity.class);
                    intent.putExtra("pageIndex", 2);
                    startActivity(intent);
                }
            }
            TimerCountHandler.removeCallbacks(TimerCountRunnable);
            TimerCountHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefUtils = new PrefUtils(getApplicationContext());

        initBinding();
        initListener();
    }

    private void initBinding(){
        binding = ActivityInputNoticeBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(binding.getRoot());

        String policeName = prefUtils.getString("standardInput4") + "경찰서장";
        String noticeTitle = prefUtils.getString(PrefUtils.NOTICE_TITLE_KEY);
        String noticeContent = prefUtils.getString(PrefUtils.NOTICE_CONTENT_KEY);

        edtNoticeTitle = binding.edtNoticeTitle;
        edtNoticeContent = binding.edtNoticeContent;
        tvPoliceName = binding.tvPoliceName;

        tvPoliceName.setText(policeName);
        edtNoticeTitle.setText(noticeTitle);
        edtNoticeContent.setText(noticeContent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TimerCountHandler.removeCallbacks(TimerCountRunnable);
        TimerCountHandler.post(TimerCountRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TimerCountHandler.removeCallbacks(TimerCountRunnable);
    }

    private void initListener(){
        edtNoticeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                prefUtils.saveString(PrefUtils.NOTICE_TITLE_KEY, edtNoticeTitle.getText().toString());
            }
        });
        edtNoticeContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                prefUtils.saveString(PrefUtils.NOTICE_CONTENT_KEY, edtNoticeContent.getText().toString());
            }
        });
        binding.layoutPoliceContent.setOnClickListener(view ->{
            finish();
        });
    }
}
