package com.client.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ByteConverter {
    private static final Logger logger = LoggerFactory.getLogger(ByteConverter.class);

    public static byte[] convertToBytes(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try {
            FileInputStream inF = new FileInputStream(file);
            int byte1;
            for (int i = 0; (byte1 = inF.read()) > -1; i++) {
                bytes[i] = (byte) byte1;
            }
        } catch (IOException e) {
            logger.error("error while converting to bytes");
        }
        return bytes;
    }

}
