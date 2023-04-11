package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private EditText pinEditText;
    private GridLayout keypadGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinEditText = findViewById(R.id.pinEditText);
        keypadGridLayout = findViewById(R.id.keypadGridLayout);

        createKeypad();
    }

    private void createKeypad() {
        int buttonSize = getResources().getDimensionPixelSize(R.dimen.keypad_button_size);
        int buttonMargin = getResources().getDimensionPixelSize(R.dimen.keypad_button_margin);
        for (int i = 1; i <= 9; i++) {
            Button button = createButton(String.valueOf(i), buttonSize);
            int row = (i - 1) / 3;
            int col = (i - 1) % 3;
            GridLayout.Spec rowSpec = GridLayout.spec(row);
            GridLayout.Spec colSpec = GridLayout.spec(col, 1f);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);
            layoutParams.setGravity(Gravity.FILL_HORIZONTAL);
            layoutParams.width = 0;
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
            button.setLayoutParams(layoutParams);
            keypadGridLayout.addView(button);
        }

        // Инициализируем кнопку 0
        Button zeroButton = createButton("0", buttonSize);
        GridLayout.Spec rowSpec = GridLayout.spec(3);
        GridLayout.Spec colSpec = GridLayout.spec(1, 1f);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);
        layoutParams.setGravity(Gravity.FILL_HORIZONTAL);
        layoutParams.width = 0;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
        zeroButton.setLayoutParams(layoutParams);
        keypadGridLayout.addView(zeroButton);

        // Инициализируем кнопку отпечатка пальца
        ImageButton fingerprintButton = createFingerprintButton(buttonSize);
        GridLayout.Spec fpRowSpec = GridLayout.spec(3);
        GridLayout.Spec fpColSpec = GridLayout.spec(0, 1f);
        GridLayout.LayoutParams fpLayoutParams = new GridLayout.LayoutParams(fpRowSpec, fpColSpec);
        fpLayoutParams.setGravity(Gravity.FILL_HORIZONTAL);
        fpLayoutParams.width = 0;
        fpLayoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        fpLayoutParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
        fingerprintButton.setLayoutParams(fpLayoutParams);
        keypadGridLayout.addView(fingerprintButton);

        // Инициализируем кнопку бэкспейс
        ImageButton eraseButton = createEraseButton(buttonSize);
        GridLayout.Spec eraseRowSpec = GridLayout.spec(3);
        GridLayout.Spec eraseColSpec = GridLayout.spec(2, 1f);
        GridLayout.LayoutParams eraseLayoutParams = new GridLayout.LayoutParams(eraseRowSpec, eraseColSpec);
        eraseLayoutParams.setGravity(Gravity.FILL_HORIZONTAL);
        eraseLayoutParams.width = 0;
        eraseLayoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        eraseLayoutParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin);
        eraseButton.setLayoutParams(eraseLayoutParams);
        keypadGridLayout.addView(eraseButton);
    }

    private ImageButton createFingerprintButton(int buttonSize) {
        ImageButton button = new ImageButton(this);
        button.setImageResource(R.drawable.fingerprint_icon);
        button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        button.setAdjustViewBounds(true);
        button.setBackgroundResource(R.drawable.rounded_button);
        button.setMaxWidth(buttonSize);
        button.setMaxHeight(buttonSize);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateFingerprint();
            }
        });

        return button;
    }
    private ImageButton createEraseButton(int buttonSize) {
        ImageButton button = new ImageButton(this);
        button.setImageResource(R.drawable.erase_icon);
        button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        button.setAdjustViewBounds(true);
        button.setBackgroundResource(R.drawable.rounded_button);
        button.setMaxWidth(buttonSize);
        button.setMaxHeight(buttonSize);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Стираем написанное
                if (pinEditText.length() > 0) {
                    pinEditText.getText().delete(pinEditText.length() - 1, pinEditText.length());
                }
            }
        });

        return button;
    }
    private void authenticateFingerprint() {
        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Fingerprint Authentication")
                    .setDescription("Please place your finger on the sensor")
                    .setNegativeButtonText("Cancel")
                    .build();

            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor,
                    new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            // Обработка ошибок аутентификации
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            // Обработка успешной аутентификации
                            // Например, переход к следующему экрану или отображение сообщения
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            // Обработка неудачной аутентификации, например, неправильный отпечаток пальца
                        }
                    });

            biometricPrompt.authenticate(promptInfo);
        } else {
            // Биометрическая аутентификация недоступна, показать сообщение или предложить альтернативный метод аутентификации
        }
    }
    private Button createButton(String text, int buttonSize) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(24);
        button.setTextColor(getResources().getColor(android.R.color.black));
        button.setWidth(buttonSize);
        button.setHeight(buttonSize);
        button.setMinWidth(buttonSize);
        button.setMinHeight(buttonSize);
        button.setMinimumWidth(buttonSize);
        button.setMinimumHeight(buttonSize);

        button.setBackgroundResource(R.drawable.rounded_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                pinEditText.append(button.getText());
            }
        });

        return button;
    }
}
