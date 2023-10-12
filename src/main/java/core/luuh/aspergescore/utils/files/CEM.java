package core.luuh.aspergescore.utils.files;

public enum CEM {

    ERROR_CONSOLE_EXECUTE("error-console-execute"),
    NOT_ENOUGH_ARGUMENTS("not-enough-arguments"),
    NOT_ENOUGH_ARGS("not-enough-arguments"),
    NOT_ENOUGH_PERMS("not-enough-perms"),
    NOT_ENOUGH_PERMISSIONS("not-enough-perms"),
    INVALID_PLAYER("invalid-player"),
    INVALID_ARG("invalid-arg"),
    INVALID_ARGS("invalid-arg");

    private final String path;

    CEM(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
