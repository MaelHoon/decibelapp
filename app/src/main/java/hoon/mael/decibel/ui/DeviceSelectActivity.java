package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Constants.PAGE_INDEX;
import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Utils.BluetoothStateUtil;
import hoon.mael.decibel.Utils.PageUtil;
import hoon.mael.decibel.databinding.ActivityMainBinding;
import hoon.mael.decibel.databinding.ActivitySelectDeviceBinding;
import hoon.mael.decibel.databinding.DeviceListHeaderBinding;
import hoon.mael.decibel.model.DecibelModel;

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
    }

    private void initListener(){
        binding.layoutBtn.btnNext.setOnClickListener(view ->{
            finish();
            PageUtil.startActivity(getApplicationContext(),InputDecibelActivity.class);
        });
        binding.layoutBtn.btnPrev.setOnClickListener(view ->{
            finish();
            Intent intent = new Intent(getApplicationContext(),NoticeActivity.class);
            intent.putExtra(PAGE_INDEX,4);
            startActivity(intent);
        });
        binding.imgPolice.setOnClickListener(view ->{
            finishAffinity();
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
