package org.altbeacon.beaconreference;

import android.util.Log;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * 1 대 1 소켓 통신 클라이언트 예제
 */
public class socketclient extends Thread {
    String host; // 서버 IP
    int port;    // 서버 포트
    String data; // 전송 데이터

    public socketclient(String host, int port, String data){
        this.host = host;
        this.port = port;
        this.data = data;
    }

    @Override
    public void run() {

        try{
            Socket socket = new Socket(host, port); // 소켓 열어주기

            // OutputStream - 클라이언트에서 Server로 메세지 발송
            OutputStream out = socket.getOutputStream();
            // socket의 OutputStream 정보를 OutputStream out에 넣은 뒤
            PrintWriter writer = new PrintWriter(out, true);
            // PrintWriter에 위 OutputStream을 담아 사용

            writer.println(data);
            // 클라이언트에서 서버로 메세지 보내기
            Log.d("ClientStream", "Sent to server. data: " + data);

//            ObjectInputStream instream = new ObjectInputStream(socket.getInputStream());
//            Object input = instream.readObject();
//            Log.d("ClientThread", "Received data: " + input);

            socket.close(); // 소켓 해제

        }catch(Exception e){
            Log.d("socket", "error.");
            e.printStackTrace();
        }
    }
}

