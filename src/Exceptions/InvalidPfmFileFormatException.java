package Exceptions;

public class InvalidPfmFileFormatException extends Throwable {
    public InvalidPfmFileFormatException(String missingEndiannessSpecification) {
    System.out.println(missingEndiannessSpecification);
    }
}
