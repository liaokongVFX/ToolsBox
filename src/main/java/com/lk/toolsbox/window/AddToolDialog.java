package com.lk.toolsbox.window;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
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

        String[] options = {"网址", "可执行程序", "文件夹", "cmd命令"};
        comboBox = new ComboBox<>(options);

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
                LinkedList<ToolData> toolDataList = FilePersistence.loadData();
                for(ToolData data : toolDataList) {
                    if(data.getName().equals(name)) {
                        JOptionPane.showMessageDialog(contentPanel,"工具名称已存在");
                        return;
                    }
                }

                toolDataList.add(new ToolData(name, content, type));
                FilePersistence.saveData(toolDataList);

                // 创建一个通知
                Project project = ProjectManager.getInstance().getOpenProjects()[0];
                Notification notification = new Notification(
                        "toolsbox_notification_group",
                        "提示", // 通知标题
                        "工具添加成功！", // 通知内容
                        NotificationType.INFORMATION // 通知类型
                );
                Notifications.Bus.notify(notification, project);


                close(0);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        panel.add(button);
        return panel;
    }
}
