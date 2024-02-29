package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Constants.PAGE_CHANGE_INTERVAL;
import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Utils.BluetoothStateUtil;
import hoon.mael.decibel.Utils.PageUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityDecibelIntroBinding;
import hoon.mael.decibel.model.DecibelModel;

public class DecibelIntroActivity extends AppCompatActivity {

    private ActivityDecibelIntroBinding binding;
    private GestureDetector gestureDetector;
    private Button btnPrev, btnNext;
    private ImageView ivIntroBackground;
    private TextView tvPoliceName;

    private PrefUtils prefUtils;

    private int pageIndex = 1;
    private boolean isOverThr = false;
    private int btnClickCount = 0;

    private long backKeyPressedTime = 0;// 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private Toast toast;

    private int TimerCount = 0;

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

    private String standardMaxDecibel, standardThrDecibel, currentDecibel, averageDecibel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDecibelIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Intent intent = new Intent(getApplicationContext(), DecibelPageActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
        prefUtils = new PrefUtils(getApplicationContext());

        initComponent();
        initListener();

        disableNavigationBar(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        initComponent();

        TimerCountHandler.removeCallbacks(TimerCountRunnable);
        TimerCountHandler.postDelayed(TimerCountRunnable,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TimerCountHandler.removeCallbacks(TimerCountRunnable);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponent() {
        standardMaxDecibel = prefUtils.getString("standardInput3");
        standardThrDecibel = prefUtils.getString("standardInput2");
        String policeName = prefUtils.getString("standardInput4") + "경찰서장";

        binding.getRoot().setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        btnPrev = binding.layoutBtn.btnPrev;
        btnNext = binding.layoutBtn.btnNext;
        tvPoliceName = binding.tvPoliceName;
        tvPoliceName.setText(policeName);

        ivIntroBackground = binding.ivIntroBackground;
    }

    private void initListener() {
        btnPrev.setOnClickListener(view -> {
            finish();
            PageUtil.startActivity(getApplicationContext(), InputDecibelActivity.class);
        });
        btnNext.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), DecibelPageActivity.class);
            intent.putExtra("pageIndex", 1);

            startActivity(intent);
        });
        binding.layoutPoliceContent.setOnClickListener(view -> {
            PageUtil.startActivity(getApplicationContext(), InputNoticeActivity.class);
        });
    }

    private void getDecibel() {
        currentDecibel = DecibelModel.getCurrentDecibel();
        averageDecibel = DecibelModel.getAverageDecibel();

        if (BluetoothStateUtil.getReceiveStatus()) {
            if (isStandardOver(currentDecibel, standardMaxDecibel)) {
                showThrOverPage(pageIndex);
            } else {
                hideThrOverPage();
            }
        } else {
            hideThrOverPage();
        }
    }

    private void showThrOverPage(int index) {
        switch (index) {
            case 1:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud01);
                break;
            case 2:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud02);
                break;
            case 3:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud03);
                break;
            case 4:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud04);
                break;
        }
    }

    private void hideThrOverPage() {
        ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_backgroud);
    }

    private void showNextPage() {
        if (pageIndex >= 4) {
            return;
        }
        pageIndex++;
        showThrOverPage(pageIndex);
    }

    private void showPrevPage() {
        if (pageIndex <= 1) {
            return;
        }
        pageIndex--;
        showThrOverPage(pageIndex);
    }

    private Boolean isStandardOver(String currentDecibel, String standardMaxDecibel) {
        try {
            if (standardMaxDecibel == null || currentDecibel == null) {
                isOverThr = false;
                return false;
            }
            double currentDecibelValue = Double.parseDouble(currentDecibel);
            double standardMaxDecibelValue = Double.parseDouble(standardMaxDecibel);

            isOverThr = currentDecibelValue > standardMaxDecibelValue;
            return currentDecibelValue > standardMaxDecibelValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        isOverThr = false;
        return false;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }

}
