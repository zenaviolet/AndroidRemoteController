#include <winsock2.h>
#include <windows.h> // For SendInput, SetCursorPos, Sleep, and VK_ codes
#include <ws2tcpip.h> // For inet_ntop
#include <stdio.h>    // For printf
#include <string.h>   // For memset, strcmp, strncmp
#include <stdlib.h>   // For atoi

#pragma comment(lib, "ws2_32.lib") // Link with Winsock library

// Function to send a keyboard key event (press or release)
void send_key(WORD vk_code, BOOL is_press) {
    INPUT ip;
    ip.type = INPUT_KEYBOARD;
    ip.ki.wScan = 0; // Hardware scan code for the key (0 for virtual key code)
    ip.ki.time = 0;
    ip.ki.dwExtraInfo = 0;

    ip.ki.wVk = vk_code; // Virtual-key code

    if (is_press) {
        ip.ki.dwFlags = 0; // 0 for key press
    } else {
        ip.ki.dwFlags = KEYEVENTF_KEYUP; // KEYEVENTF_KEYUP for key release
    }

    SendInput(1, &ip, sizeof(INPUT));
}

// Function to simulate a full key press (down and up) with a small delay
void simulate_key_press(WORD vk_code) {
    send_key(vk_code, TRUE); // Key down
    Sleep(50); // Small delay
    send_key(vk_code, FALSE); // Key up
}


// Function to send a left mouse click at absolute coordinates (Keep if you use this)
// Note: This function sets the cursor position then clicks.
// For relative movement, MOUSEEVENTF_MOVE is used directly on dx, dy.
void send_click_absolute(int x, int y) {
    SetCursorPos(x, y); // Move cursor to absolute position
    INPUT input = {0};
    input.type = INPUT_MOUSE;
    input.mi.dwFlags = MOUSEEVENTF_LEFTDOWN; // Left button down
    SendInput(1, &input, sizeof(input));
    Sleep(50); // Small delay for the click to register
    input.mi.dwFlags = MOUSEEVENTF_LEFTUP;  // Left button up
    SendInput(1, &input, sizeof(input));
}

// Function to simulate a relative mouse move
void send_mouse_move_relative(int dx, int dy) {
    INPUT input = {0};
    input.type = INPUT_MOUSE;
    input.mi.dx = dx;
    input.mi.dy = dy;
    input.mi.dwFlags = MOUSEEVENTF_MOVE; // Indicates relative movement
    SendInput(1, &input, sizeof(input));
}

// Function to simulate a left mouse click (without changing position)
void send_left_click() {
    INPUT input[2] = {0}; // Two events: mouse down, mouse up

    // Left Button Down
    input[0].type = INPUT_MOUSE;
    input[0].mi.dwFlags = MOUSEEVENTF_LEFTDOWN;

    // Left Button Up
    input[1].type = INPUT_MOUSE;
    input[1].mi.dwFlags = MOUSEEVENTF_LEFTUP;

    SendInput(2, input, sizeof(INPUT)); // Send both events
}

// Function to simulate a right mouse click (without changing position)
void send_right_click() {
    INPUT input[2] = {0};

    // Right Button Down
    input[0].type = INPUT_MOUSE;
    input[0].mi.dwFlags = MOUSEEVENTF_RIGHTDOWN;

    // Right Button Up
    input[1].type = INPUT_MOUSE;
    input[1].mi.dwFlags = MOUSEEVENTF_RIGHTUP;

    SendInput(2, input, sizeof(INPUT));
}

