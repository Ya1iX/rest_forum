package com.plnv.forum.exception;

public class AccountLockedPermanentlyException extends RuntimeException {
    @Override
    public String getMessage() {
        return "ACCESS DENIED: Account is locked permanently";
    }
}
