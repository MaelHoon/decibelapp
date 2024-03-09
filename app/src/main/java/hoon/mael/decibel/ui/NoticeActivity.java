package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Constants.PAGE_CHANGE_INTERVAL;
import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Utils.BluetoothStateUtil;
import hoon.mael.decibel.Utils.PageUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityNoticeBinding;

public class NoticeActivity extends AppCompatActivity {

    private ActivityNoticeBinding binding;
    private ImageView ivIntroBackground;
    private Button btnNext, btnPrev;
    private TextView tvPoliceName;

    private PrefUtils prefUtils;

    private int PageIndex = 1;

    private int TimerCount = 0;

    private Handler UIRefreshTimerHandler = new Handler(
            Looper.getMainLooper()
    );
    private Thread UIRefreshTimerRunnable = new Thread() {
        @Override
        public void run() {
            initValue();

            UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
            UIRefreshTimerHandler.post(this);
        }
    };

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

        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBinding();
        initViews();
        initListener();
        disableNavigationBar(this);

        Intent intent = getIntent();
        int pageIndex = intent.getIntExtra("pageIndex", 1);

        switch (pageIndex) {
            case 3:
                showPage(3);
                break;
            case 4:
                showPage(4);
                break;
        }
    }

    private void initValue() {
        if (BluetoothStateUtil.getToogle()) {
            Intent intent = new Intent(getApplicationContext(),DecibelPageActivity.class);
            intent.putExtra("pageIndex",2);
            startActivity(intent);
            finish();

            BluetoothStateUtil.setToogle(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimerCountHandler.removeCallbacks(TimerCountRunnable);
        TimerCountHandler.postDelayed(TimerCountRunnable,1000);

        UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
        UIRefreshTimerHandler.post(UIRefreshTimerRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimerCountHandler.removeCallbacks(TimerCountRunnable);
        UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void initViews() {
        String policeName = prefUtils.getString("standardInput4") + "경찰서장";
        tvPoliceName.setText(policeName);
    }

    private void initListener() {
        btnNext.setOnClickListener(view -> {

            showNextPage();
        });
        btnPrev.setOnClickListener(view -> {

            showPrevPage();
        });
        binding.layoutPoliceContent.setOnClickListener(view -> {
            PageUtil.startActivity(getApplicationContext(),InputNoticeActivity.class);
        });
    }

    private void initBinding() {
        ivIntroBackground = binding.ivIntroBackground;
        btnNext = binding.layoutBtn.btnNext;
        btnPrev = binding.layoutBtn.btnPrev;

        tvPoliceName = binding.tvPoliceName;
    }

    private void showNextPage() {
        if (PageIndex >= 5) {
            return;
        }
        PageIndex++;
        showPage(PageIndex);
    }

    private void showPrevPage() {
        if (PageIndex <= 1) {
            Intent intent = new Intent(getApplicationContext(), DecibelPageActivity.class);
            intent.putExtra("pageIndex", 3);
            startActivity(intent);
            return;
        }
        PageIndex--;
        showPage(PageIndex);
    }

    private void showPage(int pageIndex) {
        switch (pageIndex) {
            case 1:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud01);
                ivIntroBackground.setVisibility(View.VISIBLE);
                PageIndex = 1;
                break;
            case 2:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud02);
                ivIntroBackground.setVisibility(View.VISIBLE);
                PageIndex = 2;
                break;
            case 3:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud03);
                ivIntroBackground.setVisibility(View.VISIBLE);
                PageIndex = 3;
                break;
            case 4:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud04);
                ivIntroBackground.setVisibility(View.VISIBLE);
                PageIndex = 4;
                break;
            case 5:
                finish();
                PageUtil.startActivity(getApplicationContext(), DeviceSelectActivity.class);
                break;
        }
    }
}
