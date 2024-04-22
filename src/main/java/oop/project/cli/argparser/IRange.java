package oop.project.cli.argparser;

public interface IRange<T extends Comparable<T>> {
    public boolean isInRange(T value);
    public String toString();
}
