<<<<<<< HEAD
package com.example.androidremotecontroller;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
=======
// Java (MainActivity.java)
package com.example.androidremotecontroller; // <--- IMPORTANT: Make sure this package name matches your project's package!

import android.os.Bundle;
>>>>>>> 824acb41fc50b4b7452a5868a2bcd3efab2003f4
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText ipAddressEditText;
    private EditText portNumberEditText;
    private TextView statusTextView;
    private Button connectButton;
    private Button leftClickButton;
    private Button rightClickButton;
    private View trackpadView;

    // Existing Keyboard Button Declarations
    private Button enterButton;
    private Button backspaceButton;
    private Button spaceButton;
    private Button upArrowButton;
    private Button leftArrowButton;
    private Button downArrowButton;
    private Button rightArrowButton;

    // Text Input and Send Button Declarations
    private EditText textInputEditText;
    private Button sendTextButton;

    // --- NEW: Additional Keyboard Function Button Declarations ---
    private Button tabButton;
    private Button escapeButton;
    private Button deleteButton;
    private Button homeButton;
    private Button endButton;
    private Button pageUpButton;
    private Button pageDownButton;
    private Button controlButton;
    private Button altButton;
    private Button windowsButton;
    private Button volumeUpButton;
    private Button volumeDownButton;
    private Button muteButton;
    private Button f1Button;
    private Button f2Button;
    private Button f3Button;
    private Button f4Button;
    // --- END NEW ---

    // Network variables
    private String serverIpAddress;
    private int serverPort;
    private DatagramSocket clientSocket;
    private ExecutorService networkExecutor;

    // Trackpad variables
    private float lastX, lastY;
    private boolean isTracking = false;

    private static final String TAG = "AndroidRemote";
=======
public class MainActivity extends AppCompatActivity {

