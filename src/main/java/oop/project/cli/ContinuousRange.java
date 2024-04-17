package oop.project.cli;

import oop.project.cli.argparser.IRange;
import oop.project.cli.argparser.NotImplementedException;

public class ContinuousRange<T> implements IRange {
    public ContinuousRange(T lowerBound, T upperBound) {
        throw new NotImplementedException("ContinuousRange::isInRange is not implemented");
    }

    @Override
    public boolean isInRange(Object value) {
        throw new NotImplementedException("ContinuousRange::isInRange is not implemented");
    }
}
