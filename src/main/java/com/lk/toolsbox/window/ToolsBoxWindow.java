package com.lk.toolsbox.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.lk.toolsbox.data.ToolData;
import com.lk.toolsbox.utils.FilePersistence;
import com.lk.toolsbox.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

public class ToolsBoxWindow {
    private JTextArea noteArea;
    private JPanel ToolsBoxPanel;
    private JPanel buttonPanel;
    private JPanel notePanel;
    private JButton addButton;
    private JButton editButton;
    private JButton refreshButton;

    private void init(){
        loadTools();
    }

    private void loadTools(){
        buttonPanel.removeAll();
        buttonPanel.revalidate();
        buttonPanel.repaint();

        try {
            LinkedList<ToolData> toolDataList = FilePersistence.loadData();

            for (ToolData toolData : toolDataList){
                JButton button = new JButton(toolData.getName());
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (toolData.getType().equals("网址")) {
                            Utils.openUrl(toolData.getContent());
                        } else if (toolData.getType().equals("可执行程序")) {
                            Utils.openEXE(toolData.getContent());
                        } else if (toolData.getType().equals("文件夹")) {
                            Utils.openDir(toolData.getContent());
                        }
                    }
                });
                buttonPanel.add(button);
            }

        } catch (IOException e) {
            System.out.println("加载数据失败");
        }
    }

    public ToolsBoxWindow(Project project, ToolWindow toolWindow) {
        init();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddToolDialog addToolDialog = new AddToolDialog();
                if(addToolDialog.showAndGet()){
                    loadTools();
                }
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTools();
            }
        });
    }

    public JPanel getToolsBoxPanel() {
        return ToolsBoxPanel;
    }
}
