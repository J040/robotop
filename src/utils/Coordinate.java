/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import robocode.ScannedRobotEvent;

/**
 *
 * @author rudson
 */
public class Coordinate {
    public Double x, y;
    
    public Coordinate() {
        
    }
    
    public Coordinate(ScannedRobotEvent e, double x, double y) {
        double absBearing = e.getBearingRadians() + e.getHeadingRadians();
        this.x = x + e.getDistance() * Math.sin(absBearing);
        this.y = y + e.getDistance() * Math.cos(absBearing);
    }
    
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
