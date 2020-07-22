package com.unthinkable.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
public class FileEncodingDecodingService {
    
    private String inputFilePath = "me_One.jpg";
    private String outputFilePath = "me_One_Copy.jpg";
    
    public void fileToBase64StringConversion() throws IOException {

        //load file from /src/test/resources
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader
            .getResource(inputFilePath)
            .getFile());
        
        byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
        String encodedString = Base64
            .getEncoder()
            .encodeToString(fileContent);
        
        //create output file
        File outputFile = new File(inputFile
            .getParentFile()
            .getAbsolutePath() + File.pathSeparator + outputFilePath);

        // decode the string and write to file
        byte[] decodedBytes = Base64
            .getDecoder()
            .decode(encodedString);

        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

        assertTrue(FileUtils.contentEquals(inputFile, outputFile));
    }
}
