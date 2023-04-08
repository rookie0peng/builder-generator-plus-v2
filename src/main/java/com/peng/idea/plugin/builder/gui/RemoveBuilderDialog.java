package com.peng.idea.plugin.builder.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/8
 * </pre>
 */
public class RemoveBuilderDialog extends DialogWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditBuilderTemplateDialog.class);

    private final Project project;

    private final RemoveBuilderDialogDO removeDO;

    private JPanel myMainPanel;

    protected RemoveBuilderDialog(@Nullable Project project, RemoveBuilderDialogDO removeDO) {
        super(project, true);
        this.project = project;
        this.removeDO = removeDO;

    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        initFields();
        return myMainPanel;
    }



    @Override
    public void show() {
        super.init();
        super.show();
    }

    private void initFields() {

    }
}
