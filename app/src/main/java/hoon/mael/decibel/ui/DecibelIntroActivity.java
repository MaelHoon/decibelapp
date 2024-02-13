package hoon.mael.decibel.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Utils.BluetoothStateUtil;
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

    private Handler UIRefreshTimerHandler = new Handler(
            Looper.getMainLooper()
    );
    private Thread UIRefreshTimerRunnable = new Thread() {
        @Override
        public void run() {
            getDecibel();

            UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
            UIRefreshTimerHandler.post(this);
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
    }
    @Override
    protected void onResume() {
        super.onResume();

        initComponent();

        UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
        UIRefreshTimerHandler.post(UIRefreshTimerRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
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

        btnPrev = binding.btnPrev;
        btnNext = binding.btnNext;
        tvPoliceName = binding.tvPoliceName;
        tvPoliceName.setText(policeName);

        ivIntroBackground = binding.ivIntroBackground;
    }

    private void initListener() {
        btnPrev.setOnClickListener(view -> {
            btnClickCount++;

            if (btnClickCount == 2) {
                btnClickCount = 0;  // 초기화

                Intent intent = new Intent(getApplicationContext(), DecibelPageActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 첫 번째 클릭 후 500ms 내에 다시 클릭되지 않으면 초기화
                new Handler().postDelayed(() -> btnClickCount = 0, 500);
                if (isOverThr) {
                    new Handler().postDelayed(this::showPrevPage, 500);
                }
            }
        });
        btnNext.setOnClickListener(view -> {
            btnClickCount++;

            if (btnClickCount == 2) {
                btnClickCount = 0;  // 초기화

                Intent intent = new Intent(getApplicationContext(), DecibelPageActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 첫 번째 클릭 후 500ms 내에 다시 클릭되지 않으면 초기화
                new Handler().postDelayed(() -> btnClickCount = 0, 500);
                if (isOverThr) {
                    new Handler().postDelayed(this::showNextPage, 500);
                }
            }
        });

        tvPoliceName.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(),NoticeActivity.class);
            startActivity(intent);
        });
    }

    private void getDecibel() {
        currentDecibel = DecibelModel.getCurrentDecibel();
        averageDecibel = DecibelModel.getAverageDecibel();

        if(BluetoothStateUtil.getReceiveStatus()) {
            if (isStandardOver(currentDecibel, standardMaxDecibel)) {
                showThrOverPage(pageIndex);
            } else {
                hideThrOverPage();
            }
        }else{
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
        ivIntroBackground.setImageResource( R.drawable.img_decibel_intro_backgroud);
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
