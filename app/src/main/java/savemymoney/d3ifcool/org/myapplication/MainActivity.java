package savemymoney.d3ifcool.org.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    EditText inputText, inputPassword;
    TextView outputEncrypt, outputDecrypt;
    Button encryptButton, decryptButton;
    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (EditText) findViewById(R.id.editText_textToEncrypt);
        inputPassword = (EditText) findViewById(R.id.editText_ecryptPassword);

        outputEncrypt = (TextView) findViewById(R.id.encrypt_result);
        outputDecrypt = (TextView) findViewById(R.id.decrypt_result);

        encryptButton = (Button) findViewById(R.id.encrypt_button);
        decryptButton = (Button) findViewById(R.id.decrypt_button);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = encrypt(inputText.getText().toString(), inputPassword.getText().toString());
                    outputEncrypt.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputString = decrypt(outputEncrypt.getText().toString(), inputPassword.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                outputDecrypt.setText(outputString);
            }
        });
    }

    private String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decryptValue = c.doFinal(decodeValue);
        String decryptedValue = new String(decryptValue);
        return decryptedValue;
    }

    private String encrypt(String data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptVal = c.doFinal(data.getBytes());
        String encryptedValue = Base64.encodeToString(encryptVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}