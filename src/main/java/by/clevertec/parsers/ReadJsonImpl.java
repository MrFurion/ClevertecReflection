package by.clevertec.parsers;

import by.clevertec.interfaces.ReadJson;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadJsonImpl implements ReadJson {
    @SneakyThrows
    @Override
    public String readJson() {
        InputStream inputStream = ReadJsonImpl.class.getResourceAsStream("/customJson.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Файл не найден");
        }

        StringBuilder json = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = br.readLine()) != null) {
                json.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("exception reading file");
        }
        return json.toString();
    }
}
