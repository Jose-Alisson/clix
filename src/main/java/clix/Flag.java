package clix;

import clix.types.ArgumentType;

public class Flag extends Argument {

    private final String flag;

    private final String value;

    public Flag(String flag, String value) {
        super(flag);
        this.flag = flag;
        this.value = value;
    }

    public String getFlag() {
        return flag;
    }

    public String getValue() { return value; }

    @Override
    public String toString() {
        return flag + (value == null ? "" : "=%s".formatted(value));
    }

    @Override
    public ArgumentType getType() {
        return ArgumentType.FLAG;
    }
}
