package com.lk.toolsbox.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ToolsBoxFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToolsBoxWindow toolsBoxWindow = new ToolsBoxWindow(project, toolWindow);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(toolsBoxWindow.getToolsBoxPanel(), null, false);
        toolWindow.getContentManager().addContent(content);
    }
}
