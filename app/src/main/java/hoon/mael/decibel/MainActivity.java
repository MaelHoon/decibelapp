package hoon.mael.decibel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import hoon.mael.decibel.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private ActivityMainBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
        } else {
            onBackStackChanged();
        }

        swipeRefreshLayout = findViewById(R.id.layout_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 현재 화면에 표시된 Fragment를 가져옵니다.
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

            // TerminalFragment가 현재 화면에 표시되어 있지 않으면 refresh 동작을 수행합니다.
            if (!(currentFragment instanceof TerminalFragment)) {
                DevicesFragment devicesFragment = (DevicesFragment) getSupportFragmentManager().findFragmentByTag("devices");
                if (devicesFragment != null) {
                    devicesFragment.refresh();
                }
            } else {
                swipeRefreshLayout.setEnabled(false);
            }

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onBackStackChanged() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);

        if (!(currentFragment instanceof TerminalFragment)) {
            swipeRefreshLayout.setEnabled(true);
        }else{
            swipeRefreshLayout.setEnabled(false);
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
