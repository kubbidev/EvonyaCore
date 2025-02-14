package me.kubbidev.evonyacore.exceptions;

public class EvonyaPlayerDoesNotExistException extends Exception {
	  private static final long serialVersionUID = 1159293747235742412L;
	  
	  public EvonyaPlayerDoesNotExistException(String name) {
	    super("Error : EvonyaPlayer " + name + " doesn't exist");
	  }
}
