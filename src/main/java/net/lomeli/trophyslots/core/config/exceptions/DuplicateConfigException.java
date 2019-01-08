package net.lomeli.trophyslots.core.config.exceptions;

public class DuplicateConfigException extends Exception {
    public DuplicateConfigException(String configName, String category) {
        super(String.format("Duplicate config with name \"%1$s\" in category \"%2$s\"", configName, category));
    }
}
