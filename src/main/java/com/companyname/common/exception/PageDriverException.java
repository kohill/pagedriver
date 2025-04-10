package com.companyname.common.exception;

public class PageDriverException extends RuntimeException{

        private static final long serialVersionUID = 799049911177464448L;

        public PageDriverException(String message) {
            super(message);
        }

    public PageDriverException(String message, Throwable cause) {
        super(message, cause);
    }
}
