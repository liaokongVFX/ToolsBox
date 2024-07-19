package com.lk.toolsbox.window;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.lk.toolsbox.data.ToolData;
import com.lk.toolsbox.data.ToolsBoxData;
import com.lk.toolsbox.utils.FilePersistence;
import com.lk.toolsbox.utils.Utils;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class EditToolsDialog extends DialogWrapper {
    private JPanel contentPanel;
    protected static JBTable infoTable;
    private static DefaultTableModel defaultTableModel;

    protected EditToolsDialog() {
        super(true);
        setTitle("编辑工具");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        infoTable = new JBTable();
        defaultTableModel = new DefaultTableModel(null, new String[]{"名称", "内容", "类型", "操作"});
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
        private int currentRow;

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
                    if (currentRow > 0) {
                        defaultTableModel.moveRow(currentRow, currentRow, currentRow - 1);
                        infoTable.setRowSelectionInterval(currentRow - 1, currentRow - 1);
                    } else {
                        defaultTableModel.moveRow(currentRow, currentRow, defaultTableModel.getRowCount() - 1);
                        infoTable.setRowSelectionInterval(defaultTableModel.getRowCount() - 1, defaultTableModel.getRowCount() - 1);
                    }
                }
            });

            downButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    if (currentRow < defaultTableModel.getRowCount() - 1) {
                        defaultTableModel.moveRow(currentRow, currentRow, currentRow + 1);
                        infoTable.setRowSelectionInterval(currentRow + 1, currentRow + 1);
                    } else {
                        defaultTableModel.moveRow(currentRow, currentRow, 0);
                        infoTable.setRowSelectionInterval(0, 0);
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    defaultTableModel.removeRow(currentRow);
                    if (infoTable.isEditing()) {
                        infoTable.getCellEditor().stopCellEditing();
                    }
                }
            });

            panel.add(upButton);
            panel.add(downButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }


    public LinkedList<ToolData> getTableData() {
        int rowCount = defaultTableModel.getRowCount();
        LinkedList<ToolData> toolDataLinkedList = new LinkedList<>();
        Set<String> nameSet = new HashSet<>();

        for (int i = 0; i < rowCount; i++) {
            String name = (String) defaultTableModel.getValueAt(i, 0);

            if(!nameSet.add(name)){
                JOptionPane.showMessageDialog(contentPanel,"工具名称出现重复，请先修改");
                return null;
            }

            toolDataLinkedList.add(new ToolData(
                (String) defaultTableModel.getValueAt(i, 0),
                (String) defaultTableModel.getValueAt(i, 1),
                (String) defaultTableModel.getValueAt(i, 2)
            ));
            nameSet.add(name);
        }
        return toolDataLinkedList;
    }

    protected void doOKAction() {
        LinkedList<ToolData> data = getTableData();
        if(data == null){
            return;
        }

        try {
            ToolsBoxData toolsBoxData = FilePersistence.loadData();
            if (toolsBoxData == null) {
                toolsBoxData = new ToolsBoxData("", new LinkedList<ToolData>());
            }

            toolsBoxData.setTools(data);
            FilePersistence.saveData(toolsBoxData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        super.doOKAction();
    }
}
