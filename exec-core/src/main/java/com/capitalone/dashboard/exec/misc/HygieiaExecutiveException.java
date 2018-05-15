package com.capitalone.dashboard.exec.misc;

public class HygieiaExecutiveException extends Exception {
    private static final long serialVersionUID = 4596406816345733781L;

    private int errorCode = 0;

    public HygieiaExecutiveException (String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HygieiaExecutiveException (String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public HygieiaExecutiveException (Throwable cause) {
        super(cause);
    }

    public HygieiaExecutiveException (String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
