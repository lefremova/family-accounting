package org.example.com.family.accounting.exceptions;

import org.springframework.lang.Nullable;

public class DuplicateObjectException extends Exception {
    public DuplicateObjectException(@Nullable String msg) {
        super(msg);
    }

    public DuplicateObjectException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
