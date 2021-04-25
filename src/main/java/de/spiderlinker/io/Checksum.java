package de.spiderlinker.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class Checksum {

    public static String getMD5Checksum(String filename)
            throws NoSuchAlgorithmException, IOException {
        /* return converted checksum */
        return (filename == null || filename.trim().isEmpty()) ? null
                : getToHex(createMD5Checksum(filename));
    }

    private static byte[] createMD5Checksum(String filename)
            throws IOException, NoSuchAlgorithmException {
        if (filename == null || filename.trim().isEmpty()) {
            return new byte[0];
        }

        /* create MessageDigest to analyze file checksum */
        MessageDigest complete = MessageDigest.getInstance("MD5");

        /* create FileInputStream to read file */
        try (InputStream fis = new FileInputStream(filename)) {
            /* create buffer */
            byte[] buffer = new byte[16 * 1024];

            int numRead;// line of file

            do {
                /* read file */
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            }
            while (numRead != -1);
        }

        /* return Checksum */
        return complete.digest();
    }


    public static long getCRC32Checksum(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            return 0;
        }

        /* Create new CRC32 Checksum */
        CRC32 gCRC = new CRC32();

        /* Create BufferedInputStream */
        try (InputStream input = new BufferedInputStream(new FileInputStream(filename))) {

            int bytes = 0;

            /* Buffer to load file */
            byte[] buf = new byte[1024 * 64];

            /* Create Checksum */
            while ((bytes = input.read(buf)) > 0) {
                gCRC.update(buf, 0, bytes);
            }
        }

        /* return CRC32 Checksum */
        return gCRC.getValue();
    }

    public static String getSHA256(String password) {
        if (password == null) {
            return null;
        }

        try {
            /*
             * create messagedigest to generate sha-256 hash of passed password
             */
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            /* return hex string of password */
            return getToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
        }

        return null;
    }

    private static String getToHex(byte[] arr) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : arr) {
            buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        /* return hex string of password */
        return buffer.toString();
    }

}
