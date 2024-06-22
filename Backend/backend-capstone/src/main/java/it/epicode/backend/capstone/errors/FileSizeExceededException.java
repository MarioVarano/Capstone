package it.epicode.backend.capstone.errors;


public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException(String message) {
        super(message);
    }
}

