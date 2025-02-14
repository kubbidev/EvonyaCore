package me.kubbidev.evonyacore.exceptions;

public class TrackerDoesNotExistException extends Exception {
	  private static final long serialVersionUID = 1159293747235742412L;

	  public TrackerDoesNotExistException(String name) {
	    super("Error : Tracker " + name + " doesn't exist");
	  }
}
