package com.lk.toolsbox.window;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.lk.toolsbox.data.ToolData;
import com.lk.toolsbox.data.ToolsBoxData;
import com.lk.toolsbox.utils.FilePersistence;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

public class EditToolsDialog extends DialogWrapper {
    private JPanel contentPanel;
    protected JBTable infoTable;

    protected EditToolsDialog() {
        super(true);
        setTitle("编辑工具");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        infoTable = new JBTable();
        DefaultTableModel defaultTableModel = new DefaultTableModel(null, new String[]{"名称", "内容", "类型", "操作"});
        infoTable.setModel(defaultTableModel);
        infoTable.setEnabled(true);
        setTableColumnWidths(new int[]{100, 500, 80, 120});
        infoTable.setRowHeight(32);

        try {
            ToolsBoxData toolsBoxData = FilePersistence.loadData();
            if (toolsBoxData == null) {
                toolsBoxData = new ToolsBoxData("", new LinkedList<ToolData>());
            }

            LinkedList<ToolData> toolDataList = toolsBoxData.getTools();
            for (ToolData toolData : toolDataList) {
                setComboBoxInTableColumn(ToolData.getOptions(), toolData.getType());
                defaultTableModel.addRow(new Object[]{toolData.getName(), toolData.getContent(), toolData.getType()});
                setButtonsInTableColumn(3);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 将 JTable 放置在 JScrollPane 中
        JScrollPane scrollPane = new JBScrollPane(infoTable);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        return contentPanel;
    }

    private void setTableColumnWidths(int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            TableColumn column = infoTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }
    }

    private void setComboBoxInTableColumn(String[] items, String defaultItem) {
        TableColumn column = infoTable.getColumnModel().getColumn(2);
        ComboBox<String> comboBox = new ComboBox<>(items);
        comboBox.setSelectedItem(defaultItem);
        column.setCellEditor(new DefaultCellEditor(comboBox));
    }

    // 将某一列设置为包含多个按钮
    private void setButtonsInTableColumn(int columnIndex) {
        TableColumn column = infoTable.getColumnModel().getColumn(columnIndex);
        column.setCellRenderer((TableCellRenderer) new ButtonRenderer());
        column.setCellEditor((TableCellEditor) new ButtonEditor(new JCheckBox()));
    }

    // 自定义渲染器
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton upButton;
        private JButton downButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
            upButton = new JButton(AllIcons.Actions.MoveUp);
            downButton = new JButton(AllIcons.Actions.MoveDown);
            deleteButton = new JButton(AllIcons.Actions.DeleteTagHover);

            upButton.setPreferredSize(new Dimension(30, 30));
            downButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.setPreferredSize(new Dimension(30, 30));

            add(upButton);
            add(downButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // 自定义编辑器
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton upButton;
        private JButton downButton;
        private JButton deleteButton;

        public ButtonEditor(JCheckBox checkBox) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
            upButton = new JButton(AllIcons.Actions.MoveUp);
            downButton = new JButton(AllIcons.Actions.MoveDown);
            deleteButton = new JButton(AllIcons.Actions.DeleteTagHover);

            upButton.setPreferredSize(new Dimension(30, 30));
            downButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.setPreferredSize(new Dimension(30, 30));

            upButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    System.out.println("up按钮1点击");
                }
            });

            downButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    System.out.println("down按钮2点击");
                }
            });

            panel.add(upButton);
            panel.add(downButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

}