// Function to simulate typing a string character by character
void send_string(const char* str) {
    INPUT ip;
    ip.type = INPUT_KEYBOARD;
    ip.ki.wScan = 0; // Hardware scan code for the key (0 for virtual key code)
    ip.ki.time = 0;
    ip.ki.dwExtraInfo = 0;

    for (int i = 0; i < strlen(str); i++) {
        char c = str[i];
        WORD vk_code = 0;
        BOOL needs_shift = FALSE;

        if (c >= 'a' && c <= 'z') {
            vk_code = c - ('a' - 'A'); // Convert to uppercase VK code
        } else if (c >= 'A' && c <= 'Z') {
            vk_code = c;
            needs_shift = TRUE;
        } else if (c >= '0' && c <= '9') {
            vk_code = c; // VK_0 through VK_9 map to ASCII '0' through '9'
        } else if (c == ' ') {
            vk_code = VK_SPACE;
        }
        // --- Extensive Symbol Handling (Add more as needed for your keyboard layout) ---
        // This mapping assumes a standard US keyboard layout.
        // For other layouts, VK_ codes for symbols might differ,
        // or require VK_OEM_ codes.
        else if (c == '!') { vk_code = '1'; needs_shift = TRUE; }
        else if (c == '@') { vk_code = '2'; needs_shift = TRUE; }
        else if (c == '#') { vk_code = '3'; needs_shift = TRUE; }
        else if (c == '$') { vk_code = '4'; needs_shift = TRUE; }
        else if (c == '%') { vk_code = '5'; needs_shift = TRUE; }
        else if (c == '^') { vk_code = '6'; needs_shift = TRUE; }
        else if (c == '&') { vk_code = '7'; needs_shift = TRUE; }
        else if (c == '*') { vk_code = '8'; needs_shift = TRUE; }
        else if (c == '(') { vk_code = '9'; needs_shift = TRUE; }
        else if (c == ')') { vk_code = '0'; needs_shift = TRUE; }
        else if (c == '-') { vk_code = VK_OEM_MINUS; }
        else if (c == '_') { vk_code = VK_OEM_MINUS; needs_shift = TRUE; }
        else if (c == '=') { vk_code = VK_OEM_PLUS; }
        else if (c == '+') { vk_code = VK_OEM_PLUS; needs_shift = TRUE; }
        else if (c == '[') { vk_code = VK_OEM_4; } // {
        else if (c == '{') { vk_code = VK_OEM_4; needs_shift = TRUE; }
        else if (c == ']') { vk_code = VK_OEM_6; } // }
        else if (c == '}') { vk_code = VK_OEM_6; needs_shift = TRUE; }
        else if (c == '\\') { vk_code = VK_OEM_5; } // |
        else if (c == '|') { vk_code = VK_OEM_5; needs_shift = TRUE; }
        else if (c == ';') { vk_code = VK_OEM_1; } // :
        else if (c == ':') { vk_code = VK_OEM_1; needs_shift = TRUE; }
        else if (c == '\'') { vk_code = VK_OEM_7; } // "
        else if (c == '"') { vk_code = VK_OEM_7; needs_shift = TRUE; }
        else if (c == ',') { vk_code = VK_OEM_COMMA; } // <
        else if (c == '<') { vk_code = VK_OEM_COMMA; needs_shift = TRUE; }
        else if (c == '.') { vk_code = VK_OEM_PERIOD; } // >
        else if (c == '>') { vk_code = VK_OEM_PERIOD; needs_shift = TRUE; }
        else if (c == '/') { vk_code = VK_OEM_2; } // ?
        else if (c == '?') { vk_code = VK_OEM_2; needs_shift = TRUE; }
        else if (c == '`') { vk_code = VK_OEM_3; } // ~
        else if (c == '~') { vk_code = VK_OEM_3; needs_shift = TRUE; }
        // --- End Symbol Handling ---
        else {
            printf("  -> Warning: Character '%c' (ASCII %d) not handled for typing.\n", c, (int)c);
            continue; // Skip this character
        }

        if (vk_code != 0) {
            if (needs_shift) {
                send_key(VK_SHIFT, TRUE); // Press Shift
                Sleep(10);
            }

            send_key(vk_code, TRUE); // Press the actual key
            Sleep(10); // Small delay for the key press to register
            send_key(vk_code, FALSE); // Release the actual key

            if (needs_shift) {
                Sleep(10);
                send_key(VK_SHIFT, FALSE); // Release Shift
            }
        }
    }
}


