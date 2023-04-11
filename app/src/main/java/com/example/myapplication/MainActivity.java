package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

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
            layoutParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin); // добавьте отступы между кнопками
            button.setLayoutParams(layoutParams);
            keypadGridLayout.addView(button);
        }

        Button zeroButton = createButton("0", buttonSize);
        GridLayout.Spec rowSpec = GridLayout.spec(3);
        GridLayout.Spec colSpec = GridLayout.spec(1, 1f);
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);
        layoutParams.setGravity(Gravity.FILL_HORIZONTAL);
        layoutParams.width = 0;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.setMargins(buttonMargin, buttonMargin, buttonMargin, buttonMargin); // добавьте отступы между кнопками
        zeroButton.setLayoutParams(layoutParams);
        keypadGridLayout.addView(zeroButton);
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
