/*
This class takes care of sending and receiving input for the phone.
Note that HttpClient is deprecated for android. Will be rewritten such that it uses
HttpUrlConnection instead of HttpClient.
 */

package com.rugged.application.hestia;

import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class RequestHandler {
//    public static void GETtest() throws IOException {
//
//        CloseableHttpClient	httpclient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet("http://127.0.0.1:5000/devices/");
//        CloseableHttpResponse response1 = httpclient.execute(httpGet);
//        InputStream is = response1.getEntity().getContent();
//        Gson gson= new Gson();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//        Type deviceListType= new TypeToken<ArrayList<Device>>() {}.getType();
//        ArrayList<Device> devices = gson.fromJson(gson.newJsonReader(reader), deviceListType);
//        System.out.println(devices);
//        reader.close();
//        response1.close();
//        httpclient.close();
//    }

    public static void POSTtest(int deviceId,int activatorId,String activatorType, Object state) throws ClientProtocolException, IOException {
//        CloseableHttpClient	httpclient = HttpClients.createDefault();
//        String path=("http://127.0.0.1:5000/devices/"+deviceId+"/"+"activator/"+activatorId);
//        HttpPost httpPost = new HttpPost(path);
//        JsonObject json=new JsonObject();;
//        if(activatorType=="bool"){
//            json.addProperty("state", (Boolean) state);;
//        }
//        else{
//            System.out.println("no");
//        }
//        StringEntity se = new StringEntity(json.toString());
//        httpPost.setEntity(se);
//        ResponseHandler responseHandler = new BasicResponseHandler();
//        HttpResponse response=  httpclient.execute(httpPost, responseHandler);
//        System.out.println(response);
//        httpclient.close();
    }
}