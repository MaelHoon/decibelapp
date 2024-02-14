package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.databinding.ActivityNoticeBinding;

public class NoticeActivity extends AppCompatActivity {

    private ActivityNoticeBinding binding;
    private PrefUtils prefUtils;

    private TextView tvPoliceName;
    private EditText edtNoticeTitle, edtNoticeContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefUtils = new PrefUtils(getApplicationContext());

        initBinding();
        initViews();
        initListener();

        disableNavigationBar(this);

    }

    private void initViews() {
        String policeName = prefUtils.getString("standardInput4") + "경찰서장";
        String noticeTitle = prefUtils.getString(PrefUtils.NOTICE_TITLE_KEY);
        String noticeContent = prefUtils.getString(PrefUtils.NOTICE_CONTENT_KEY);

        tvPoliceName.setText(policeName);
        edtNoticeTitle.setText(noticeTitle);
        edtNoticeContent.setText(noticeContent);
    }

    private void initListener() {
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
        binding = ActivityNoticeBinding.inflate(LayoutInflater.from(getApplicationContext()));
        setContentView(binding.getRoot());

        tvPoliceName = binding.tvPoliceName;
        edtNoticeTitle = binding.edtNoticeTitle;
        edtNoticeContent = binding.edtNoticeContent;
    }
}
