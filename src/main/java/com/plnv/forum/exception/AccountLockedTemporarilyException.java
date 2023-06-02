package com.plnv.forum.exception;

import java.time.LocalDateTime;

public class AccountLockedTemporarilyException extends RuntimeException {
    public AccountLockedTemporarilyException(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    private final LocalDateTime expirationDate;

    @Override
    public String getMessage() {
        return "ACCESS DENIED: Account is temporarily locked by " + expirationDate;
    }
}
