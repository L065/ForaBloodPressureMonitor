package org.example;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        monitorWifiConnection();
    }

    private void monitorWifiConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();

        connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                uploadStoredTemperatures();
            }
        });
    }

    private void uploadStoredTemperatures() {
        Cursor cursor = dbHelper.getTemperatures();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            double value = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VALUE));
            // 上傳數據到遠端資料庫
            boolean uploadSuccess = uploadToRemoteDatabase(value);
            if (uploadSuccess) {
                dbHelper.deleteTemperature(id);
            }
        }
    }

    private boolean uploadToRemoteDatabase(double temperature) {
        // 上傳到遠端資料庫的實現
        return true;
    }
}
