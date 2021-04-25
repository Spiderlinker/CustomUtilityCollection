package de.spiderlinker.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    /* Units of memory */
    private static final String[] UNITS = new String[]{"Bytes", "kB", "MB", "GB", "TB", "EB", "ZB",
            "YB"};

    /*
     * returns the filename without the file-extension (e.g. 'example.txt' ->
     * 'example'
     */
    public static String getExtensionlessFileName(final File file) {
        /* get name of file */
        final String name = file.getName();

        /* get index of last '.' to replace extension */
        final int i = name.lastIndexOf('.');

        /* if file has no extension, return file name */
        if (i == -1) {
            return name;
        }

        /* return file name without extension */
        return name.substring(0, i);
    }

    /*
     * deletes the passed file and all sub folders, if there are some
     */
    public static boolean delete(final File file) {
        if (file != null) {
            /* check, if passed file is a directory */
            if (file.isDirectory()) {
                /* get all subfiles of file directory */
                final File[] list = file.listFiles();

                if (list != null) {
                    for (int i = 0; i < list.length; i++) {
                        /* call this method again with subfile */
                        FileUtils.delete(list[i]);
                    }
                }
            }
            if (file.exists()) {
                /* passed file is a file and can be deleted */
                if (!file.delete()) {
                    LOGGER.error("Could not delete " + file);
                    return false;
                }

                /* file was successfully deleted */
                return true;
            }
        }
        /*
         * return false, because file is neither a directory nor a file (it may
         * be null)
         */
        return false;
    }

    public static boolean endsWith(final File file, final String... extensions) {
        /*
         * returns 'true', if the passed file ends with any passed extensions,
         * 'false' if not
         */
        return FileUtils.endsWith(file.getAbsolutePath(), extensions);
    }

    public static boolean endsWith(final String file, final String... extensions) {
        /* Check, if passed extensions are not null! */
        if (extensions == null) {
            /*
             * passed extensions are null! Passed file can not ends with passed
             * extensions (they are null!)
             */
            return false;
        }

        /* signalizes if the passed file ends with the passed extensions */
        boolean endsWithExtension = false;

        /* Go through all passed extensions */
        for (final String extension : extensions) {
            /* Check, if file ends with passed extensions */
            if (file.endsWith(extension.toLowerCase())) // TODO toLowerCase ->
            // richtigkeit
            // gewährleistet?
            {
                /* File ends with extension, break and return 'true' */
                endsWithExtension = true;
                break;
            }
        }

        /* return 'true', if passed file ends with passed extensions */
        return endsWithExtension;
    }

    public static boolean hasFileExtension(final File file) {
        /* returns a boolean if the passed file has a file extension */
        return FileUtils.getFileExtension(file) != null;
    }

    public static String getFileExtension(final File file) {
        try {
            /*
             * returns the extension of a file with a '.' Example:
             * C:\Users\*Name*\example.txt -> .txt
             */
            return file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.'));
        } catch (final StringIndexOutOfBoundsException e) {
            /* The file has no file extension! return null! */
            return null;
        }
    }

    /*
     * Extracts a file out of this application .jar to a hard drive
     */
    public static void extractFile(final String resource, final File fileTo) throws IOException {
        /* Check if the passed file to exists */
        FileCheck.validateFileExists(fileTo);

        /* create InputStream with passed resource */
        try (InputStream stream = FileUtils.class.getResourceAsStream("/" + resource)) {

            if (stream == null) {
                /* resource not found, throw exception */
                throw new FileNotFoundException("[FileUtils] Could not extract file: " + resource);
            }

            int readBytes;

            final byte[] buffer = new byte[4096];
            try (OutputStream resStreamOut = new FileOutputStream(fileTo)) {
                /* copy resource to hard drive */
                while ((readBytes = stream.read(buffer)) > 0) {
                    /* write file */
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static String formatSize(final long size) {
        if (size <= 0) {
            /* if size is less than 0, return '0 Bytes' */
            return "0 Bytes";
        }

        /* calculate size */
        final int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        /* return calculated size with matching unit */
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " "
                + FileUtils.UNITS[digitGroups];
    }

    public static String getAvailableFileName(final String path) {
        /* returns the next available file name of passed file path */
        return FileUtils.getAvailableFileName(new File(path)).getAbsolutePath();
    }

    public static File getAvailableFileName(File file) {

        /*
         * Check, if passed file exists If it not exists, return passed file
         */
        if (file.exists()) {
            /* Get extension of passed file (if it has one) */
            String fileExtension = "";

            /* Check if the file has an extension */
            if (FileUtils.hasFileExtension(file)) {
                /* Passed file has an extension, set it to extension string */
                fileExtension = FileUtils.getFileExtension(file);
            }

            /* Get path and name of passed file (without extension) */
            final String filePathName = file.getAbsolutePath().replace(fileExtension, "");

            /* Create counter */
            int counter = 2;

            /*
             * While file name exists, modify file name with count of counter =>
             * Passed: C:\Users\*Name*\example.txt -> C:\Users\*Name*\example
             * (1).txt If modified file name already exists, increase counter
             * again! ( => C:\Users\*Name*\example (2).txt) etc.
             */
            do {
                /* create new file name with new number of counter */
                file = new File(String.format("%s (%s)%s", filePathName, counter, fileExtension));

                /* Increase counter to modify the file name */
                counter++;
            }
            while (file.exists());
        }

        /* return next available file name */
        return file;
    }

    public static File getRoot(final File file) {
        /* root file of passed file */
        File root = file;

        /* get parent of this file as long as it is not null */
        while (root.getParentFile() != null) {
            /* set new root file */
            root = root.getParentFile();
        }

        /* return root */
        return root;
    }
}
