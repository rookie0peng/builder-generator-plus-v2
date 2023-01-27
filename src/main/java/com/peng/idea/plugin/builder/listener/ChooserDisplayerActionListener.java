package com.peng.idea.plugin.builder.listener;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.java.JavaBundle;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.peng.idea.plugin.builder.util.dialog.PackageChooserDialogUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:11
 * </pre>
 */
public class ChooserDisplayerActionListener implements ActionListener {

    private final ReferenceEditorComboWithBrowseButton comboWithBrowseButton;

    private final Project project;

    public ChooserDisplayerActionListener(ReferenceEditorComboWithBrowseButton comboWithBrowseButton, Project project) {
        this.comboWithBrowseButton = comboWithBrowseButton;
        this.project = project;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PackageChooserDialog chooser =
                PackageChooserDialogUtil.getPackageChooserDialog(JavaBundle.message("dialog.create.class.package.chooser.title"), project);
        chooser.selectPackage(comboWithBrowseButton.getText());
        chooser.show();
        PsiPackage aPackage = chooser.getSelectedPackage();
        if (aPackage != null) {
            comboWithBrowseButton.setText(aPackage.getQualifiedName());
        }
    }
}

