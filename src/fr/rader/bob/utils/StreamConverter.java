package fr.rader.bob.utils;

import java.io.*;

public class StreamConverter {

    public static InputStream toInputStream(OutputStream outputStream) {
        PipedInputStream inputStream = new PipedInputStream();

        try {
            PipedOutputStream pipedOutputStream = new PipedOutputStream(inputStream);

            if(outputStream instanceof ByteArrayOutputStream) {
                ((ByteArrayOutputStream) outputStream).writeTo(pipedOutputStream);

                pipedOutputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;
    }
}
