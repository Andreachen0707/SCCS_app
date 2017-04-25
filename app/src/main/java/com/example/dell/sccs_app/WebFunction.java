package com.example.dell.sccs_app;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dell on 2017/4/12.
 */

public class WebFunction {
    public Map<String,String> headpush;
    private WebClient mClient;

    private Timer _axTimer = new Timer();
    private int mapIndex = 0;

    private static final int STATUS_CLOSE = 0;
    private static final int STATUS_CONNECT = 1;
    private static final int STATUS_MESSAGE = 2;

    public static Map<String,String> getRequestHeader(Map<String,List<String>> header){
        Map<String,String> mapnew = new HashMap<String,String>();
        Iterator<String> HeaderIterator = header.keySet().iterator();
        while (HeaderIterator.hasNext()){
            String key = HeaderIterator.next();
            String value = header.get(key).toString();
            mapnew.put(key,value);
        }
        return mapnew;
    }

    public static Map<String,String> getRequestHeader(HttpURLConnection conn) {
        //https://github.com/square/okhttp/blob/master/okhttp-urlconnection/src/main/java/okhttp3/internal/huc/HttpURLConnectionImpl.java#L236
        Map<String, List<String>> requestHeaderMap = conn.getRequestProperties();
        Map<String,String> mapnew = new HashMap<String,String>();
        Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
        //StringBuilder sbRequestHeader = new StringBuilder();
        while (requestHeaderIterator.hasNext()) {
            String requestHeaderKey = requestHeaderIterator.next();
            Log.i("request",requestHeaderKey);
            String requestHeaderValue = conn.getRequestProperty(requestHeaderKey);
            Log.i("value",requestHeaderValue);
            mapnew.put(requestHeaderKey,requestHeaderValue);

        }
        return mapnew;
    }


    public static Map get_head(Map<String,List<String>> headers,String sid){
        /**String cookieList = headers.get("Cookie");
         if (null == cookieList) {
         cookieList = new String();
         }

         cookieList = sid;
         headers.put("Cookie",cookieList);
         */
        List<String> cookieList = headers.get("Cookie");
        if (null == cookieList) {
            cookieList = new ArrayList<>();
        }
        cookieList.add("AXWEBSID="+sid); // set your cookie value here
        headers.put("Cookie", cookieList);
        return headers;
    }

    public class Client extends WebSocketClient {

