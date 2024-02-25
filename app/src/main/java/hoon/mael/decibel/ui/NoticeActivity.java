package hoon.mael.decibel.ui;

import android.content.Intent;
import android.os.Bundle;
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
import hoon.mael.decibel.Utils.PageUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityNoticeBinding;

public class NoticeActivity extends AppCompatActivity {

    private ActivityNoticeBinding binding;
    private ImageView ivIntroBackground;
    private Button btnNext, btnPrev;
    private TextView tvPoliceName;
    private EditText edtNoticeTitle, edtNoticeContent;

    private View inputNoticePage;
    private PrefUtils prefUtils;

    private int pageIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefUtils = new PrefUtils(getApplicationContext());

        binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBinding();
        initViews();
        initListener();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void initViews(){
        String policeName = prefUtils.getString("standardInput4") + "경찰서장";
        String noticeTitle = prefUtils.getString(PrefUtils.NOTICE_TITLE_KEY);
        String noticeContent = prefUtils.getString(PrefUtils.NOTICE_CONTENT_KEY);

        tvPoliceName.setText(policeName);
        edtNoticeTitle.setText(noticeTitle);
        edtNoticeContent.setText(noticeContent);
    }

    private void initListener() {
        btnNext.setOnClickListener(view ->{
            showNextPage();
        });
        btnPrev.setOnClickListener(view ->{
            showPrevPage();
        });
        tvPoliceName.setOnClickListener(view -> {
            finish();
        });
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
    }

    private void initBinding() {
        inputNoticePage = binding.layoutInputNotice.getRoot();

        ivIntroBackground = binding.ivIntroBackground;
        btnNext = binding.layoutBtn.btnNext;
        btnPrev = binding.layoutBtn.btnPrev;

        tvPoliceName = binding.layoutInputNotice.tvPoliceName;
        edtNoticeTitle = binding.layoutInputNotice.edtNoticeTitle;
        edtNoticeContent = binding.layoutInputNotice.edtNoticeContent;
    }

    private void showNextPage() {
        if (pageIndex >= 5) {
            return;
        }
        pageIndex++;
        showPage(pageIndex);
    }

    private void showPrevPage() {
        if (pageIndex <= 1) {
            Intent intent = new Intent(getApplicationContext(),DecibelPageActivity.class);
            intent.putExtra("pageIndex",3);
            startActivity(intent);
            return;
        }
        pageIndex--;
        showPage(pageIndex);
    }

    private void showPage(int pageIndex) {
        switch (pageIndex) {
            case 1:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud02);
                inputNoticePage.setVisibility(View.GONE);
                break;
            case 2:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud03);
                inputNoticePage.setVisibility(View.GONE);
                break;
            case 3:
                ivIntroBackground.setImageResource(R.drawable.img_decibel_intro_over_backgroud04);
                inputNoticePage.setVisibility(View.GONE);
                break;
            case 4:
                inputNoticePage.setVisibility(View.VISIBLE);
                break;
            case 5:
                finish();
                PageUtil.startActivity(getApplicationContext(),InputDecibelActivity.class);
                break;
        }
    }
}
