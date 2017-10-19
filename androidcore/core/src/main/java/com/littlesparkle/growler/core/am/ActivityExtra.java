package com.littlesparkle.growler.core.am;

public class ActivityExtra {

    private final String key;
    private final String value;

    public ActivityExtra(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ActivityExtra{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
