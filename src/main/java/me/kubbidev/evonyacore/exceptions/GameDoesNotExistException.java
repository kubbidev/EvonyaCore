package me.kubbidev.evonyacore.exceptions;

public class GameDoesNotExistException extends Exception {
	  private static final long serialVersionUID = 1159293747235742412L;

	  public GameDoesNotExistException(String name) {
	    super("Error : Game " + name + " doesn't exist");
	  }
}
