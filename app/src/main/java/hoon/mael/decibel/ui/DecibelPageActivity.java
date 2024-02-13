package hoon.mael.decibel.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Utils.BluetoothStateUtil;
import hoon.mael.decibel.Utils.CalculateUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityDecibelPageBinding;
import hoon.mael.decibel.model.DecibelModel;

public class DecibelPageActivity extends AppCompatActivity {

    private ActivityDecibelPageBinding binding;
    private Button btnPrev, btnNext;
    private TextView tvStdMaxDecibel, tvStdThrDecibel, tvCurrentDecibel, tvPoliceName1;
    private TextView tvCurrentDecibel2, tvAverageDecibel2, tvPoliceName2;
    private TextView tvCurrentDecibel3, tvAverageDecibel3, tvPoliceName3;
    private TextView tvStandardThrDecibel, tvStandardMaxDecibel;
    private TextView tvCurrentDecibelResultEval, tvStandardDecibelResultEval;
    private TextView tvReceiveEndString02, tvReceiveEndString01;

    private View decibelPage01, decibelPage02, decibelPage03;
    private PrefUtils prefUtils;
    private int btnClickCount = 0;
    private int pageIndex = 1;
    private Timer UIUpdateTimer;
    private boolean IsUIUpdateTimerRunning = false;

    private String standardMaxDecibel, standardThrDecibel, currentDecibel, averageDecibel;
    private String standardBackgroundDecibel;

    private long backKeyPressedTime = 0;// 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private Toast toast;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDecibelPageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        prefUtils = new PrefUtils(getApplicationContext());