    // Declare your UI elements
    private EditText ipAddressEditText;
    private TextView statusTextView;
>>>>>>> 824acb41fc50b4b7452a5868a2bcd3efab2003f4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_main);

        // Initialize UI elements (existing)
        ipAddressEditText = findViewById(R.id.ipAddressEditText);
        portNumberEditText = findViewById(R.id.portNumberEditText);
        statusTextView = findViewById(R.id.statusTextView);
        connectButton = findViewById(R.id.connectButton);
        leftClickButton = findViewById(R.id.leftClickButton);
        rightClickButton = findViewById(R.id.rightClickButton);
        trackpadView = findViewById(R.id.trackpadView);

        // Existing Keyboard Button Initializations
        enterButton = findViewById(R.id.enterButton);
        backspaceButton = findViewById(R.id.backspaceButton);
        spaceButton = findViewById(R.id.spaceButton);
        upArrowButton = findViewById(R.id.upArrowButton);
        leftArrowButton = findViewById(R.id.leftArrowButton);
        downArrowButton = findViewById(R.id.downArrowButton);
        rightArrowButton = findViewById(R.id.rightArrowButton);

        // Text Input and Send Button Initializations
        textInputEditText = findViewById(R.id.textInputEditText);
        sendTextButton = findViewById(R.id.sendTextButton);

        // --- NEW: Additional Keyboard Function Button Initializations ---
        tabButton = findViewById(R.id.tabButton);
        escapeButton = findViewById(R.id.escapeButton);
        deleteButton = findViewById(R.id.deleteButton);
        homeButton = findViewById(R.id.homeButton);
        endButton = findViewById(R.id.endButton);
        pageUpButton = findViewById(R.id.pageUpButton);
        pageDownButton = findViewById(R.id.pageDownButton);
        controlButton = findViewById(R.id.controlButton);
        altButton = findViewById(R.id.altButton);
        windowsButton = findViewById(R.id.windowsButton);
        volumeUpButton = findViewById(R.id.volumeUpButton);
        volumeDownButton = findViewById(R.id.volumeDownButton);
        muteButton = findViewById(R.id.muteButton);
        f1Button = findViewById(R.id.f1Button);
        f2Button = findViewById(R.id.f2Button);
        f3Button = findViewById(R.id.f3Button);
        f4Button = findViewById(R.id.f4Button);
        // --- END NEW ---

        networkExecutor = Executors.newSingleThreadExecutor();

        // --- Connect Button Listener (existing) ---
        if (connectButton != null) {
            connectButton.setOnClickListener(v -> {
                String ip = ipAddressEditText.getText().toString();
                String portStr = portNumberEditText.getText().toString();

                if (ip.isEmpty()) {
                    statusTextView.setText("Please enter an IP Address.");
                    return;
                }
                if (portStr.isEmpty()) {
                    statusTextView.setText("Please enter a Port Number.");
                    return;
                }

                try {
                    serverIpAddress = ip;
                    serverPort = Integer.parseInt(portStr);
                    statusTextView.setText("Configuration: " + serverIpAddress + ":" + serverPort);
                    Log.d(TAG, "Server config set: " + serverIpAddress + ":" + serverPort);

                    networkExecutor.execute(() -> {
                        try {
                            if (clientSocket == null || clientSocket.isClosed()) {
                                clientSocket = new DatagramSocket();
                                runOnUiThread(() -> statusTextView.setText("Connected. Ready to send commands."));
                                Log.d(TAG, "UDP Socket created successfully.");
                            }
                        } catch (java.net.SocketException e) {
                            Log.e(TAG, "Error creating UDP socket: " + e.getMessage());
                            runOnUiThread(() -> statusTextView.setText("Socket creation error: " + e.getMessage()));
                        }
                    });

                } catch (NumberFormatException e) {
                    statusTextView.setText("Invalid Port Number. Please enter a valid number.");
                    Log.e(TAG, "Invalid port number format: " + portStr);
                }
            });
        }

        // --- Mouse Click Button Listeners (existing) ---
        if (leftClickButton != null) {
            leftClickButton.setOnClickListener(v -> sendCommand("LEFT_CLICK"));
        }
        if (rightClickButton != null) {
            rightClickButton.setOnClickListener(v -> sendCommand("RIGHT_CLICK"));
        }

        // --- Trackpad View OnTouchListener (existing) ---
        if (trackpadView != null) {
            trackpadView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getX();
                        lastY = event.getY();
                        isTracking = true;
                        Log.d(TAG, "Trackpad - ACTION_DOWN: " + lastX + ", " + lastY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isTracking) {
                            float currentX = event.getX();
                            float currentY = event.getY();
                            int deltaX = (int) (currentX - lastX);
                            int deltaY = (int) (currentY - lastY);
                            if (Math.abs(deltaX) > 0 || Math.abs(deltaY) > 0) {
                                String moveCommand = "MOVE:" + deltaX + ":" + deltaY;
                                sendCommand(moveCommand);
                                lastX = currentX;
                                lastY = currentY;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isTracking = false;
                        Log.d(TAG, "Trackpad - ACTION_UP/CANCEL");
                        break;
                }
                return true;
            });
        }

        // Existing Keyboard Button Listeners
        if (enterButton != null) {
            enterButton.setOnClickListener(v -> sendCommand("KEY:ENTER"));
        }
        if (backspaceButton != null) {
            backspaceButton.setOnClickListener(v -> sendCommand("KEY:BACKSPACE"));
        }
        if (spaceButton != null) {
            spaceButton.setOnClickListener(v -> sendCommand("KEY:SPACE"));
        }
        if (upArrowButton != null) {
            upArrowButton.setOnClickListener(v -> sendCommand("KEY:ARROW_UP"));
        }
        if (leftArrowButton != null) {
            leftArrowButton.setOnClickListener(v -> sendCommand("KEY:ARROW_LEFT"));
        }
        if (downArrowButton != null) {
            downArrowButton.setOnClickListener(v -> sendCommand("KEY:ARROW_DOWN"));
        }
        if (rightArrowButton != null) {
            rightArrowButton.setOnClickListener(v -> sendCommand("KEY:ARROW_RIGHT"));
        }

        // Text Input Send Button Listener
        if (sendTextButton != null) {
            sendTextButton.setOnClickListener(v -> {
                String textToSend = textInputEditText.getText().toString();
                if (!textToSend.isEmpty()) {
                    sendCommand("TEXT:" + textToSend);
                    textInputEditText.setText(""); // Clear the input field after sending
                } else {
                    statusTextView.setText("Please enter text to send.");
                }
            });
        }

        // --- NEW: Additional Keyboard Function Button Listeners ---
        if (tabButton != null) {
            tabButton.setOnClickListener(v -> sendCommand("KEY:TAB"));
        }
        if (escapeButton != null) {
            escapeButton.setOnClickListener(v -> sendCommand("KEY:ESCAPE"));
        }
        if (deleteButton != null) {
            deleteButton.setOnClickListener(v -> sendCommand("KEY:DELETE"));
        }
        if (homeButton != null) {
            homeButton.setOnClickListener(v -> sendCommand("KEY:HOME"));
        }
        if (endButton != null) {
            endButton.setOnClickListener(v -> sendCommand("KEY:END"));
        }
        if (pageUpButton != null) {
            pageUpButton.setOnClickListener(v -> sendCommand("KEY:PAGE_UP"));
        }
        if (pageDownButton != null) {
            pageDownButton.setOnClickListener(v -> sendCommand("KEY:PAGE_DOWN"));
        }
        if (controlButton != null) {
            controlButton.setOnClickListener(v -> sendCommand("KEY:CTRL"));
        }
        if (altButton != null) {
            altButton.setOnClickListener(v -> sendCommand("KEY:ALT"));
        }
        if (windowsButton != null) {
            windowsButton.setOnClickListener(v -> sendCommand("KEY:WINDOWS"));
        }
        if (volumeUpButton != null) {
            volumeUpButton.setOnClickListener(v -> sendCommand("KEY:VOLUME_UP"));
        }
        if (volumeDownButton != null) {
            volumeDownButton.setOnClickListener(v -> sendCommand("KEY:VOLUME_DOWN"));
        }
        if (muteButton != null) {
            muteButton.setOnClickListener(v -> sendCommand("KEY:MUTE"));
        }
        if (f1Button != null) {
            f1Button.setOnClickListener(v -> sendCommand("KEY:F1"));
        }
        if (f2Button != null) {
            f2Button.setOnClickListener(v -> sendCommand("KEY:F2"));
        }
        if (f3Button != null) {
            f3Button.setOnClickListener(v -> sendCommand("KEY:F3"));
        }
        if (f4Button != null) {
            f4Button.setOnClickListener(v -> sendCommand("KEY:F4"));
        }
        // --- END NEW ---

    } // End of onCreate

    // sendCommand method (existing)
    private void sendCommand(final String command) {
        if (serverIpAddress == null || clientSocket == null || clientSocket.isClosed()) {
            runOnUiThread(() -> statusTextView.setText("Not connected. Please set IP/Port and Connect."));
            Log.w(TAG, "Attempted to send command without valid connection details.");
            return;
        }

        networkExecutor.execute(() -> {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                byte[] buffer = command.getBytes();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddr, serverPort);
                clientSocket.send(packet);
                Log.d(TAG, "Sent command: " + command);

            } catch (Exception e) {
                Log.e(TAG, "Error sending UDP packet: " + e.getMessage(), e);
                runOnUiThread(() -> statusTextView.setText("Send Error: " + e.getMessage()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkExecutor != null && !networkExecutor.isShutdown()) {
            networkExecutor.shutdownNow();
        }
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
    }
}
=======
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
>>>>>>> 824acb41fc50b4b7452a5868a2bcd3efab2003f4
