package Exceptions;

public class InvalidPfmFileFormat extends Throwable {
    public InvalidPfmFileFormat(String s) {
        System.out.println(s);
    }
}
