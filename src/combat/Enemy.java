/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combat;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import sun.misc.Queue;

/**
 *
 * @author rudson
 */
public class Enemy {    
    final int MAX_REG = 2;
    
    public String name;
    public Double distance;
    
    public List<Double> energies = new ArrayList<>();
    public List<Double> velocities = new ArrayList<>();
    public List<Double> bearings = new ArrayList<>();    
    public List<Double> headings = new ArrayList<>();
    
    public Enemy(String name, Double energy, Double distance, Double velocity, Double bearing, Double heading) {        
        this.name = name;
        this.distance = distance;
        
        energies.add(energy);
        velocities.add(velocity);
        bearings.add(bearing);
        headings.add(heading);
    }
    
    public void update(ScannedRobotEvent robot) {
        addEnergy(robot.getEnergy());
        addVelocity(robot.getVelocity());
        addBearing(robot.getBearing());
        addHeading(robot.getHeading());
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
    
    public Double getHeading() {
        return this.headings.get(0);
    }
    
    public Double getLastHeading() {
        return this.headings.get(1);
    }
    
    public void addEnergy(Double energy) {
        energies.add(energy);
        if(energies.size() > MAX_REG)
            energies.remove(MAX_REG - 1);
    }
    
    public void addVelocity(Double velocity) {
        velocities.add(velocity);
        if(velocities.size() > MAX_REG)
            velocities.remove(MAX_REG - 1);
    }
    
    public void addBearing(Double bearing) {
        bearings.add(bearing);
        if(bearings.size() > MAX_REG)
            bearings.remove(MAX_REG - 1);
    }      
    
    public void addHeading(Double heading) {
        headings.add(heading);
        if(headings.size() > MAX_REG)
            headings.remove(MAX_REG - 1);
    }        
}
