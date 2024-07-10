package com.lk.toolsbox.utils;

import com.intellij.openapi.application.PathManager;

import java.awt.*;
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
}


