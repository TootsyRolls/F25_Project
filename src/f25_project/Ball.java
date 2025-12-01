/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package f25_project;

import javafx.scene.shape.Circle;

/**
 *
 * @author Natha
 */
public class Ball {
    public static final int BALLRADIUS = 20;
    private double ballMass = 1;
    private Circle ball;
    private double PosX;
    private double PosY;
    private double velocityX;
    private double velocityY;
    private double kineticEnergy;
    private boolean bouncedX = false;
    private boolean bouncedY = false;

    public Ball(Circle ball, double PosX, double PosY) {
        this.ball = ball;
        this.PosX = PosX;
        this.PosY = PosY;
        this.velocityX = 0;
        this.velocityY = 0;
        this.kineticEnergy = 0;
        ball.setRadius(BALLRADIUS);
    }
    
    private double pythagorean(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }

    public double getBallMass() {
        return ballMass;
    }

    public void setBallMass(double ballMass) {
        this.ballMass = ballMass;
    }

    public Circle getBall() {
        return ball;
    }

    public void setBall(Circle ball) {
        this.ball = ball;
    }

    public double getPosX() {
        return PosX;
    }

    public void setPosX(double PosX) {
        this.PosX = PosX;
    }

    public double getPosY() {
        return PosY;
    }

    public void setPosY(double PosY) {
        this.PosY = PosY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getKineticEnergy() {
        return this.kineticEnergy;
    }

    public void setKineticEnergy(double kineticEnergy) {
        this.kineticEnergy = kineticEnergy;
    }

    public boolean isBouncedX() {
        return bouncedX;
    }

    public void setBouncedX(boolean bouncedX) {
        this.bouncedX = bouncedX;
    }

    public boolean isBouncedY() {
        return bouncedY;
    }

    public void setBouncedY(boolean bouncedY) {
        this.bouncedY = bouncedY;
    }

    
    
    
}
