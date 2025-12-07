package clix;

public class Argument {

    private String name;

    private String description;

    public Argument(String argument){
        name = argument;
    }

    public Argument(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "%s%s".formatted(name, description == null ? "" : " " + description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
