package org.neoflk.kim.common;

/**
 * @author neoflk
 * 创建时间：2020年04月30日
 */
public class KimException extends RuntimeException {

    public KimException() {
        super();
    }

    public KimException(String message) {
        super(message);
    }

    public KimException(String message, Throwable cause) {
        super(message, cause);
    }

    public KimException(Throwable cause) {
        super(cause);
    }

    protected KimException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, false, false);
    }
}
