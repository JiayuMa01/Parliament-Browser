package org.ppr.abschlussprojekt.helper;

/**
 * @author Jiayu Ma
 * Helper to handel the input exception in UserInterface
 */
public class InputException extends Exception{

    public InputException() {
    }

    public InputException(String message) {
        super(message);
    } //konstruktor bekommt ein String von User input.
}
