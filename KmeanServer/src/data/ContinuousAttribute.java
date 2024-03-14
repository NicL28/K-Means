package data;

public class ContinuousAttribute extends Attribute {
    private double min;
    private double max;

    public ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    public double getScaledValue(double v) {
        return (v - min) / (max - min);
    }

}
