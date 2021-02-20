package com.feup.nemiprotouno;

// ----------------------------------------------------------------------------
//
//  NEMMI is developed by Alexandre Clément at the University of Porto's Faculty of Engineering
//  Released under GNU Affero General Public License version 3
//  https://opensource.org/licenses/AGPL-3.0
//
// ----------------------------------------------------------------------------
//
//  UdpClient
//  Class responsible for network communication
//  Usage: UdpClient(context, port);
//  Create and open a network socket to communicate with PD patch
//
// ----------------------------------------------------------------------------

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UdpClient extends AsyncTask<Object, String, Object> {

    private final WeakReference<MainActivity> activityWeakReference;

    boolean run;
    byte[] message = new byte[8000];
    DatagramPacket packet;
    DatagramSocket udpSocket;

    private final int port;

    String logTAG;

    UdpClient(MainActivity activity, int port) {
        this.activityWeakReference = new WeakReference<>(activity);
        this.port = port;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        this.logTAG = InteractionActivity.TAG;
        this.run = true;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            this.udpSocket = new DatagramSocket(null);
            this.udpSocket.setReuseAddress(true);
            this.udpSocket.bind(new InetSocketAddress(this.port));

        } catch (IOException e) {
            Log.d(logTAG, "UDP client has IOException error: ", e);
            this.run = false;
        }

        Log.d(logTAG, "UDP client: waiting to receive");

        while (this.run) {
            try {
                String status = "";
                this.message = new byte[8000];
                this.packet = new DatagramPacket(this.message, this.message.length);
                this.udpSocket.receive(this.packet);
                String text = new String(this.message, 0, this.packet.getLength());

                InetAddress sender = this.packet.getAddress();
                text = text.replaceAll("([\\r\\n])", "");

                switch (text) {
                    case "test start;":
                        this.message = ("OK").getBytes();
                        status = "OK";
                        break;
                    case "test end;":
                        this.message = ("OL").getBytes();
                        status = "OL";
                        break;
                    case "new test;":
                        this.message = ("OM").getBytes();
                        status = "OM";
                        break;
                    case "soundoff;":
                        this.message = ("ON").getBytes();
                        status = "ON";
                        break;
                }

                this.packet = new DatagramPacket(this.message, this.message.length, sender, 64000);

                try {
                    this.udpSocket.send(this.packet);
                    publishProgress(status);

                } catch (IOException e) {
                    Log.d(logTAG, "Error sending packet.");
                    e.printStackTrace();
                    return null;
                }

            } catch (IOException e) {
                Log.d(logTAG, "UDP client has IOException error: ", e);
                this.udpSocket.close();
                this.run = false;
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... msg) {
        super.onProgressUpdate(msg);

        MainActivity activity= activityWeakReference.get();

        activity.doMessageReceived(msg[0]);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}