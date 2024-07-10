package com.lk.toolsbox.window;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.lk.toolsbox.data.ToolData;
import org.jetbrains.annotations.NotNull;
import com.lk.toolsbox.utils.FilePersistence;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class AddToolDialog extends DialogWrapper {
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
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField();
        nameField.setToolTipText("请输入名称");

        contentField = new JTextField();
        contentField.setToolTipText("请输入内容");

        String[] options = {"网址", "可执行程序", "文件夹", "cmd命令"};
        comboBox = new ComboBox<>(options);

        // 添加 "名称" 标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("名称:"), gbc);

        // 添加 "名称" 文本框
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // 使文本框宽度自适应
        panel.add(nameField, gbc);

        // 重置 weightx
        gbc.weightx = 0;

        // 添加 "内容" 标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("内容:"), gbc);

        // 添加 "内容" 文本框
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0; // 使文本框宽度自适应
        panel.add(contentField, gbc);

        // 重置 weightx
        gbc.weightx = 0;

        // 添加 "类型" 标签
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("类型:"), gbc);

        // 添加 "类型" 下拉框
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0; // 使下拉框宽度自适应
        panel.add(comboBox, gbc);

        return panel;
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
                MessageDialogBuilder.yesNo("提示", "请输入完整信息").show();
                return;
            }

            try {
                LinkedList<ToolData> toolDataList = FilePersistence.loadData();
                toolDataList.add(new ToolData(name, content, type));
                FilePersistence.saveData(toolDataList);
                // 使用action 提醒用户添加成功

                close(0);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        panel.add(button);
        return panel;
    }
}
