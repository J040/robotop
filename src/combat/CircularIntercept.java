/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combat;

import utils.Coordinate;

/**
 *
 * @author rudson
 */
public class CircularIntercept extends Intercept {

    protected Coordinate getEstimatedPosition(double time) {
        if (Math.abs(angularVelocity_rad_per_sec)
                <= Math.toRadians(0.1)) {
            return super.getEstimatedPosition(time);
        }

        double initialTargetHeading = Math.toRadians(targetHeading);
        double finalTargetHeading = initialTargetHeading
                + angularVelocity_rad_per_sec * time;
        double x = targetStartingPoint.x - targetVelocity
                / angularVelocity_rad_per_sec * (Math.cos(finalTargetHeading)
                - Math.cos(initialTargetHeading));
        double y = targetStartingPoint.y - targetVelocity
                / angularVelocity_rad_per_sec
                * (Math.sin(initialTargetHeading)
                - Math.sin(finalTargetHeading));
        return new Coordinate(x, y);
    }

}
