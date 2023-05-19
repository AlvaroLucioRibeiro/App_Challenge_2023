package com.cursoandroid.navigationdrawer.ui.principal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.cursoandroid.navigationdrawer.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Random;

public class BarChartClass extends AppCompatActivity {

    private Button botaoAtualizarBar;
    private static final int PERMISSION_REQUEST_CODE = 1;
    double callUsageMB, otherUsageMB, streamingUsageMB, downloadUsageMB; // Variável de instância

    // SUPOSIÇÃO DO USO DE DADOS
    @SuppressLint("DefaultLocale")
    private void getUsageData() {
        // Obtém o número total de bytes recebidos e enviados
        long totalBytes = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();

        // Constantes para as porcentagens
        final double CALL_USAGE_PERCENTAGE = 0.10;
        final double DOWNLOAD_USAGE_PERCENTAGE = 0.45;
        final double STREAMING_USAGE_PERCENTAGE = 0.25;

        // Cálculos de uso em bytes
        long callUsage = (long) (totalBytes * CALL_USAGE_PERCENTAGE);
        long downloadUsage = (long) (totalBytes * DOWNLOAD_USAGE_PERCENTAGE);
        long streamingUsage = (long) (totalBytes * STREAMING_USAGE_PERCENTAGE);

        // Cálculo do uso em outros (subtraindo o uso de voz, downloads e streaming)
        long otherUsage = totalBytes - (callUsage + downloadUsage + streamingUsage);

        // Conversão para megabytes (MB)
        double callUsageMB = callUsage / (1024.0 * 1024);
        double downloadUsageMB = downloadUsage / (1024.0 * 1024);
        double streamingUsageMB = streamingUsage / (1024.0 * 1024);
        double otherUsageMB = otherUsage / (1024.0 * 1024);

        this.callUsageMB = callUsageMB;
        this.downloadUsageMB = downloadUsageMB;
        this.streamingUsageMB = streamingUsageMB;
        this.otherUsageMB = otherUsageMB;
    }

    /*--------------------------------------------------------------------------------------------*/
    // Permissão do Usuário
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida, então obtemos as informações de uso
                getUsageData();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchart);

        // Inicialize o TelephonyManager - Dados Streaming, call voice...
        // Dados Streaming, call voice...
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        botaoAtualizarBar = findViewById(R.id.buttonAtualizarBar);

        // Verifica se a permissão READ_PHONE_STATE foi concedida
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão se não foi concedida
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        } else {
            // A permissão foi concedida, então obtemos as informações de uso
            getUsageData();
        }

        int numero = new Random().nextInt(1001);

        botaoAtualizarBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numero = new Random().nextInt(1001);

                BarChart barChart = findViewById(R.id.barChart);

                ArrayList<BarEntry> visitors = new ArrayList<>();
                visitors.add(new BarEntry(2014, (float) callUsageMB));
                visitors.add(new BarEntry(2015, (float) downloadUsageMB));
                visitors.add(new BarEntry(2016, (float) streamingUsageMB));
                visitors.add(new BarEntry(2017, (float) otherUsageMB));
                visitors.add(new BarEntry(2018, numero));

                BarDataSet barDataSet = new BarDataSet(visitors, "Uso de Dados");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);

                barChart.setFitBars(true);
                barChart.setData(barData);
                //barChart.getDescription().setText("Uso de Dados");
                barChart.animateY(2000);
            }
        });

        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014, (float) callUsageMB));
        visitors.add(new BarEntry(2015, (float) downloadUsageMB));
        visitors.add(new BarEntry(2016, (float) streamingUsageMB));
        visitors.add(new BarEntry(2017, (float) otherUsageMB));
        visitors.add(new BarEntry(2018, numero));

        BarDataSet barDataSet = new BarDataSet(visitors, "Uso de Dados");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Uso de Dados");
        barChart.animateY(2000);
    }
}
