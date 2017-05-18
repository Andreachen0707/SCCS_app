package com.example.dell.sccs_app.Util;

/**
 * Created by dell on 2017/5/19.
 */

public class GPS {
        private double wgLat;
        private double wgLon;

        public GPS(double wgLat, double wgLon) {
            setWgLat(wgLat);
            setWgLon(wgLon);
        }

        public double getWgLat() {
            return wgLat;
        }

        public void setWgLat(double wgLat) {
            this.wgLat = wgLat;
        }

        public double getWgLon() {
            return wgLon;
        }

        public void setWgLon(double wgLon) {
            this.wgLon = wgLon;
        }

        @Override
        public String toString() {
            return wgLat + "," + wgLon;
        }
}
