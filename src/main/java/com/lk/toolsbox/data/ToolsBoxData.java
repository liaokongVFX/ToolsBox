package com.lk.toolsbox.data;

import java.util.LinkedList;

public class ToolsBoxData {
    private String note;
    private LinkedList<ToolData> tools;

    public ToolsBoxData(String note, LinkedList<ToolData> tools) {
        this.note = note;
        this.tools = tools;
    }

    public String getNote() {
        return note;
    }

    public LinkedList<ToolData> getTools() {
        return tools;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTools(LinkedList<ToolData> tools) {
        this.tools = tools;
    }
    public void addTool(ToolData toolData){
        tools.add(toolData);
    }
}
