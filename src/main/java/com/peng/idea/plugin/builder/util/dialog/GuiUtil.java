package com.peng.idea.plugin.builder.util.dialog;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/23
 * </pre>
 */
public class GuiUtil {

    public static JTextField jTextField(String text, boolean editable) {
        JTextField jTextField = new JTextField(text);
        jTextField.setEditable(editable);
        return jTextField;
    }

    public static JCheckBox jCheckBox(boolean isCheck) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(isCheck);
        return checkBox;
    }
}
