/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combat;

import java.util.ArrayList;
import java.util.List;
import robocode.ScannedRobotEvent;
import utils.Coordinate;

/**
 *
 * @author rudson
 */
public class Enemy {    
    final int MAX_REG = 2;
    
    public String name;
    public Double distance;
    public Integer dodges = 0;
    public Integer hitted = 0;
    public Double damageDealt = 0.0;
    
    public List<Double> energies = new ArrayList<>();
    public List<Double> velocities = new ArrayList<>();
    public List<Double> bearings = new ArrayList<>();    
    public List<Double> bearingsRadians = new ArrayList<>();
    public List<Double> headings = new ArrayList<>();
    public List<Double> headingsRadians = new ArrayList<>();
    public List<Coordinate> positions = new ArrayList<>();
    
    public Enemy(String name, Double energy, Double distance, Double velocity, Double bearing, Double bearingRadians, Double heading, Double headingRadians, Coordinate position) {
        this.name = name;
        this.distance = distance;
        energies.add(energy);
        velocities.add(velocity);
        bearings.add(bearing);
        bearingsRadians.add(bearingRadians);
        headings.add(heading);
        headingsRadians.add(headingRadians);
        positions.add(position);
    }
    
    public void update(ScannedRobotEvent robot, Coordinate c) {
        addEnergy(robot.getEnergy());
        addVelocity(robot.getVelocity());
        addBearing(robot.getBearing());
        addHeading(robot.getHeading());
        
        positions.add(0, new Coordinate(robot, c.x, c.y));
        if(positions.size() > MAX_REG)
            positions.remove(MAX_REG);
                
        this.distance = robot.getDistance();
    }
    
    public Double getEnergy() {
        return this.energies.get(0);
    }
    
    public Double getLastEnergy() {
        return this.energies.get(1);
    }
    
    public Double getVelocity() {
        return this.velocities.get(0);
    }
    
    public Double getLastVelocity() {
        return this.velocities.get(1);
    }
    
    public Double getBearing() {
        return this.bearings.get(0);
    }
    
    public Double getLastBearing() {
        return this.bearings.get(1);
    }
    
    public Double getBearingRadians() {
        return this.bearingsRadians.get(0);
    }
    
    public Double getLastBearingRadians() {
        return this.bearingsRadians.get(1);
    }
    
    public Double getHeading() {
        return this.headings.get(0);
    }
    
    public Double getLastHeading() {
        return this.headings.get(1);
    }
    
    public Double getHeadingRadians() {
        return this.headingsRadians.get(0);
    }
    
    public Double getLastHeadingRadians() {
        return this.headingsRadians.get(1);
    }
    
    public Coordinate getPosition() {
        return this.positions.get(0);
    }
    
    public Coordinate getLastPosition() {
        return this.positions.get(1);
    }
    
    public void addEnergy(Double energy) {        
        energies.add(0, energy);
        if(energies.size() > MAX_REG)
            energies.remove(MAX_REG);
    }
    
    public void addVelocity(Double velocity) {
        velocities.add(0, velocity);        
        if(velocities.size() > MAX_REG)
            velocities.remove(MAX_REG);
    }
    
    public void addBearing(Double bearing) {
        bearings.add(0, bearing);
        if(bearings.size() > MAX_REG)
            bearings.remove(MAX_REG);
    }      
    
    public void addBearingRadians(Double radians) {
        bearingsRadians.add(0, radians);
        if(bearingsRadians.size() > MAX_REG)
            bearingsRadians.remove(MAX_REG);
    }
    
    public void addHeading(Double heading) {
        headings.add(0, heading);
        if(headings.size() > MAX_REG)
            headings.remove(MAX_REG);
    }
    
    public void addHeadingRadians(Double radians) {
        headingsRadians.add(0, radians);
        if(headingsRadians.size() > MAX_REG)
            headingsRadians.remove(MAX_REG);
    }
    
    public Boolean isStopped() {
        for(int i = 1; i < positions.size(); i++) {
//            if(
//                (positions.get(i).x != positions.get(i - 1).x &&
//                Math.abs(positions.get(i).x - positions.get(i - 1).x) >= 15)
//                    ||
//                (positions.get(i).y != positions.get(i - 1).y &&
//                Math.abs(positions.get(i).y - positions.get(i - 1).y) >= 15)
//              )
            if(positions.get(i).x != positions.get(i - 1).x || positions.get(i).y != positions.get(i - 1).y)
                return false;
        }
        return true;
    }
}
