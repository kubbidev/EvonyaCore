package me.kubbidev.evonyacore.exceptions;

public class EvonyaStatisticDoesNotExistException extends Exception {
    private static final long serialVersionUID = 1159293747235742412L;

    public EvonyaStatisticDoesNotExistException(String name) {
        super("Error : EvonyaStatistic " + name + " doesn't exist");
    }
}
