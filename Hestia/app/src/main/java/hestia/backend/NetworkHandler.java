package hestia.backend;

import android.app.Application;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A singleton class which handles interaction between front and back-end. It contains methods
 * for sending 4 types of requests (GET, PUT, POST AND DELETE), along with additional methods
 * for setting up the connection to the server, sending and receiving data from the server,
 * as well as getters and setters for the ip and the port number.
 */

public class NetworkHandler extends Application {
    private final String TAG = "NetworkHandler";
    private String ip;
    private Integer port;

    public NetworkHandler(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public JsonElement GET(String endpoint) throws IOException {
        HttpURLConnection connector = this.connectToServer("GET", endpoint);
        JsonElement payload = this.getPayloadFromServer(connector);
        return payload;
    }

    public JsonElement POST(JsonObject object, String endpoint) throws IOException {
        HttpURLConnection connector = this.connectToServer("POST", endpoint);
        this.sendToServer(connector, object);
        JsonElement payload = this.getPayloadFromServer(connector);
        return payload;
    }

    public JsonElement DELETE(String endpoint) throws IOException {
        HttpURLConnection connector = this.connectToServer("DELETE", endpoint);
        JsonElement payload = this.getPayloadFromServer(connector);
        return payload;
    }

    public JsonElement PUT(JsonObject object, String endpoint) throws IOException {
        HttpURLConnection connector = this.connectToServer("PUT", endpoint);
        this.sendToServer(connector, object);
        JsonElement payload = this.getPayloadFromServer(connector);
        return payload;
    }

    /**
     * This method establishes the connection to the server, by setting the type of request
     * and the path.
     * @param requestMethod the type of request that will be sent to the server.
     * @param endpoint path to the server's endpoint.
     * @return the object responsible for setting up the connection to the server.
     * @throws IOException
     */
    private HttpURLConnection connectToServer(String requestMethod, String endpoint) throws IOException {
        String path = this.getDefaultPath() + endpoint;
        URL url = new URL(path);
        HttpURLConnection connector = (HttpURLConnection) url.openConnection();
        connector.setReadTimeout(2000);
        connector.setConnectTimeout(2000);
        connector.setRequestMethod(requestMethod);
        connector.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        return connector;
    }

    /**
     * This method sends the JsonObject object to the server.
     * @param connector the object responsible for setting up the connection to the server.
     * @param object the JsonObject that will be sent to the server.
     * @throws IOException IOException
     */
    private void sendToServer(HttpURLConnection connector, JsonObject object) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connector.getOutputStream());
        outputStreamWriter.write(object.toString());
        Log.d(TAG, object.toString());
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    /**
     * Returns the payload from the server as a JsonElement. If the response code is successful,
     * it will get the input stream from the connector. Otherwise, it will get the error stream.
     * @param connector the object responsible for setting up the connection to the server.
     * @return the payload received from the server.
     * @throws IOException IOException
     */
    private JsonElement getPayloadFromServer(HttpURLConnection connector) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Integer responseCode = connector.getResponseCode();
        Log.d(TAG, "Response code: " + responseCode);
        BufferedReader reader;
        if (this.isSuccessfulRequest(responseCode)) {
            reader = new BufferedReader(new InputStreamReader(connector.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connector.getErrorStream()));
        }

        Type returnType = new TypeToken<JsonElement>(){}.getType();
        JsonElement payload = gson.fromJson(gson.newJsonReader(reader), returnType);
        reader.close();

        return payload;
    }

    /**
     * Checks if the HTTP request was successful or not.
     * @param responseCode the response code of the request.
     * @return true if it succeeded, false otherwise.
     */
    private boolean isSuccessfulRequest(Integer responseCode) {
        return (responseCode != null && (200 <= responseCode && responseCode <= 299));
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDefaultPath() {
        return "http://" + ip + ":" + port + "/";
    }
}
