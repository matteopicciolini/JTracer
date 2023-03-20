package Exceptions;

/**
 * This class contains the exceptions for the invalid reading format of the PFM image that
 * will be read by pfm_creator's methods
 */
public class InvalidPfmFileFormat extends Throwable {
    public InvalidPfmFileFormat(String mex) {
        super(mex);
    }
}