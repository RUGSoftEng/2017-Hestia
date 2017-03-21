package com.rugged.application.hestia; /**
 * Currently we use BufferedReader for Input
 *   and DataOutputStream for Output
 */

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import json.simple.*;

public class Client {
    private static final String TAG = "Client";

    private Socket clientSocket;
    private DataOutputStream outToServer;

    public Client(String address, Integer port) {
        this.openClientSocket(address, port);
        this.openOutToServerStream();
    }

    private void openClientSocket(String address, Integer port) {
        try {
            this.clientSocket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openOutToServerStream() {
        try {
            this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendActionRequest(Integer id, String action) {
        // Write JSON
        JSONObject jsonObj = new JSONObject();
        // Stripping the text from the new-line separators is necessary to avoid
        // Strings line "abc...\n" -> the "\n" would be visible
        jsonObj.put("type", action);
        jsonObj.put("id", id);
        JSONObject jsonAction = new JSONObject();
        jsonAction.put("action", jsonObj);

        this.writeJSONToServer(jsonAction);
    }

    private void writeJSONToServer(JSONObject jsonObj) {
        try {
            this.outToServer.writeBytes(jsonObj.toJSONString() + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClientSocket() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
