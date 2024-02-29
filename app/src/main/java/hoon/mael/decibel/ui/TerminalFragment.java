package hoon.mael.decibel.ui;

import static hoon.mael.decibel.Utils.MessageUtils.disableNavigationBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

import hoon.mael.decibel.R;
import hoon.mael.decibel.Serial.SerialListener;
import hoon.mael.decibel.Serial.SerialService;
import hoon.mael.decibel.Serial.SerialSocket;
import hoon.mael.decibel.Utils.BluetoothStateUtil;
import hoon.mael.decibel.Utils.PrefUtils;
import hoon.mael.decibel.model.DecibelModel;


public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener {
    private long delay = 0;

    private enum Connected {False, Pending, True}

    private String deviceAddress;
    private SerialService service;

    private TextView receiveText, tvStdMaxDecibel, tvStdThrDecibel, tvCurrentDecibel;

    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean hexEnabled = false;

    private View terminalView, decibelPage01, decibelPage02, decibelPage03, layoutButton;
    private Button btnPrev, btnNext;
    private View frameDecibel;

    private PrefUtils prefUtils;
    private GestureDetector gestureDetector;

    private String instantDecibel, averageDecibel;
    private LottieAnimationView progressBar;
    private int hours, minutes, remainingSeconds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        deviceAddress = getArguments().getString("device");

        prefUtils = new PrefUtils(requireContext());
        Activity activity = getActivity();
        if (activity != null) {

            disableNavigationBar(activity);
        }
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(new Intent(getActivity(), SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (service != null)
            service.attach(this);
        else
            getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onStop() {
        if (service != null && !getActivity().isChangingConfigurations())
            //service.detach();
            super.onStop();
    }

    @SuppressWarnings("deprecation")
    // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        try {
            getActivity().unbindService(this);
        } catch (Exception ignored) {
        }
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (initialStart && service != null) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    /*
     * UI
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        terminalView = inflater.inflate(R.layout.fragment_terminal, container, false);

        initValue();
        initComponent();
        initComponentValue();
        initListener();

        return terminalView;
    }

    private void initValue() {
        gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                return true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {

        btnPrev.setOnClickListener(view -> {
            delay = System.currentTimeMillis() + 200;

            if (System.currentTimeMillis() > delay && frameDecibel.getVisibility() == View.VISIBLE) {
                updateDecibelPagePrev();
            }
            if (System.currentTimeMillis() <= delay) {
                if (frameDecibel.getVisibility() == View.GONE) {
                    frameDecibel.setVisibility(View.VISIBLE);
                } else {
                    frameDecibel.setVisibility(View.GONE);
                }
            }
        });

        btnNext.setOnClickListener(view -> {
            delay = System.currentTimeMillis() + 200;

            if (System.currentTimeMillis() > delay && frameDecibel.getVisibility() == View.VISIBLE) {
                updateDecibelPageNext();
            }
            if (System.currentTimeMillis() <= delay) {
                if (frameDecibel.getVisibility() == View.GONE) {
                    frameDecibel.setVisibility(View.VISIBLE);
                } else {
                    frameDecibel.setVisibility(View.GONE);
                }
            }
        });
    }

    private void saveInputValue() {


    }

    private void initComponent() {
        progressBar = terminalView.findViewById(R.id.progressBar);
        decibelPage01 = terminalView.findViewById(R.id.layout_page_decibel_01);
        decibelPage02 = terminalView.findViewById(R.id.layout_page_decibel_02);
        decibelPage03 = terminalView.findViewById(R.id.layout_page_decibel_03);
        layoutButton = terminalView.findViewById(R.id.layout_Button);
        frameDecibel = terminalView.findViewById(R.id.frame_decibel);
        btnPrev = layoutButton.findViewById(R.id.btn_prev);
        btnNext = layoutButton.findViewById(R.id.btn_next);

        receiveText = decibelPage01.findViewById(R.id.receive_text);
        tvStdMaxDecibel = decibelPage01.findViewById(R.id.tv_stdMaxDecibel);
        tvCurrentDecibel = decibelPage01.findViewById(R.id.tv_currentDecibel);
        tvStdThrDecibel = decibelPage01.findViewById(R.id.tv_stdThrDecibel);
    }

    private void initComponentValue() {

        tvStdMaxDecibel.setText(prefUtils.getString("standardInput3"));
        tvStdThrDecibel.setText(prefUtils.getString("standardInput2"));
    }

    private int getDecibelPageIndex() {
        boolean isDecibelPage01Visible = (decibelPage01.getVisibility() == View.VISIBLE);
        boolean isDecibelPage02Visible = (decibelPage02.getVisibility() == View.VISIBLE);
        boolean isDecibelPage03Visible = (decibelPage03.getVisibility() == View.VISIBLE);

        if (isDecibelPage01Visible && !isDecibelPage02Visible && !isDecibelPage03Visible) {
            return 1;
        } else if (!isDecibelPage01Visible && isDecibelPage02Visible && !isDecibelPage03Visible) {
            return 2;
        } else if (!isDecibelPage01Visible && !isDecibelPage02Visible && isDecibelPage03Visible) {
            return 3;
        } else {
            return 1;
        }
    }

    private void updateDecibelPageNext() {
        switch (getDecibelPageIndex()) {
            case 1:
                decibelPage01.setVisibility(View.GONE);
                decibelPage02.setVisibility(View.VISIBLE);
                decibelPage03.setVisibility(View.GONE);
                break;
            case 2:
                decibelPage01.setVisibility(View.GONE);
                decibelPage02.setVisibility(View.GONE);
                decibelPage03.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateDecibelPagePrev() {
        switch (getDecibelPageIndex()) {
            case 2:
                decibelPage01.setVisibility(View.VISIBLE);
                decibelPage02.setVisibility(View.GONE);
                decibelPage03.setVisibility(View.GONE);
                break;
            case 3:
                decibelPage01.setVisibility(View.GONE);
                decibelPage02.setVisibility(View.VISIBLE);
                decibelPage03.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_terminal, menu);
        menu.findItem(R.id.hex).setChecked(hexEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /*
     * Serial + UI
     */
    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("connecting...");

            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
        Toast.makeText(requireContext(), "장치 연결에 실패 하였습니다.\n 전원을 확인 바랍니다.", Toast.LENGTH_SHORT).show();
    }


    StringBuffer str = new StringBuffer("");

    private void receive(ArrayDeque<byte[]> datas) {
        try {
            for (byte[] data : datas) {
                String ByteToStr = new String(data);
                str = str.append(ByteToStr);

                if (str.toString().contains("\n\r")) {
                    String str1 = String.valueOf(str);
                    String[] words = str1.split(",");
                    //Log.d("마엘", words[0]);
                    str.setLength(0);

                    if (words[0].matches("\\d+")) {// 수신 시작 신호
                        int nthData = Integer.parseInt(words[0]);
                        hours = nthData / 3600;
                        minutes = (nthData % 3600) / 60;
                        remainingSeconds = nthData % 60;

                        BluetoothStateUtil.setIsReceiveStarted(true);

                        BluetoothStateUtil.setBLEStateRunning();

                        BluetoothStateUtil.setToogle(false);
                    } else {
                        if (words[0].contains("Q")) { //0번째 값이 숫자일 경우에만 참이되도록 정규식사용 수신 종료 프로세스
                            String timeString = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
                            String currentTime = getCurrentTime();

                            String endReceiveString = currentTime + " Runtime " + timeString;
                            Log.d("마엘", endReceiveString);

                            BluetoothStateUtil.setToogle(true);
                            BluetoothStateUtil.setIsReceiveStarted(false);

                            BluetoothStateUtil.setBleStateStop();

                            BluetoothStateUtil.setReceiveEndString(endReceiveString);
                        }
                    }

                    instantDecibel = words[1];
                    averageDecibel = words[2];

                    updateDecibelUiPage01(instantDecibel);
                    Log.d("마엘", "순간 데시벨 : " + words[1] + "평균 데시벨" + words[2]);
                    DecibelModel.setCurrentDecibel(instantDecibel);
                    DecibelModel.setAverageDecibel(averageDecibel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    void updateDecibelUiPage01(String currentDecibel) {
        tvCurrentDecibel.setText(currentDecibel);
    }

    void updateAverageDecibel(String value) {

    }

    private void status(String str) {
/*        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        receiveText.append(spn);*/
    }

    /*
     * SerialListener
     */
    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;

        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(requireContext(), InputDecibelActivity.class);
        intent.putExtra("asd", instantDecibel);
        intent.putExtra("asd2", averageDecibel);
        startActivity(intent);
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect();

        Intent intent = new Intent(requireContext(), DeviceSelectActivity.class);
        startActivity(intent);

        Toast.makeText(requireContext(), "장치 연결에 실패 하였습니다.\n 전원을 확인 바랍니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSerialRead(byte[] data) {
        ArrayDeque<byte[]> datas = new ArrayDeque<>();
        datas.add(data);
        receive(datas);
    }

    public void onSerialRead(ArrayDeque<byte[]> datas) {
        receive(datas);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }

}
