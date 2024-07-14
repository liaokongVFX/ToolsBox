package com.lk.toolsbox.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.lk.toolsbox.data.ToolData;
import com.lk.toolsbox.data.ToolsBoxData;
import com.lk.toolsbox.utils.FilePersistence;
import com.lk.toolsbox.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(3, 2, 3, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        try {
            ToolsBoxData toolsBoxData = FilePersistence.loadData();
            if (toolsBoxData == null){
                return;
            }
            LinkedList<ToolData> toolDataList = toolsBoxData.getTools();

            for (int i = 0; i < toolDataList.size(); i++) {
                ToolData toolData = toolDataList.get(i);
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
                        } else if (toolData.getType().equals("cmd命令")) {
                            Utils.openCmd(toolData.getContent());
                        }
                    }
                });

                gbc.gridx = i % 5; // 列索引
                gbc.gridy = i / 5; // 行索引
                buttonPanel.add(button, gbc);
            }

            noteArea.setText(toolsBoxData.getNote());

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
                Utils.showNotification("工具成功！");
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditToolsDialog editToolDialog = new EditToolsDialog();
                if(editToolDialog.showAndGet()){
                    loadTools();
                }
            }
        });
        noteArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String note = noteArea.getText();
                try {
                    ToolsBoxData toolsBoxData = FilePersistence.loadData();
                    if (toolsBoxData == null){
                        toolsBoxData = new ToolsBoxData("", new LinkedList<>());
                    }
                    toolsBoxData.setNote(note);
                    FilePersistence.saveData(toolsBoxData);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public JPanel getToolsBoxPanel() {
        return ToolsBoxPanel;
    }
}
