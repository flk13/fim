package org.neoflk.kim.common.resp;

import org.neoflk.kim.common.KimProtocol;

import java.util.Map;

/**
 * @author neoflk
 * 创建时间：2020年04月29日
 */
public class KimResponse extends KimProtocol {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private KimResponse(Builder builder) {
        this.code = builder.code;
        this.type = builder.type;
        this.sessionId = builder.sessionId;
        this.body = builder.body;
        this.attactments = builder.attactments;
    }

    public static class Builder {
        private int code;
        private byte type;
        private String sessionId;
        private Object body;
        private Map<String,String> attactments;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder type(byte type) {
            this.type = type;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder attactments(Map<String, String> attactments) {
            this.attactments = attactments;
            return this;
        }

        public KimResponse build() {
            return new KimResponse(this);
        }
    }

}
