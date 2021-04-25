package de.spiderlinker.io;

import de.spiderlinker.network.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
        // Utility class should not be instantiatable
    }

    public static String readTextFromFile(String path) throws IOException {
        /* returns the text of passed file */
        return readTextFromFile(new File(path));
    }

    public static String readTextFromFile(File file) throws IOException {
        /* returns the text of passed file */
        return readTextFromFile(file.toPath());
    }

    public static String readTextFromFile(Path path) throws IOException {
        /* returns the text of passed file */
        return new String(Files.readAllBytes(path));
    }


    public static void writeTextToFile(String text, File file) throws IOException {
        /* check, if passed file exists */
        FileCheck.validateFileExists(file);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            /* write passed object to passed file */
            writer.write(text);
        }
    }

    public static List<Object> readObjects(File path, FileFilter filter)
            throws IOException, InvalidClassException {
        /* Contains all read objects of passed path */
        List<Object> objects = new ArrayList<>();

        /* Get list of all filtered files that are included in passed path */
        File[] files = path.listFiles(filter);

        /* Check, if sub files are not null! */
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                /* read objects and add to list */
                objects.add(readObject(files[i]));
            }
        }

        /* return all objects */
        return objects;
    }

    public static Object readObject(File file) throws IOException, InvalidClassException {
        /* object will be read and returned */
        Object obj = null;

        /* ObjectInputStream and FileInputStream to read object */
        try (FileInputStream fos = new FileInputStream(file);
             ObjectInputStream stream = new ObjectInputStream(fos)) {
            /* read object from file */
            obj = stream.readObject();
        } catch (ClassNotFoundException e) {
            /*
             * This exception occurs if the read object is not found in this
             * application
             */
            LOGGER.error("Unknown object: " + e.getMessage());
        } catch (Exception e) {
            /* Throw new exception with pass of incompatible object */
            throw new InvalidClassException(file.getAbsolutePath());
        }

        /* return read object */
        return obj;
    }

    public static void writeObject(Object obj, File file)
            throws NotSerializableException, IOException {

        /* check, if passed file exists */
        FileCheck.validateFileExists(file);

        /* create ObjectOutputStream to write object to file */
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            /* write passed object to passed file */
            stream.writeObject(obj);
        }
    }


}
