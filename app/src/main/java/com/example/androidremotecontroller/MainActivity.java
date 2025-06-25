// Java (MainActivity.java)
package com.example.androidremotecontroller; // <--- IMPORTANT: Make sure this package name matches your project's package!

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare your UI elements
    private EditText ipAddressEditText;
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // --- FIX HERE: You MUST call setContentView() BEFORE findViewById() ---
        // You need to set the content view to your layout XML file.
        // Replace 'R.layout.activity_main' with the actual ID of your layout file.
        setContentView(R.layout.activity_main); // Assuming your layout file is named activity_main.xml

        // Initialize your UI elements after setContentView()
        ipAddressEditText = findViewById(R.id.ipAddressEditText); // Make sure this ID matches your XML
        Button connectButton = findViewById(R.id.connectButton);         // Make sure this ID matches your XML
        statusTextView = findViewById(R.id.statusTextView);       // Make sure this ID matches your XML
        View trackpadView = findViewById(R.id.trackpadView);           // Make sure this ID matches your XML
        Button leftClickButton = findViewById(R.id.leftClickButton);     // Make sure this ID matches your XML
        Button rightClickButton = findViewById(R.id.rightClickButton);   // Make sure this ID matches your XML


        // --- Now you can safely set your OnClickListeners, because the buttons are found ---

        if (connectButton != null) { // Good practice to add a null check during debugging
            connectButton.setOnClickListener(v -> {
                // Your connect logic here
                String ipAddress = ipAddressEditText.getText().toString();
                statusTextView.setText("Connecting to: " + ipAddress);
            });
        } else {
            // This line would print if findViewById(R.id.connectButton) returned null
            // Check your XML id and setContentView() if you see this in Logcat
            System.err.println("connectButton is null! Check XML ID and setContentView.");
        }


        if (leftClickButton != null) {
            leftClickButton.setOnClickListener(v -> {
                // Your left click logic
                statusTextView.setText("Left Clicked!");
            });
        }

        if (rightClickButton != null) {
            rightClickButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Your right click logic
                    statusTextView.setText("Right Clicked!");
                }
            });
        }

        if (trackpadView != null) {
            trackpadView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, android.view.MotionEvent event) {
                    // Your trackpad touch logic (e.g., mouse movement)
                    statusTextView.setText("Trackpad touched at: " + event.getX() + ", " + event.getY());
                    return true; // Consume the event
                }
            });
        }
    }
}
