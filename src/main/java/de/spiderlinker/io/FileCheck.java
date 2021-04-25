package de.spiderlinker.io;

import java.io.File;
import java.io.IOException;

public class FileCheck {

    /**
     * Creates the passed file (and directory) if it does not exists
     *
     * @param file file to check
     */
    public static void validateFileExists(String file) {
        /* creates a new file, if it not exists */
        validateFileExists(new File(file));
    }

    /**
     * Creates the passed file (and directory) if it does not exists
     *
     * @param file file to check
     */
    public static void validateFileExists(File file) {
        /* check, if file already exists */
        if (!file.exists()) {
            /* check, if the directory of this file exists */
            validateDirectoryExists(file.getParentFile());

            try {
                /* file does not exist, create new one */
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the passed directory if it does not exists
     *
     * @param path directory to check
     */
    public static void validateDirectoryExists(String path) {
        /* checks, if the passed path exists */
        validateDirectoryExists(new File(path));
    }

    /**
     * Creates the passed directory if it does not exists
     *
     * @param path directory to check
     */
    public static void validateDirectoryExists(File path) {
        /* Checks, if the passed path already exists */
        if (!path.exists()) {
            /* create new path, because it does not exists */
            path.mkdirs();
        }
    }
}
