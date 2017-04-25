package com.example.dell.sccs_app;

/**
 * Created by Administrator on 2017/4/8.
 */

public class HeartConnect extends Thread {
    @Override
    public void run(){
        while(true)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.print("fuck");
        }
    }
}
