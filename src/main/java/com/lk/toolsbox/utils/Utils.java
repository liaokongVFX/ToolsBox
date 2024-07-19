package com.lk.toolsbox.utils;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.*;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.JBTerminalWidget;
import org.jetbrains.plugins.terminal.ShellTerminalWidget;
import org.jetbrains.plugins.terminal.TerminalView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Utils {
    private static final String TERMINAL_KEY = "MY_CUSTOM_TERMINAL";

    public static void openUrl(String url) {
        try {
            // 检查系统是否支持Desktop类
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    // 创建URI对象
                    URI uri = new URI(url);
                    // 使用默认浏览器打开URI
                    desktop.browse(uri);
                }
            } else {
                System.out.println("Desktop功能在当前平台上不受支持。");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void openEXE(String path) {
        try {
            Runtime.getRuntime().exec(path);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void openDir(String folderPath) {
        File folder = new File(folderPath);
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop功能在当前平台上不受支持。");
        }
    }

    private static ShellTerminalWidget findOrCreateTerminal(TerminalView terminalView, Project project) {
        Set<JBTerminalWidget> terminals = terminalView.getWidgets();
        for (JBTerminalWidget widget : terminals) {
            if (widget instanceof ShellTerminalWidget) {
                ShellTerminalWidget terminal = (ShellTerminalWidget) widget;
                if ("My Terminal".equals(terminal.getName())) {
                    return terminal;
                }
            }
        }
        // If no existing terminal named "My Terminal" is found, create a new one
        return terminalView.createLocalShellWidget(project.getBasePath(), "ToolsBox Terminal");
    }

    public static void openCmd(String command, Project project){
        command = command.replace("$ProjectFileDir$", project.getBasePath());
        TerminalView terminalView = TerminalView.getInstance(project);
        ToolWindow terminalToolWindow = ToolWindowManager.getInstance(project).getToolWindow("Terminal");

        if (terminalToolWindow != null) {
            terminalToolWindow.activate(null);

            ShellTerminalWidget terminal = findOrCreateTerminal(terminalView, project);

            if (terminal != null) {
                try {
                    terminal.executeCommand(command);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void _openCmd(String command, Project project) {
        try {
            command = command.replace("$ProjectFileDir$", project.getBasePath());
            System.out.println(command);

            GeneralCommandLine commandLine = new GeneralCommandLine(command);
            commandLine.setWorkDirectory(project.getBasePath());

            // 如果是 Windows 系统，使用 cmd /c 前缀
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                commandLine.setExePath("cmd");
                commandLine.addParameters("/c");
                commandLine.addParameters(command);
            }

            ProcessHandler processHandler = new OSProcessHandler(commandLine);
            ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
            consoleView.attachToProcess(processHandler);

            ExecutionManager executionManager = ExecutionManager.getInstance(project);
            RunContentManager contentManager = executionManager.getContentManager();

            Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            RunContentDescriptor descriptor = new RunContentDescriptor(consoleView, processHandler, consoleView.getComponent(), "Command Output");
            contentManager.showRunContent(executor, descriptor);

            consoleView.print("Executing command: " + String.join(" ", command) + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);

            processHandler.startNotify();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void showNotification(Project project, String message, NotificationType messageType, int timeout){
        Notification notification = new Notification(
                "toolsBoxNotificationGroup",
                "提示", // 通知标题
                message, // 通知内容
                messageType // 通知类型
        );
        Notifications.Bus.notify(notification, project);

        if (timeout > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    notification.expire();
                }
            }, timeout);
        }
    }

    public static void showNotification(Project project, String message, NotificationType messageType) {
        int timeout = 2000;
        if(messageType != NotificationType.INFORMATION){
            timeout = 0;
        }
        showNotification(project, message, messageType, timeout);
    }

    public static void showNotification(Project project, String message) {
        showNotification(project, message, NotificationType.INFORMATION, 2000);
    }
}


