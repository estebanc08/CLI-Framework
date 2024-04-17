package oop.project.cli.argparser;

public interface IRange<T extends Comparable<?>> {
    public boolean isInRange(Comparable<T> value);
}
