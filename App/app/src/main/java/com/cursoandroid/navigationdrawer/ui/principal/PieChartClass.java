package com.cursoandroid.navigationdrawer.ui.principal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.cursoandroid.navigationdrawer.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PieChartClass extends AppCompatActivity {

    float linkDownstreamBandwidthMbps, linkUpstreamBandwidthMbps;

    // Dados para verificar MB enviados e recebidos
    private TextView txDataUsage;
    private volatile boolean restartRequested = false;

    ConnectivityManager connManager;
    WifiManager wifiManager;

    private static final int NOTIFICATION_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart);

        /* -------------------------------------------------------------------------------------- */

        // Criação do canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Channel Name";
            String channelDescription = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", channelName, importance);
            notificationChannel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        /* ----------------------------DADOS ENVIADOS E RECEBIDOS---------------------------------*/

        // Inicializa os elementos da interface
        txDataUsage = findViewById(R.id.txDataUsage);

        // Agrupa as inicializações dos botões
        Button btnRestart = findViewById(R.id.btnRestart);

        // Configura o ouvinte para o botão de reinício usando expressão lambda
        btnRestart.setOnClickListener(v -> restartRequested = true);

        startDataUsageMonitoring();

        // -------------------------------------------------------------------------------------- //

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Configura o ouvinte de clique para o botão usando expressão lambda
        startDataUsageMonitoring();
        atualizarInformacoes();
        /* ---------------------------------------------------------------------------------------*/

        View botaoAtualizarPie = findViewById(R.id.buttonAtualizarPie);
        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> visitors = new ArrayList<>();
        visitors.add(new PieEntry(linkDownstreamBandwidthMbps, "Descida"));
        visitors.add(new PieEntry(linkUpstreamBandwidthMbps, "Subida"));
        PieDataSet pieDataSet = new PieDataSet(visitors, "Banda Larga");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        //pieChart.setCenterText("Banda Larga");
        pieChart.animate();

        botaoAtualizarPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PieChart pieChart = findViewById(R.id.pieChart);

                ArrayList<PieEntry> visitors = new ArrayList<>();
                visitors.add(new PieEntry(linkDownstreamBandwidthMbps, "Descida"));
                visitors.add(new PieEntry(linkUpstreamBandwidthMbps, "Subida"));

                PieDataSet pieDataSet = new PieDataSet(visitors, "Banda Larga");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                //pieChart.setCenterText("Banda Larga");
                pieChart.animate();
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/
    // CALCULO DO PACOTE DE SUBIDA, DESCIDA E FORÇA DE SINAL

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void atualizarInformacoes() {
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            NetworkCapabilities networkCapabilities = connManager.getNetworkCapabilities(connManager.getActiveNetwork());

            int linkDownstreamBandwidthKbps = networkCapabilities.getLinkDownstreamBandwidthKbps();
            int linkUpstreamBandwidthKbps = networkCapabilities.getLinkUpstreamBandwidthKbps();

            float linkDownstreamBandwidthMbps = linkDownstreamBandwidthKbps / 1000f;
            float linkUpstreamBandwidthMbps = linkUpstreamBandwidthKbps / 1000f;

            this.linkUpstreamBandwidthMbps = linkUpstreamBandwidthMbps;
            this.linkDownstreamBandwidthMbps = linkDownstreamBandwidthMbps;
        }
    }

    /*--------------------------------------------------------------------------------------------*/
    // CÁLCULO DO MB ENVIADOS E RECEBIDOS
    private boolean notificationSent = false;
    private void startDataUsageMonitoring() {
        EditText inputMBUtilizados = findViewById(R.id.input_mb_utilizados);

        // Obtém o valor armazenado em SharedPreferences, se existir
        SharedPreferences sharedPreferences = getSharedPreferences("data_usage", MODE_PRIVATE);
        final double[] MBUtilizados = {sharedPreferences.getFloat("mb_utilizados", 0.0f)};

        // Define o valor inicial do EditText
        inputMBUtilizados.setText(String.valueOf(MBUtilizados[0]));

        inputMBUtilizados.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Salva o valor atualizado de MBUtilizados em SharedPreferences
                    MBUtilizados[0] = Double.parseDouble(inputMBUtilizados.getText().toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("mb_utilizados", (float) MBUtilizados[0]);
                    editor.apply();

                    // Oculta o teclado e remove o foco do EditText
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputMBUtilizados.getWindowToken(), 0);
                    inputMBUtilizados.clearFocus();

                    return true;
                }
                return false;
            }
        });

        long initialRxBytes = sharedPreferences.getLong("initial_rx_bytes", 0);
        long initialTxBytes = sharedPreferences.getLong("initial_tx_bytes", 0);
        long initialTime = sharedPreferences.getLong("initial_time", System.currentTimeMillis());

        // Restaurar os valores iniciais salvos
        if (initialRxBytes == 0 || initialTxBytes == 0) {
            initialRxBytes = TrafficStats.getTotalRxBytes();
            initialTxBytes = TrafficStats.getTotalTxBytes();
            initialTime = System.currentTimeMillis();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("initial_rx_bytes", initialRxBytes);
            editor.putLong("initial_tx_bytes", initialTxBytes);
            editor.putLong("initial_time", initialTime);
            editor.apply();
        }

        long finalInitialRxBytes = initialRxBytes;
        long finalInitialTxBytes = initialTxBytes;
        long finalInitialTime = initialTime;

        double finalMBUtilizados = MBUtilizados[0];
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !restartRequested) {
                    long currentRxBytes = TrafficStats.getTotalRxBytes();
                    long currentTxBytes = TrafficStats.getTotalTxBytes();

                    final long rxBytes = currentRxBytes - finalInitialRxBytes;
                    final long txBytes = currentTxBytes - finalInitialTxBytes;

                    final double rxMB = rxBytes / (1024.0 * 1024.0);
                    final double txMB = txBytes / (1024.0 * 1024.0);

                    final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String dataUsage = "↓ Dados Recebidos: " + decimalFormat.format(rxMB) + " MB\n" +
                                    "↑ Dados Enviados: " + decimalFormat.format(txMB) + " MB";
                            txDataUsage.setText(dataUsage);
                        }
                    });

                    double totalDataUsageMB = rxMB + txMB;
                    double dataUsagePercentage = (totalDataUsageMB / finalMBUtilizados) * 100.0;

                    // Verifica se a pessoa excedeu o limite
                    if (totalDataUsageMB > finalMBUtilizados && !notificationSent) {
                        // Faça o que for necessário quando o limite for excedido
                        // Por exemplo, exibir uma mensagem de aviso ou interromper o acesso à internet
                        showNotification("Limite de dados excedido", "Os dados foram completamente utilizados.");
                        notificationSent = true; // Define a variável como true para indicar que a notificação foi enviada
                    } else if (dataUsagePercentage >= 80.0 && !notificationSent) {
                        showNotification("Uso de dados próximo do limite", "O uso de dados atingiu 80% do limite.");
                        notificationSent = true; // Define a variável como true para indicar que a notificação foi enviada
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Reinicia a cada 30 dias (em milissegundos: 30 dias * 24 horas * 60 minutos * 60 segundos * 1000)
                    if (System.currentTimeMillis() - finalInitialTime >= 30L * 24L * 60L * 60L * 1000L) {
                        restartRequested = true;
                    }
                }
                if (restartRequested) {
                    // Limpa os valores salvos
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("initial_rx_bytes");
                    editor.remove("initial_tx_bytes");
                    editor.remove("initial_time");
                    editor.apply();
                    // Reinicia a contagem
                    restartRequested = true;
                }
            }
        }).start();
    }

    private void showNotification(String title, String message) {
        Intent notificationIntent = new Intent(this, PieChartClass.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT); // Adicione a flag FLAG_ONE_SHOT

        // Obtém o ícone do aplicativo
        int appIconResId = R.drawable.icon_app; // Substitua pelo seu próprio ícone do aplicativo

        // Cria a notificação usando NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(appIconResId)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
