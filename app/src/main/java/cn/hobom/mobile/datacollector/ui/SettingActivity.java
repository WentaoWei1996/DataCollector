package cn.hobom.mobile.datacollector.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.hobom.mobile.datacollector.R;
import cn.hobom.mobile.datacollector.util.HeaderUtil;
import cn.hobom.mobile.datacollector.util.PreferencesUtil;

import static cn.hobom.mobile.datacollector.trace.GetLocalTrace.GetPID;
import static cn.hobom.mobile.datacollector.trace.GetLocalTrace.GetPPID;
import static cn.hobom.mobile.datacollector.trace.GetLocalTrace.KillPID;

/**
 * Created by o on 2018-03-18.
 */

public class SettingActivity extends Activity {
    private CheckBox checkBox;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        setupView();

    }
    private void setupView(){
        HeaderUtil.show(this, true, false, true, false);
        HeaderUtil.initCenterTitle(this, "设置");
        checkBox = (CheckBox)findViewById(R.id.check);
        checkBox.setChecked(PreferencesUtil.getInstance().getSettingBoolean("enable",true));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferencesUtil.getInstance().setSettingBoolean("enable",b);
            }
        });

        button = (Button) findViewById(R.id.clear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PPID = GetPPID();
                String thisPID = GetPID("cn.hobom.mobile.datacollector");

                KillPID(PPID,thisPID);
            }
        });
    }
}
