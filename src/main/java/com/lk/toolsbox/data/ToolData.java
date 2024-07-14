package com.lk.toolsbox.data;

public class ToolData {
    private String name;
    private String content;
    private String type;
    static String[] options = {"网址", "可执行程序", "文件夹", "cmd命令"};

    // Constructor, getters, setters, etc.
    public ToolData(String name, String content, String type) {
        this.name = name;
        this.content = content;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String[] getOptions() {
        return options;
    }
}
