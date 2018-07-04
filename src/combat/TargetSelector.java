/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combat;

/**
 *
 * @author rudson
 */
public class TargetSelector {

    double energy;
    double energyEnemy = 0;
    double bearingFromGun = 0;
    double lastBearingFromGun = 0;
    double closeDistance = 0;
    double farDistance = 0;
    double lowEnergy = 0;
    double highEnergy = 0;
    double lowAggress = 0;
    double moderateAggress = 0;
    double highAggress = 0;
    double precision = 0.05;

    public Double resolve(Enemy e, Double energy) {
        fuzzifyDistance(e.distance);
        fuzzifyEnergy(energy);
        getAgressiveness();
        return desfuzzify();
    }

    private void fuzzifyDistance(double dist) {
        if (dist <= 200) {
            closeDistance = 1.0;
            farDistance = 0.0;
        } else if (dist > 200 && dist < 300) {
            closeDistance = (300.0 - dist) / 100.0;
            farDistance = (dist - 200.0) / 100.0;
        } else {
            closeDistance = 0.0;
            farDistance = 1.0;
        }
    }

    private void fuzzifyEnergy(double energy) {
        if (energy <= 30.0) {
            lowEnergy = 1.0;
            highEnergy = 0.0;
        } else if (energy > 30.0 && energy < 50.0) {
            lowEnergy = (50.0 - energy) / 20.0;
            highEnergy = (energy - 30.0) / 20.0;
        } else {
            lowEnergy = 0.0;
            highEnergy = 1.0;
        }
    }

    private double desfuzzify() {
        double outputLevel = 0.0;
        double controller = 0.0;

        for (double i = 0.0; i <= 3.0; i += precision) {

            //Agressividade Baixa
            if (i >= 0.0 && i < 1.0) {
                if (lowAggress != 0) {
                    outputLevel += lowAggress * i;
                    controller++;
                }
            } //Agressividade Baixa-Media
            else if (i >= 1.0 && i <= 1.5) {
                if (lowAggress > moderateAggress) {
                    if (lowAggress > 0) {
                        outputLevel += lowAggress * i;
                        controller++;
                    }
                } else {
                    if (moderateAggress > 0) {
                        outputLevel += moderateAggress * i;
                        controller++;
                    }
                }
            } //Agressividade Media
            else if (i > 1.5 && i < 2.0) {
                if (moderateAggress > 0) {
                    outputLevel += moderateAggress * i;
                    controller++;
                }
            } //Agressividade Media-Alta
            else if (i >= 2.0 && i <= 2.5) {
                if (moderateAggress > highAggress) {
                    if (moderateAggress > 0) {
                        outputLevel += moderateAggress * i;
                        controller++;
                    }
                } else {
                    if (highAggress > 0) {
                        outputLevel += highAggress * i;
                        controller++;
                    }
                }
            } //Agressividade Alta
            else if (i > 2.5) {
                if (highAggress > 0) {
                    outputLevel += highAggress * i;
                    controller++;
                }
            }
        }

        outputLevel /= controller;

        return outputLevel;
    }

    private void getAgressiveness() {
        //Energia = Alta
        if (highEnergy > 0.0) {
            //Distancia = Perto
            if (closeDistance > 0.0) {
                highAggress += highEnergy * closeDistance;
            }
            //Distancia = Longe
            if (farDistance > 0.0) {
                moderateAggress += highEnergy * farDistance;
            }
        }
        //Energia = Baixa
        if (lowEnergy > 0.0) {
            //Distancia = Perto
            if (closeDistance > 0.0) {
                moderateAggress += lowEnergy * closeDistance;
            }
            //Distancia = Longe
            if (farDistance > 0.0) {
                lowAggress += lowEnergy * farDistance;
            }
        }
    }
}
