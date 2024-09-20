package by.clevertec.helper;

import by.clevertec.interfaces.WriterJson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriterJsonImpl implements WriterJson {
    public void writerJson(String json, String fileName) throws IOException {
        try {
            Path path = Paths.get("src/main/resources/" + fileName);

            Files.write(path, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
