package oop.project.cli.argparser;

import oop.project.cli.argparser.IRange;
import oop.project.cli.argparser.NotImplementedException;

public class ContinuousRange<T extends Comparable<T>> implements IRange<T> {
    public ContinuousRange(T lowerBound, T upperBound) {
        throw new NotImplementedException("ContinuousRange::isInRange is not implemented");
    }

    public boolean isInRange(Comparable<T> value) {
        throw new NotImplementedException("ContinuousRange::isInRange is not implemented");
    }
}
