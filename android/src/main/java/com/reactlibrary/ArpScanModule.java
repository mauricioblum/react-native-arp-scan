package com.reactlibrary;

import java.net.InetAddress;
import java.util.ArrayList;
import java.net.Socket;
import java.net.InetSocketAddress;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableArray;


public class ArpScanModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public static WritableArray hostList;

    public ArpScanModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ArpScan";
    }

    @ReactMethod
    public void scan(String ip, int timeout, Promise promise){
        ArrayList<String> hosts = new ArrayList<String>();
        WritableArray host_list = new WritableNativeArray();
        for (int i=1;i<255;i++){
          try {
            String host=ip + "." + i;
            if (InetAddress.getByName(host).isReachable(timeout)){
              Socket socket = new Socket();
              socket.connect(new InetSocketAddress(host, 9100), timeout);
              socket.close();
              hosts.add(host);
            }
          } catch(Exception e){

          }
        }
        String[] arrayHosts = hosts.toArray(new String[hosts.size()]);
        WritableMap info = new WritableNativeMap();
        for(String s : arrayHosts){
            info.putString("host", s);
            host_list.pushMap(info);
        }
        hostList = host_list;
        promise.resolve(hostList);
  }
}
