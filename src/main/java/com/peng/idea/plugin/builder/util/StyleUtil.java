package com.peng.idea.plugin.builder.util;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/3 20:21
 * </pre>
 */
public class StyleUtil {

    private static final int WIDTH = 40;

    public static void setPreferredSize(JTextField field) {
        if (field != null) {
            Dimension size = field.getPreferredSize();
            FontMetrics fontMetrics = field.getFontMetrics(field.getFont());
            size.width = fontMetrics.charWidth('a') * WIDTH;
            field.setPreferredSize(size);
        }
    }
}
