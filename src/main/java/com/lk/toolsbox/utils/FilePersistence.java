package com.lk.toolsbox.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.PathManager;
import com.lk.toolsbox.data.ToolsBoxData;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;



public class FilePersistence {
    private static final String FILE_PATH = PathManager.getConfigPath() + "/ToolsBoxPresets.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void saveData(ToolsBoxData data) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(FILE_PATH), "UTF-8")) {
            GSON.toJson(data, writer);
        }
    }

    public static @Nullable ToolsBoxData loadData() throws IOException {
        System.out.println(FILE_PATH);

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(FILE_PATH), "UTF-8")) {
            Type listType = new TypeToken<ToolsBoxData>() {}.getType();
            return GSON.fromJson(reader, listType);
        }
    }
}