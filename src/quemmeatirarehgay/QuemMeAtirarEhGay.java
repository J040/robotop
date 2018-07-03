package quemmeatirarehgay;

import combat.Enemy;
import combat.Intercept;
import robocode.ScannedRobotEvent;
import robocode.HitWallEvent;
import robocode.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import robocode.AdvancedRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import utils.Coordinate;

public class QuemMeAtirarEhGay extends AdvancedRobot {

    Map<String, Enemy> enemies = new HashMap<String, Enemy>();
    int movementDirection = 1;
    int c = 0;

    public void run() {
        initialConfig();
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        this.registerRobot(e);
        this.dodge(enemies.get(e.getName()));        
        this.trackFire(getCloserEnemy());
        //this.predict(getCloserEnemy());
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        enemies.remove(e.getName());
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        double bearing = Math.abs(e.getBearing());
        if (bearing >= 0 && bearing <= 90)
            back(20);
        else
            ahead(20);
    }

    /* Utilitary Functions */
    public void initialConfig() {
        // Colors
        setBodyColor(Color.PINK);
        setGunColor(Color.MAGENTA);
        setRadarColor(Color.RED);
        setBulletColor(Color.magenta);

        // Radar
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setTurnRadarRight(Double.POSITIVE_INFINITY);
    }

    public Enemy getCloserEnemy() {
        Map.Entry<String, Enemy> closer = null;
        for (Map.Entry<String, Enemy> e : enemies.entrySet()) {
            if (closer == null || e.getValue().distance < closer.getValue().distance) {
                closer = e;
            }
        }
        return closer.getValue();
    }

    /* Scan */
    public void registerRobot(ScannedRobotEvent e) {                
        Enemy enemy = new Enemy(
                e.getName(),
                e.getEnergy(),
                e.getDistance(),
                e.getVelocity(),
                e.getBearing(),
                e.getHeading(),
                new Coordinate(e, getX(), getY())
        );
        if (enemies.containsKey(e.getName()))
            enemies.get(e.getName()).update(e, new Coordinate(getX(), getY()));
        else
            enemies.put(e.getName(), enemy);
    }

    /* Movement */
    public Boolean dodge(Enemy e) {
        if (e.energies.size() < 2)
            return false;
        setTurnRight(e.getBearing() + 90 - 30 * movementDirection);
        double previousEnergy = e.getLastEnergy();
        double changeInEnergy = previousEnergy - e.getEnergy();
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            movementDirection = -movementDirection;
            setAhead((e.distance / 4 + 25) / movementDirection);
            return true;
        }
        return false;
    }

    /* Fire */
    public void trackFire(Enemy e) {
        if (e == null)
            return;
        double absoluteBearing = getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        if (Math.abs(bearingFromGun) <= 3) {
            setTurnGunRight(bearingFromGun);
            if (getGunHeat() == 0) {
                setFire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
            }
        } else {
            setTurnGunRight(bearingFromGun);
        }
        if (bearingFromGun == 0)
            scan();
    }
    
    public void predict(Enemy e) {
        double absoluteBearing = getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
        double bulletPower = Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1);
        Intercept intercept = new Intercept();
        intercept.calculate(
                getX(),
                getY(),
                e.getPosition().x,
                e.getPosition().y,
                e.getHeading(),
                e.getVelocity(),
                bulletPower,
                0,
                e.getHeading()
        );
        double turnAngle = normalRelativeAngleDegrees(intercept.bulletHeading_deg - getGunHeading());
        setTurnGunRight(turnAngle);

        if (Math.abs(turnAngle) <= intercept.angleThreshold) {
            if ((intercept.impactPoint.x > 0)
                    && (intercept.impactPoint.x < getBattleFieldWidth())
                    && (intercept.impactPoint.y > 0)
                    && (intercept.impactPoint.y < getBattleFieldHeight())) {
                fire(bulletPower);
            }
        }
    }
}
