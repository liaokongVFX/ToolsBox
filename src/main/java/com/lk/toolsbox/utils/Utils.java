package com.lk.toolsbox.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Utils {
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
}