        initComponent();
        initListener();
    }


    @Override
    protected void onResume() {
        super.onResume();

        UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
        UIRefreshTimerHandler.post(UIRefreshTimerRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIRefreshTimerHandler.removeCallbacks(UIRefreshTimerRunnable);
    }

    private void initComponent() {
        decibelPage01 = binding.layoutPageDecibel01.getRoot();
        decibelPage02 = binding.layoutPageDecibel02.getRoot();
        decibelPage03 = binding.layoutPageDecibel03.getRoot();

        btnPrev = binding.btnPrev;
        btnNext = binding.btnNext;

        tvStdMaxDecibel = binding.layoutPageDecibel01.tvStdMaxDecibel;
        tvStdThrDecibel = binding.layoutPageDecibel01.tvStdThrDecibel;
        tvCurrentDecibel = binding.layoutPageDecibel01.tvCurrentDecibel;

        tvPoliceName1 = binding.layoutPageDecibel01.tvPoliceName;
        tvPoliceName2 = binding.layoutPageDecibel02.tvPoliceName;
        tvPoliceName3 = binding.layoutPageDecibel03.tvPoliceName;

        tvReceiveEndString01 = binding.layoutPageDecibel01.tvReceiveEndString01;

        tvCurrentDecibel2 = binding.layoutPageDecibel02.tvCurrentDecibel2;
        tvAverageDecibel2 = binding.layoutPageDecibel02.tvAverageDecibel2;
        tvReceiveEndString02 = binding.layoutPageDecibel02.tvReceiveEndString02;

        tvStandardThrDecibel = binding.layoutPageDecibel02.tvStandardThrDecibel;
        tvStandardMaxDecibel = binding.layoutPageDecibel02.tvStandardMaxDecibel;

        tvCurrentDecibel3 = binding.layoutPageDecibel03.tvCurrentDecibelResult;
        tvAverageDecibel3 = binding.layoutPageDecibel03.tvStandardDecibelResult;
        tvCurrentDecibelResultEval = binding.layoutPageDecibel03.tvCurrentDecibelResultEval;
        tvStandardDecibelResultEval = binding.layoutPageDecibel03.tvStandardDecibelResultEval;

        String policeName = prefUtils.getString("standardInput4") + "경찰서장";
        standardMaxDecibel = prefUtils.getString("standardInput3");
        standardThrDecibel = prefUtils.getString("standardInput2");
        standardBackgroundDecibel = prefUtils.getString("standardInput1");

        binding.layoutPageDecibel03.tvStandardMaxDecibel2.setText(standardMaxDecibel);
        binding.layoutPageDecibel03.tvStandardThrDecibel2.setText(standardThrDecibel);

        tvPoliceName1.setText(policeName);
        tvPoliceName2.setText(policeName);
        tvPoliceName3.setText(policeName);

        tvStandardMaxDecibel.setText(standardMaxDecibel);
        tvStandardThrDecibel.setText(standardThrDecibel);
        tvStdMaxDecibel.setText(standardMaxDecibel);
        tvStdThrDecibel.setText(standardThrDecibel);
    }

    private void initValue() {
        currentDecibel = DecibelModel.getCurrentDecibel();
        averageDecibel = DecibelModel.getAverageDecibel();

        if (!BluetoothStateUtil.getReceiveStatus()) {
            tvReceiveEndString01.setVisibility(View.VISIBLE);
            tvReceiveEndString02.setVisibility(View.VISIBLE);

            tvReceiveEndString01.setText(BluetoothStateUtil.getReceiveEndString());
            tvReceiveEndString02.setText(BluetoothStateUtil.getReceiveEndString());
        } else {
            tvReceiveEndString01.setVisibility(View.GONE);
            tvReceiveEndString02.setVisibility(View.GONE);
        }

        if (currentDecibel == null || averageDecibel == null) {
            return;
        }

        try { //기기 데시벨 측정 시작시 Leq값 수신 방지
            if (Double.parseDouble(currentDecibel) > Double.parseDouble(standardMaxDecibel)) {
                tvCurrentDecibelResultEval.setText("초과");
                tvCurrentDecibelResultEval.setTextColor(getResources().getColor(R.color.colorWarning));
            } else {
                tvCurrentDecibelResultEval.setText("준수");
                tvCurrentDecibelResultEval.setTextColor(getResources().getColor(R.color.btnColor01));
            }

            if (Double.parseDouble(averageDecibel) > Double.parseDouble(standardThrDecibel)) {
                tvStandardDecibelResultEval.setText("초과");
                tvStandardDecibelResultEval.setTextColor(getResources().getColor(R.color.colorWarning));
            } else {
                tvStandardDecibelResultEval.setText("준수");
                tvStandardDecibelResultEval.setTextColor(getResources().getColor(R.color.btnColor01));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Double correctionDecibelValue = CalculateUtil.calculateCorrection((Double.parseDouble(averageDecibel)), Double.parseDouble(standardBackgroundDecibel));

            int currentDecibelRound = (int) (Math.round(Double.parseDouble(currentDecibel)));
            int AverageDecibelRound = (int) (Math.round((Double.parseDouble(averageDecibel)) - correctionDecibelValue));

            tvCurrentDecibel.setText(currentDecibel);
            tvCurrentDecibel2.setText(currentDecibel);
            tvAverageDecibel2.setText(averageDecibel);
            tvCurrentDecibel3.setText(String.valueOf(currentDecibelRound));
            tvAverageDecibel3.setText(String.valueOf(AverageDecibelRound));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BluetoothStateUtil.getReceiveStatus()) {
            if (isStandardOver(currentDecibel, standardMaxDecibel)) {
                setBackgroundWarning();

                runOverTask();
            } else {
                setBackgroundDefault();

                stopOverTask();
            }
        }else{
            stopOverTask();

            setBackgroundDefault();
            showCurrentPage();
        }
    }

    private void setBackgroundWarning() {
        int color = ContextCompat.getColor(getApplicationContext(), R.color.colorWarning);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        binding.layoutPageDecibel01.getRoot().setBackground(colorDrawable);
        binding.layoutPageDecibel02.getRoot().setBackground(colorDrawable);
    }

    private void setBackgroundDefault() {
        int color = ContextCompat.getColor(getApplicationContext(), R.color.background_default);
        ColorDrawable colorDrawable = new ColorDrawable(color);
        binding.layoutPageDecibel01.getRoot().setBackground(colorDrawable);
        binding.layoutPageDecibel02.getRoot().setBackground(colorDrawable);
    }

    private void showCurrentPage() {
        switch (pageIndex) {
            case 1:
                decibelPage01.setVisibility(View.VISIBLE);
                decibelPage02.setVisibility(View.GONE);
                decibelPage03.setVisibility(View.GONE);
                break;
            case 2:
                decibelPage01.setVisibility(View.GONE);
                decibelPage02.setVisibility(View.VISIBLE);
                decibelPage03.setVisibility(View.GONE);
                break;
        }
    }

    private Boolean isStandardOver(String currentDecibel, String standardMaxDecibel) {
        try {
            if (standardMaxDecibel == null || currentDecibel == null) {
                return false;
            }
            double currentDecibelValue = Double.parseDouble(currentDecibel);
            double standardMaxDecibelValue = Double.parseDouble(standardMaxDecibel);

            return currentDecibelValue > standardMaxDecibelValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initListener() {
        btnPrev.setOnClickListener(view -> {
            btnClickCount++;
            if (btnClickCount == 2) {
                btnClickCount = 0;  // 초기화

                Intent intent = new Intent(getApplicationContext(), DecibelIntroActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 첫 번째 클릭 후 500ms 내에 다시 클릭되지 않으면 초기화
                new Handler().postDelayed(() -> btnClickCount = 0, 500);
                new Handler().postDelayed(this::updateDecibelPagePrev, 500);
            }
        });

        btnNext.setOnClickListener(view -> {
            btnClickCount++;
            if (btnClickCount == 2) {
                btnClickCount = 0;  // 초기화

                Intent intent = new Intent(getApplicationContext(), DecibelIntroActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 첫 번째 클릭 후 500ms 내에 다시 클릭되지 않으면 초기화
                new Handler().postDelayed(() -> btnClickCount = 0, 500);
                new Handler().postDelayed(this::updateDecibelPageNext, 500);
            }
        });
    }

    private void runOverTask() {
        if (IsUIUpdateTimerRunning) {
            return;
        }

        UIUpdateTimer = new Timer();

        UIUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (decibelPage03.getVisibility() == View.VISIBLE) {
                            showCurrentPage();
                        } else {
                            decibelPage01.setVisibility(View.GONE);
                            decibelPage02.setVisibility(View.GONE);
                            decibelPage03.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }, 5000, 5000);

        IsUIUpdateTimerRunning = true;
    }

    private void stopOverTask() {
        if (!IsUIUpdateTimerRunning) {
            return;
        }

        UIUpdateTimer.cancel();
        UIUpdateTimer = null;

        IsUIUpdateTimerRunning = false;
        showCurrentPage();
    }

    private void updateDecibelPagePrev() {
        switch (getDecibelPageIndex()) {
            case 2:
                decibelPage01.setVisibility(View.VISIBLE);
                decibelPage02.setVisibility(View.GONE);
                break;
        }
        getDecibelPageIndex();
    }

    private void updateDecibelPageNext() {
        switch (getDecibelPageIndex()) {
            case 1:
                decibelPage01.setVisibility(View.GONE);
                decibelPage02.setVisibility(View.VISIBLE);
                break;
        }
        getDecibelPageIndex();
    }

    private int getDecibelPageIndex() {
        boolean isDecibelPage01Visible = (decibelPage01.getVisibility() == View.VISIBLE);
        boolean isDecibelPage02Visible = (decibelPage02.getVisibility() == View.VISIBLE);

        if (isDecibelPage01Visible && !isDecibelPage02Visible) {
            pageIndex = 1;
            return 1;
        } else if (!isDecibelPage01Visible && isDecibelPage02Visible) {
            pageIndex = 2;
            return 2;
        } else {
            pageIndex = 1;
            return 1;
        }
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
