package com.peng.idea.plugin.builder.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.peng.idea.plugin.builder.api.GenerateBuilderDialogDO;
import com.peng.idea.plugin.builder.api.RemoveBuilderDialogDO;
import com.peng.idea.plugin.builder.api.TripleComponentDO;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/5/1 12:28
 * </pre>
 */
public class GenerateBuilderDialogV2 extends DialogWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveBuilderDialogV2.class);

    private final Project project;

    private final GenerateBuilderDialogDO generateDO;

    private JPanel myMainPanel;

    private List<TripleComponentDO> tripleComponents;

    public GenerateBuilderDialogV2(@Nullable Project project, @Nullable GenerateBuilderDialogDO generateDO) {
        super(project, true);
        this.project = project;
        this.generateDO = generateDO;
        setTitle(BuilderConstant.GenerateBuilder.DIALOG_NAME);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return null;

    }
}
