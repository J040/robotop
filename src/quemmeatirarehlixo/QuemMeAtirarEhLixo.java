package quemmeatirarehlixo;

import combat.Enemy;
import combat.Intercept;
import combat.TargetSelector;
import robocode.ScannedRobotEvent;
import robocode.HitWallEvent;
import robocode.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import robocode.AdvancedRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import utils.Coordinate;

public class QuemMeAtirarEhLixo extends AdvancedRobot {

    Map<String, Enemy> enemies = new HashMap<String, Enemy>();
    int movementDirection = 1;
    int c = 0;
    String lastTarget;
    final Integer TARGET_STRATEGY = 2;
    Intercept intercept = new Intercept();
    public void run() {        
        initialConfig();
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        this.registerRobot(e);
        this.dodge(enemies.get(e.getName()));

        Enemy enemy = getCloserEnemy();
        //if(enemy.distance <= 1000) {
            this.trackFire(enemy);
//        } else {
//            this.predict(enemy);
//        }
        lastTarget = enemy.name;
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        enemies.remove(e.getName());
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        double bearing = Math.abs(e.getBearing());
        if (bearing >= 0 && bearing <= 90) {
            back(20);
        } else {
            ahead(20);
        }
    }

    @Override
    public void onBulletHit(BulletHitEvent e) {
        enemies.get(e.getName()).hitted++;
    }

    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        enemies.get(lastTarget).dodges++;
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        enemies.get(e.getName()).damageDealt += e.getBullet().getPower();
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

//    public Enemy getEnemyByAgressiveness() {
//        TargetSelector ts = new TargetSelector();
//        Enemy enemy = null;
//        Double agressiveness = 0.0;
//        for(Map.Entry<String, Enemy> e : enemies.entrySet()) {
//            Double temp = ts.resolve(e.getValue(), this.getEnergy());
//            if(temp > agressiveness) {
//                enemy = e.getValue();
//                agressiveness = temp;
//            }
//        }
//        return enemy;
//    }

    /* Scan */
    public void registerRobot(ScannedRobotEvent e) {
        Enemy enemy = new Enemy(
                e.getName(),
                e.getEnergy(),
                e.getDistance(),
                e.getVelocity(),
                e.getBearing(),
                e.getBearingRadians(),
                e.getHeading(),
                e.getHeadingRadians(),
                new Coordinate(e, getX(), getY())
        );
        if (enemies.containsKey(e.getName())) {
            enemies.get(e.getName()).update(e, new Coordinate(getX(), getY()));
        } else {
            enemies.put(e.getName(), enemy);
        }
    }

    /* Movement */
    public Boolean dodge(Enemy e) {
        if (e.energies.size() < 2) {
            return false;
        }
        setTurnRight(e.getBearing() + 105 - 30 * movementDirection); // 90
        double previousEnergy = e.getLastEnergy();
        double changeInEnergy = previousEnergy - e.getEnergy();
        if (changeInEnergy > 0 && changeInEnergy <= 3) {
            movementDirection = -movementDirection;
            setAhead((e.distance / 4 + 50) / movementDirection); // 25
            return true;
        }
        return false;
    }

    /* Fire */
    public void trackFire(Enemy e) {
        if (e == null) {
            return;
        }
        double absoluteBearing = getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        if (Math.abs(bearingFromGun) <= 3) {
            setTurnGunRight(bearingFromGun);
            if (getGunHeat() == 0) {
                TargetSelector ts = new TargetSelector();
                setFire(ts.resolve(e, getEnergy()));
            }
        } else {
            setTurnGunRight(bearingFromGun);
        }
        if (bearingFromGun == 0) {
            scan();
        }
    }

    public void predict(Enemy e) {
        TargetSelector ts = new TargetSelector();
        double bulletPower = ts.resolve(e, getEnergy());
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double angularVelocity = e.getVelocity() * Math.sin(e.getHeadingRadians() - absoluteBearing);
        //Intercept intercept = new Intercept();
        intercept.calculate(
                getX(),
                getY(),
                e.getPosition().x,
                e.getPosition().y,
                e.getHeadingRadians(),
                e.getVelocity(),
                bulletPower,
                angularVelocity,
                getWidth() / 2
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
