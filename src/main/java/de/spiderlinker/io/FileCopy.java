package de.spiderlinker.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class FileCopy {

    private FileCopy() {
    }

    public static void fastChannelCopy(final ReadableByteChannel src, final WritableByteChannel dest)
            throws IOException {
        /* create buffer for InputStream */
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            /* prepare the buffer to be drained */
            buffer.flip();

            /* write to the channel, may block */
            dest.write(buffer);

            /*
             * If partial transfer, shift remainder down If buffer is empty,
             * same as doing clear()
             */
            buffer.compact();
        }

        /* EOF will leave buffer in fill state */
        buffer.flip();

        /* make sure the buffer is fully drained. */
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        /* get a channel from the stream */
        try (ReadableByteChannel inputChannel = Channels.newChannel(new FileInputStream(source));
             WritableByteChannel outputChannel = Channels
                     .newChannel(new FileOutputStream(destination))) {
            /* copy the channels */
            FileCopy.fastChannelCopy(inputChannel, outputChannel);
        }
    }
}
