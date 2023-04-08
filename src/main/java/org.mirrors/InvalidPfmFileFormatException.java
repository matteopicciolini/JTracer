package org.mirrors;

/**
 * This class contains the exceptions for the invalid reading format of the PFM image that
 * will be read by pfm_creator's methods
 */
public class InvalidPfmFileFormatException extends Throwable {
    public InvalidPfmFileFormatException(String mex) {
        super(mex);
    }
}
