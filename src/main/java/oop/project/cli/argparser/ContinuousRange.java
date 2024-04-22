package oop.project.cli.argparser;

public class ContinuousRange<T extends Comparable<T>> implements IRange<T> {
    private final T lowerBound;
    private final T upperBound;

    public ContinuousRange(T lower, T upper) {
        this.lowerBound = lower;
        this.upperBound = upper;
    }

    @Override
    public boolean isInRange(T value) {
        return value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) <= 0;
    }

    @Override
    public String toString() { return "Continuous Range: ["+lowerBound+","+upperBound+"]"; }
}
