package com.credix.pinpaddriverwithandroidusage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nexgolibrary.data.nexgo.device_config.NexgoDeviceConfig;
import com.example.nexgolibrary.data.nexgo.device_transaction.NexgoDeviceTransaction;

public class SplashActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword, inputTerminal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", null);
        String savedEncPassword = sharedPreferences.getString("password", null);
        String savedTerminal = sharedPreferences.getString("terminal", null);
        String decPassword = "";
        if (savedUsername != null && savedEncPassword != null && savedTerminal != null) {
            try {
                decPassword = EncryptionUtil.decrypt(savedEncPassword, "yedaPassN");
                Log.i("Decrypted", decPassword);
            } catch (Exception e){
                e.printStackTrace();
            }
            if(decPassword != ""){
                initNexgoPayment(savedUsername, decPassword, savedTerminal);
                }
        }

        setContentView(R.layout.activity_splash);

        // קישור ל-EditText מה-XML
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        inputTerminal = findViewById(R.id.inputTerminal);
    }

    private NexgoDeviceTransaction nexgoDeviceTransaction;
    private NexgoJavaWrapper nexgoJavaWrapper;

    public void sendToInit(View view) {
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String terminal = inputTerminal.getText().toString().trim();

        initNexgoPayment(username, password, terminal);
    }

    private void initNexgoPayment(String username, String password, String terminalNum) {
        Activity activity = SplashActivity.this;

        NexgoDeviceConfig nexgoDeviceConfig = new NexgoDeviceConfig(getApplicationContext());
        nexgoDeviceTransaction = new NexgoDeviceTransaction(activity);
        nexgoJavaWrapper = new NexgoJavaWrapper(nexgoDeviceConfig, nexgoDeviceTransaction);

        nexgoJavaWrapper.initNexgo(username, password, terminalNum).thenAccept(result -> {
            Log.i("MainActivity- Transaction", "תוצאה מהאתחול: " + result);
            if (result.equals("INIT_SUCCESSFUL")) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                try {
                    String ep = EncryptionUtil.encrypt(password, "yedaPassN");
                    editor.putString("password", ep);
                } catch (Exception e){
                    e.printStackTrace();
                }
                editor.putString("terminal", terminalNum); // ⚠️ לא מומלץ לשמור סיסמה כך - עדיף להצפין
                editor.apply(); // שמירת הנתונים

                // מעבר למסך הבא
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // סוגר את מסך ה-Splash כדי למנוע חזרה אליו

            } else {
                showCustomToast("שגיאה באתחול נתוני האשראי, יש למלא את כל השדות כראוי!");
            }
        }).exceptionally(e -> {
            Log.i("MainActivity- Transaction", "שגיאה בעת התחברות ל- Nexgo באתחול", e);
            showCustomToast("יש למלא את כל השדות כראוי!");
            return null;
        });
    }

    private void showCustomToast(String message) {
        runOnUiThread(() -> {
            Toast toast = Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG);

            View view = toast.getView();
            view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark)); // צבע רקע אדום

            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(getResources().getColor(android.R.color.white)); // צבע טקסט לבן
            text.setTextSize(18); // טקסט גדול יותר
            toast.show();
        });
    }
}
