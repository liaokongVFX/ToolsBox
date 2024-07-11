package com.lk.toolsbox.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.PathManager;
import com.lk.toolsbox.data.ToolData;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;



public class FilePersistence {
    private static final String FILE_PATH = PathManager.getConfigPath() + "/ToolsBoxPresets.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveData(LinkedList<ToolData> data) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH), "UTF-8")) {
            GSON.toJson(data, writer);
        }
    }

    public static LinkedList<ToolData> loadData() throws IOException {
        System.out.println(FILE_PATH);

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new LinkedList<>();
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(FILE_PATH), "UTF-8")) {
            Type listType = new TypeToken<LinkedList<ToolData>>() {}.getType();
            return GSON.fromJson(reader, listType);
        }
    }
}