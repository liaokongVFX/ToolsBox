package com.lk.toolsbox.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.lk.toolsbox.utils.Utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolsBoxWindow {
    private JButton googleButton;
    private JTextArea noteArea;
    private JPanel ToolsBoxPanel;
    private JPanel buttonPanel;
    private JPanel notePanel;
    private JButton addButton;
    private JButton editButton;

    private void init(){

    }

    public ToolsBoxWindow(Project project, ToolWindow toolWindow) {
        init();

        googleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.openUrl("https://www.google.com");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddToolDialog addToolDialog = new AddToolDialog();
                addToolDialog.show();
            }
        });
    }

    public JPanel getToolsBoxPanel() {
        return ToolsBoxPanel;
    }
}
