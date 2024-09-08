package pw1.Task3;

public enum FileType {
    XML(".xml"), JSON(".json"), XLS(".xls");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
