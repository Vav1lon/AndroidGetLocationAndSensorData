package com.example.ExpLoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.ExpLoc.service.LocationService;

public class ExplorerLocationActivity extends Activity {

    private Thread task;
    private Intent intent;

    private Button bStartButton;
    private Button bStopService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initLayoutObject();

        bStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                task = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startService(intent);
                    }
                });
                //task.run();
                startService(intent);
                bStartButton.setVisibility(View.INVISIBLE);
                bStopService.setVisibility(View.VISIBLE);
            }
        });

        bStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task.isAlive()) {
                    task.stop();
                }
                bStartButton.setVisibility(View.VISIBLE);
                bStopService.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void initLayoutObject() {
        bStartButton = (Button) findViewById(R.id.btnStartService);

        bStopService = (Button) findViewById(R.id.btnStopService);
        bStopService.setVisibility(View.INVISIBLE);

        intent = new Intent(this, LocationService.class);
    }
}
