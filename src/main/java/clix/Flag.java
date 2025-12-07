package clix;

public class Flag {

    private String flag;

    private String value;


    public Flag(String flag, String value) {
        this.flag = flag;
        this.value = value;
    }

    public String getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return flag + (value == null ? "" : "=%s".formatted(value));
    }
}
