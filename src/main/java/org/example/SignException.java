package org.example;

/**
 * This is a class of exception which is thrown when the value is negative
 */
public class SignException extends Exception{
    /** the value which is negative*/
    private final double value;
    /** the message to show to the user*/
    String message;

    /**
     * The constructor of the class SignException
     * @param value - the value which is negative
     * @param message - the message to show to the user
     */
    public SignException(double value, String message) {
        this.value = value;
        this.message=message;
    }


    /**
     * the method which returns the message of this exception
     * @return returns the message of this exception
     */
    @Override
    public String getMessage() {
        return "Вы ввели отрицательное значение ";
    }

    /**
     * The method that shows the data about object of this class
     * @return returns the description of exception
     */
    @Override
    public String toString()
    {
        return getMessage()+message+":"+value;
    }
}