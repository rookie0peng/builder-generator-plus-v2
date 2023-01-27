package com.peng.idea.plugin.builder.util.psi;

import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import javax.swing.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/23 2:54
 * </pre>
 */
public class GuiHelperUtil {

    public static void showMessageDialog(Project project, String message, String title, Icon icon) {
        Messages.showMessageDialog(project, message, title, icon);
    }

    public static void includeCurrentPlaceAsChangePlace(Project project) {
        IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
    }

    public static void positionCursor(Project project, PsiFile psiFile, PsiElement psiElement) {
        CodeInsightUtil.positionCursor(project, psiFile, psiElement);
    }
}
