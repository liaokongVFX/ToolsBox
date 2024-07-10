package com.lk.toolsbox.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.PathManager;
import com.lk.toolsbox.data.ToolData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;



public class FilePersistence {
    private static final String FILE_PATH = PathManager.getConfigPath() + "/myPluginData.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveData(LinkedList<ToolData> data) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(data, writer);
        }
    }

    public static LinkedList<ToolData> loadData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new LinkedList<>();
        }

        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<LinkedList<ToolData>>() {}.getType();
            return GSON.fromJson(reader, listType);
        }
    }
}