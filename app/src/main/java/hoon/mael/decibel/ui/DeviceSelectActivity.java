package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Utils.PageUtil;
import hoon.mael.decibel.databinding.ActivityMainBinding;
import hoon.mael.decibel.databinding.ActivitySelectDeviceBinding;

public class DeviceSelectActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private ActivitySelectDeviceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDeviceBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
        } else {
            onBackStackChanged();
        }
        initListener();

        disableNavigationBar(this);

        PageUtil.setPage(1);
    }

    private void initListener(){
        binding.layoutBtn.btnNext.setOnClickListener(view ->{
            finish();
            PageUtil.startActivity(getApplicationContext(),InputDecibelActivity.class);
        });
        binding.layoutBtn.btnPrev.setOnClickListener(view ->{
            finish();
            PageUtil.startActivity(getApplicationContext(),MainActivity.class);
        });
    }

    @Override
    public void onBackStackChanged() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

        if (!(currentFragment instanceof TerminalFragment)) {
        }else{
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
