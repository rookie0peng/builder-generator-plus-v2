package com.peng.idea.plugin.builder.util;

import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/1 21:30
 * </pre>
 */
public class PanelUtil {

    private final JPanel myPanel;

    private int line = 0;

    public PanelUtil() {
        myPanel = new JPanel(new GridBagLayout());
    }

    public static PanelUtil builder() {
        return new PanelUtil();
    }

    public JPanel getPanel() {
        return myPanel;
    }

    public void addButton(JButton jButton) {
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 1;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 0;
        gbConstraints.gridwidth = 1;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.anchor = GridBagConstraints.EAST;
        myPanel.add(jButton, gbConstraints);
        line++;
    }

    public void addLabelComponent(JLabel jLabel, JComponent jComponent) {
        GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 0;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 0;
        gbConstraints.gridwidth = 1;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.WEST;

        myPanel.add(jLabel, gbConstraints);

        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 1;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 0;
        gbConstraints.gridwidth = 1;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.WEST;

        myPanel.add(jComponent, gbConstraints);
        line++;
    }

    public void addComponentFillVertically(JComponent jComponent) {
        GridBagConstraints gbConstraints = new GridBagConstraints();

        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 0;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        gbConstraints.gridwidth = 2;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.WEST;

        myPanel.add(jComponent, gbConstraints);
        line++;
    }

    public void addTripleComponent(JComponent jComponent, JComponent jComponent2, JComponent jComponent3) {
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.ipadx = 0;
        gbConstraints.ipady = 0;


        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 0;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 0.5;
        gbConstraints.gridwidth = 1;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.WEST;

        myPanel.add(jComponent, gbConstraints);

        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 1;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 0.5;
        gbConstraints.gridwidth = 3;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.WEST;

        myPanel.add(jComponent2, gbConstraints);

        gbConstraints.insets = JBUI.insets(4, 8);
        gbConstraints.gridx = 4;
        gbConstraints.gridy = line;
        gbConstraints.weightx = 0.5;
        gbConstraints.gridwidth = 1;
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.anchor = GridBagConstraints.WEST;

        myPanel.add(jComponent3, gbConstraints);

        line++;
    }
}
