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
    public List<Double> headings = new ArrayList<>();
    public List<Coordinate> positions = new ArrayList<>();
    
    public Enemy(String name, Double energy, Double distance, Double velocity, Double bearing, Double heading, Coordinate position) {
        this.name = name;
        this.distance = distance;
        energies.add(energy);
        velocities.add(velocity);
        bearings.add(bearing);
        headings.add(heading);
        positions.add(position);
    }
    
    public void update(ScannedRobotEvent robot, Coordinate c) {
        addEnergy(robot.getEnergy());
        addVelocity(robot.getVelocity());
        addBearing(robot.getBearing());
        addHeading(robot.getHeading());
        positions.add(0, new Coordinate(robot, c.x, c.y));
                
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
    
    public void addHeading(Double heading) {
        headings.add(0, heading);
        if(headings.size() > MAX_REG)
            headings.remove(MAX_REG);
    }        
}
