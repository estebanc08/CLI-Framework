package oop.project.cli.argparser;
import java.util.ArrayList;
import java.util.List;

public class DiscreteValues<T extends Comparable<T>> implements IRange<T> {
    private final ArrayList<T> values;

    @SafeVarargs  // is this even true? I think so...
    public DiscreteValues(T... values) {
        this.values = new ArrayList<>(List.of(values));
    }

    @Override
    public boolean isInRange(T value) { return values.contains(value); }

    @Override
    public String toString() { return "Discrete Value Range: " + values.toString(); }
}