int main() {
    WSADATA wsa;
    int iResult;

    printf("Initializing Winsock...\n");
    iResult = WSAStartup(MAKEWORD(2,2), &wsa);
    if (iResult != 0) {
        printf("WSAStartup failed. Code: %d\n", WSAGetLastError());
        return 1;
    }
    printf("Winsock initialized.\n");

    SOCKET sock = socket(AF_INET, SOCK_DGRAM, 0);
    if (sock == INVALID_SOCKET) {
        printf("Socket creation failed. Code: %d\n", WSAGetLastError());
        WSACleanup();
        return 1;
    }
    printf("UDP Socket created.\n");

    struct sockaddr_in server;
    memset(&server, 0, sizeof(server));
    server.sin_family = AF_INET;
    server.sin_port = htons(5000);
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(sock, (struct sockaddr*)&server, sizeof(server)) == SOCKET_ERROR) {
        printf("Bind failed. Code: %d\n", WSAGetLastError());
        closesocket(sock);
        WSACleanup();
        return 1;
    }
    printf("UDP Server listening on port 5000...\n");

    char buffer[256]; // Increased buffer size to accommodate longer text commands
    struct sockaddr_in client;
    int client_len = sizeof(client);

    while (1) {
        memset(buffer, 0, sizeof(buffer));

        int recv_len = recvfrom(sock, buffer, sizeof(buffer) - 1, 0, (struct sockaddr*)&client, &client_len);

        if (recv_len == SOCKET_ERROR) {
            printf("recvfrom failed. Code: %d\n", WSAGetLastError());
            continue;
        }

        buffer[recv_len] = '\0';

        char client_ip[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &(client.sin_addr), client_ip, INET_ADDRSTRLEN);

        printf("Received from %s:%d (%d bytes): \"%s\"\n",
               client_ip, ntohs(client.sin_port), recv_len, buffer);

        // --- Command Parsing and Execution ---
        if (strcmp(buffer, "LEFT_CLICK") == 0) {
            printf("  -> Executing LEFT_CLICK\n");
            send_left_click();
        } else if (strcmp(buffer, "RIGHT_CLICK") == 0) {
            printf("  -> Executing RIGHT_CLICK\n");
            send_right_click();
        } else if (strncmp(buffer, "MOVE:", 5) == 0) {
            int dx, dy;
            if (sscanf(buffer, "MOVE:%d:%d", &dx, &dy) == 2) {
                printf("  -> Executing MOVE: dx=%d, dy=%d\n", dx, dy);
                send_mouse_move_relative(dx, dy);
            } else {
                printf("  -> Invalid MOVE command format: %s\n", buffer);
            }
        }
        // --- Keyboard Commands (Discrete Keys) ---
        else if (strcmp(buffer, "KEY:ENTER") == 0) {
            printf("  -> Executing KEY:ENTER\n");
            simulate_key_press(VK_RETURN);
        } else if (strcmp(buffer, "KEY:BACKSPACE") == 0) {
            printf("  -> Executing KEY:BACKSPACE\n");
            simulate_key_press(VK_BACK);
        } else if (strcmp(buffer, "KEY:SPACE") == 0) {
            printf("  -> Executing KEY:SPACE\n");
            simulate_key_press(VK_SPACE);
        } else if (strcmp(buffer, "KEY:ARROW_UP") == 0) {
            printf("  -> Executing KEY:ARROW_UP\n");
            simulate_key_press(VK_UP);
        } else if (strcmp(buffer, "KEY:ARROW_LEFT") == 0) {
            printf("  -> Executing KEY:ARROW_LEFT\n");
            simulate_key_press(VK_LEFT);
        } else if (strcmp(buffer, "KEY:ARROW_DOWN") == 0) {
            printf("  -> Executing KEY:ARROW_DOWN\n");
            simulate_key_press(VK_DOWN);
        } else if (strcmp(buffer, "KEY:ARROW_RIGHT") == 0) {
            printf("  -> Executing KEY:ARROW_RIGHT\n");
            simulate_key_press(VK_RIGHT);
        }
        // --- NEW: Additional Function Keys ---
        else if (strcmp(buffer, "KEY:TAB") == 0) {
            printf("  -> Executing KEY:TAB\n");
            simulate_key_press(VK_TAB);
        } else if (strcmp(buffer, "KEY:ESCAPE") == 0) {
            printf("  -> Executing KEY:ESCAPE\n");
            simulate_key_press(VK_ESCAPE);
        } else if (strcmp(buffer, "KEY:DELETE") == 0) {
            printf("  -> Executing KEY:DELETE\n");
            simulate_key_press(VK_DELETE);
        } else if (strcmp(buffer, "KEY:HOME") == 0) {
            printf("  -> Executing KEY:HOME\n");
            simulate_key_press(VK_HOME);
        } else if (strcmp(buffer, "KEY:END") == 0) {
            printf("  -> Executing KEY:END\n");
            simulate_key_press(VK_END);
        } else if (strcmp(buffer, "KEY:PAGE_UP") == 0) {
            printf("  -> Executing KEY:PAGE_UP\n");
            simulate_key_press(VK_PRIOR); // VK_PRIOR is Page Up
        } else if (strcmp(buffer, "KEY:PAGE_DOWN") == 0) {
            printf("  -> Executing KEY:PAGE_DOWN\n");
            simulate_key_press(VK_NEXT); // VK_NEXT is Page Down
        } else if (strcmp(buffer, "KEY:CTRL") == 0) {
            printf("  -> Executing KEY:CTRL (Left Ctrl)\n");
            simulate_key_press(VK_LCONTROL);
        } else if (strcmp(buffer, "KEY:ALT") == 0) {
            printf("  -> Executing KEY:ALT (Left Alt)\n");
            simulate_key_press(VK_LMENU); // VK_LMENU is Left Alt
        } else if (strcmp(buffer, "KEY:WINDOWS") == 0) {
            printf("  -> Executing KEY:WINDOWS (Left Windows Key)\n");
            simulate_key_press(VK_LWIN);
        } else if (strcmp(buffer, "KEY:VOLUME_UP") == 0) {
            printf("  -> Executing KEY:VOLUME_UP\n");
            simulate_key_press(VK_VOLUME_UP);
        } else if (strcmp(buffer, "KEY:VOLUME_DOWN") == 0) {
            printf("  -> Executing KEY:VOLUME_DOWN\n");
            simulate_key_press(VK_VOLUME_DOWN);
        } else if (strcmp(buffer, "KEY:MUTE") == 0) {
            printf("  -> Executing KEY:MUTE\n");
            simulate_key_press(VK_VOLUME_MUTE);
        } else if (strcmp(buffer, "KEY:F1") == 0) {
            printf("  -> Executing KEY:F1\n");
            simulate_key_press(VK_F1);
        } else if (strcmp(buffer, "KEY:F2") == 0) {
            printf("  -> Executing KEY:F2\n");
            simulate_key_press(VK_F2);
        } else if (strcmp(buffer, "KEY:F3") == 0) {
            printf("  -> Executing KEY:F3\n");
            simulate_key_press(VK_F3);
        } else if (strcmp(buffer, "KEY:F4") == 0) {
            printf("  -> Executing KEY:F4\n");
            simulate_key_press(VK_F4);
        }
        // --- End New Function Keys ---
        // --- Text Input Handling ---
        else if (strncmp(buffer, "TEXT:", 5) == 0) {
            char* text_to_type = buffer + 5; // Skip "TEXT:" prefix
            printf("  -> Typing text: \"%s\"\n", text_to_type);
            send_string(text_to_type);
        }
        // --- End Text Input Handling ---
        else {
            printf("  -> Unknown command: \"%s\"\n", buffer);
        }
    }

    printf("Server shutting down...\n");
    closesocket(sock);
    WSACleanup();

    return 0;
}