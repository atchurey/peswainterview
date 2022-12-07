package com.example.demo.enums;

public enum SmsProvider {

    TWILIO("TWILIO"),
    GIANT_SMS("GIANT_SMS");

    private final String code;

    SmsProvider(String code) {
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public static SmsProvider getByCode(final String code) {
        SmsProvider result = null;
        for (SmsProvider p : values()) {
            if (p.getCode().equalsIgnoreCase(code)) {
                result = p;
                break;
            }
        }
        return result;
    }
}
