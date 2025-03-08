package com.img;

/**
 * @author : IMG
 * @create : 2025/3/8
 */
public class RouteInfo {
    private int to;
    private double weight;

    public RouteInfo(int to, double weight) {
        this.to = to;
        this.weight = weight;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "RouteInfo{" +
                "to=" + to +
                ", weight=" + weight +
                '}';
    }
}
