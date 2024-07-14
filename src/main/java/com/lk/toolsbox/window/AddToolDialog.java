package com.lk.toolsbox.window;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ComboBox;
import com.lk.toolsbox.data.ToolData;
import com.lk.toolsbox.data.ToolsBoxData;
import com.lk.toolsbox.utils.Utils;
import org.jetbrains.annotations.NotNull;
import com.lk.toolsbox.utils.FilePersistence;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class AddToolDialog extends DialogWrapper {
    private JPanel contentPanel;
    protected JTextField nameField;
    protected JTextField contentField;
    protected JComboBox<String> comboBox;

    protected AddToolDialog() {
        super(true);
        setTitle("添加工具");
        setSize(300, 200);
        init();
    }

    @NotNull
    @Override
    protected JComponent createCenterPanel() {
        contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField();
        nameField.setToolTipText("请输入名称");

        contentField = new JTextField();
        contentField.setToolTipText("请输入内容");

        comboBox = new ComboBox<>(ToolData.getOptions());

        // 添加 "名称" 标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("名称:"), gbc);

        // 添加 "名称" 文本框
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // 使文本框宽度自适应
        contentPanel.add(nameField, gbc);

        // 重置 weightx
        gbc.weightx = 0;

        // 添加 "内容" 标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(new JLabel("内容:"), gbc);

        // 添加 "内容" 文本框
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // 使文本框宽度自适应
        contentPanel.add(contentField, gbc);

        // 重置 weightx
        gbc.weightx = 0;

        // 添加 "类型" 标签
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(new JLabel("类型:"), gbc);

        // 添加 "类型" 下拉框
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0; // 使下拉框宽度自适应
        contentPanel.add(comboBox, gbc);

        return contentPanel;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();
        JButton button = new JButton("添加工具");

        button.addActionListener(e -> {
            String name = nameField.getText();
            String content = contentField.getText();
            String type = (String) comboBox.getSelectedItem();

            if (name.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(contentPanel,"请输入完整信息");
                return;
            }

            try {
                ToolsBoxData toolsBoxData = FilePersistence.loadData();
                if(toolsBoxData == null){
                    toolsBoxData = new ToolsBoxData("", new LinkedList<ToolData>());
                }

                LinkedList<ToolData> toolDataList = toolsBoxData.getTools();
                for(ToolData data : toolDataList) {
                    if(data.getName().equals(name)) {
                        JOptionPane.showMessageDialog(contentPanel,"工具名称已存在");
                        return;
                    }
                }

                toolsBoxData.addTool(new ToolData(name, content, type));
                FilePersistence.saveData(toolsBoxData);

                Utils.showNotification("工具添加成功");
                close(0);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        panel.add(button);
        return panel;
    }
}