        public Client(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        public Client(URI serverUri, Draft draft,Map<String, String> httpHeaders, int connectTimeout){
            super(serverUri,draft,httpHeaders,connectTimeout);
        }

        @Override
        public void onOpen(ServerHandshake handShakeData) {
            startHeartTimer(true);
            Log.i("handshake","success");
            Message msg = new Message();
            msg.what = STATUS_CONNECT;
            msg.obj = String.format("[Welcome：%s]", getURI());
            mHandle.sendMessage(msg);
        }

        @Override
        public void onMessage(String message) {
            Log.i("msg",message);
            /**
             * Message msg = new Message();
             * msg.what = STATUS_MESSAGE;
             * msg.obj = message;
             * mHandle.sendMessage(msg);
             */
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i("Login",reason);
            startHeartTimer(false);
            /**
             * Message msg = new Message();
             *  msg.what = STATUS_CLOSE;
             msg.obj = String.format("[Bye：%s]", getURI());
             mHandle.sendMessage(msg);
             */

        }

        @Override
        public void onWebsocketPong(WebSocket conn, Framedata f) {
            super.onWebsocketPong(conn, f);
            String value = parseFramedata(f);

            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "pong:" + value;
            mHandle.sendMessage(msg);
        }


        @Override
        public void onWebsocketPing(WebSocket conn, Framedata f) {
            super.onWebsocketPing(conn, f);
            Log.i("ping",f.toString());

            String value = parseFramedata(f);

            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "ping:" + value;
            mHandle.sendMessage(msg);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }

        /* ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    super.beforeRequest(headers);
                    List<String> cookieList = headers.get("Cookie");
                    if (null == cookieList) {
                        cookieList = new ArrayList<>();
                    }
                    cookieList.add("AXWEBSID=" + 1); // set your cookie value here
                    headers.put("Cookie", cookieList);}
            }).build();*/

        public String parseFramedata(Framedata framedata) {
            String result = "null";
            ByteBuffer buffer = framedata.getPayloadData();
            if (null == buffer) {
                return result;
            }
            byte[] data = buffer.array();
            if (null != data && data.length > 0) {
                return new String(data);
            }
            return result;
        }


        public void startHeartTimer(boolean start) {
            if (start) {
                if (_axTimer == null)
                    _axTimer = new Timer();
                _axTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // TODO 自动生成的方法存根
                        mClient.sendMessage("login", "heart-beat","1", "" , "" , "");
                    }
                }, 1000, 60000);
            } else {
                _axTimer.cancel();
            }
        }


    }

    public class WebClient extends WebsocketTest{

        public WebClient(URI serverUri, Draft draft) {
            super(serverUri,draft);
        }

        /*public WebClient(URI serverUri, Draft draft,Map<String, List<String>> httpHeaders, int connectTimeout){
            super(serverUri,draft,httpHeaders,connectTimeout);
        }*/
        public WebClient(URI serverUri, Draft draft,Map<String, String> httpHeaders, int connectTimeout){
            super(serverUri,draft,httpHeaders,connectTimeout);
        }

        @Override
        public void onOpen(ServerHandshake handShakeData) {
            Log.i("handshake","success");
            if(mClient.isOpen()==true) {
                startHeartTimer(true);
            }
            /*Message msg = new Message();
            msg.what = STATUS_CONNECT;
            msg.obj = String.format("[Welcome：%s]", getURI());
            mHandle.sendMessage(msg);*/
        }

        @Override
        public void onMessage(String message) {
            Log.i("msg",message);
            /**
             * Message msg = new Message();
             * msg.what = STATUS_MESSAGE;
             * msg.obj = message;
             * mHandle.sendMessage(msg);
             */
        }

        @Override
        public void onWebsocketMessageFragment (WebSocket conn, Framedata f){
            Log.i("frame",f.getPayloadData().toString());
        }
        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i("Login",reason);
            startHeartTimer(false);
            /**
             * Message msg = new Message();
             *  msg.what = STATUS_CLOSE;
             msg.obj = String.format("[Bye：%s]", getURI());
             mHandle.sendMessage(msg);
             */

        }

        @Override
        public void onWebsocketPong(WebSocket conn, Framedata f) {
            super.onWebsocketPong(conn, f);
            String value = parseFramedata(f);
            Log.i("ping",f.toString());

            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "pong:" + value;
            mHandle.sendMessage(msg);
        }


        @Override
        public void onWebsocketPing(WebSocket conn, Framedata f) {
            super.onWebsocketPing(conn, f);
            String result = parseFramedata(f);
            Log.i("ping",result);

            String value = parseFramedata(f);

            Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "ping:" + value;
            mHandle.sendMessage(msg);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }

        /* ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    super.beforeRequest(headers);
                    List<String> cookieList = headers.get("Cookie");
                    if (null == cookieList) {
                        cookieList = new ArrayList<>();
                    }
                    cookieList.add("AXWEBSID=" + 1); // set your cookie value here
                    headers.put("Cookie", cookieList);}
            }).build();*/

        public String parseFramedata(Framedata framedata) {
            String result = "null";
            ByteBuffer buffer = framedata.getPayloadData();
            if (null == buffer) {
                return result;
            }
            byte[] data = buffer.array();
            if (null != data && data.length > 0) {
                return new String(data);
            }
            return result;
        }

        public void senddata(String cmd,String ctrl,String version,String cuid,String ctype,String data){

            String type = cmd + "|" + ctrl + "|" + version + "|" + getNextMappingIndex();
            String message = "0|" + type + "|1||" + "zh_CN" + "|" + cuid + "|" + ctype + "|" + data;

            FramedataImpl1 resp = new FramedataImpl1(Framedata.Opcode.TEXT);
            //System.out.println("=============WebSocket send:" + message);
            try {
                resp.setFin(true);
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                resp.setPayload(buffer);
                mClient.sendFrame(resp);
                /**
                 * WSManager.notifySocketEvent(new WSocketEvent(_self,WSEventType.WS_SEND, message));
                 */
            } catch (Exception e) {
// TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

        public void sendMessage(String cmd,String ctrl,String version,String cuid,String ctype,String data){
            String type = cmd + "|" + ctrl + "|" + version + "|" + getNextMappingIndex();
            String message = "0|" + type + "|1||" + "zh_CN" + "|" + cuid + "|" + ctype + "|" + (data!=null?data:"");
            FramedataImpl1 resp = new FramedataImpl1(Framedata.Opcode.PING);
            //System.out.println("=============WebSocket send:" + message);
            try {
                resp.setFin(true);
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                resp.setPayload(buffer);
                mClient.sendFrame(resp);
                /**
                 * WSManager.notifySocketEvent(new WSocketEvent(_self,WSEventType.WS_SEND, message));
                 */
            } catch (Exception e) {
// TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }




        public int getNextMappingIndex() {
            return mapIndex++;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;

                default:
                    break;
            }
        }
    };

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = String.format("[%d] %s\n", System.currentTimeMillis(), msg.obj.toString());
            //tvMsg.append(message);
        }
    };

    public void connectToServer(String ip, String port, String type) {
        String address = String.format("ws://%s:%s/api/ws", ip, port);
        Draft draft = new Draft_17();


        try {
            URI uri = new URI(address);
            if(type=="1"){
                mClient = new WebClient(uri, draft);
                mClient.connect();
                startHeartTimer(true);
                if(mClient.isOpen()==true) {
                    startHeartTimer(true);
                    Log.i("Test new","Success");

                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("cmd", "push-lcu");
                        jsonParam.put("ctrl", "action");
                        jsonParam.put("ver", "1");

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    mClient.senddata("login","add-listener","1","","",jsonParam.toString());
                }
                else
                    Log.i("Test new","fail");
            }
            else if(type == "0"){
                mClient = new WebClient(uri, draft);
                mClient.connect();
                if(mClient.isOpen()==true) {
                    startHeartTimer(true);
                    Log.i("Test new","Success");
                }
                else
                    Log.i("Test new","fail");
            }

            //mClient.onWebsocketPing(mClient,null);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        /**
         * catch (InterruptedException f){
         f.printStackTrace();
         }
         */


    }

    public void startHeartTimer(boolean start) {
        if (start) {
            if (_axTimer == null)
                _axTimer = new Timer();
            _axTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // TODO 自动生成的方法存根
                    mClient.sendMessage("login", "heart-beat","1", "" , "" , "");
                }
            }, 1000, 5000);
        } else {
            _axTimer.cancel();
        }
    }

    public int getNextMappingIndex() {
        return mapIndex++;
    }
}
