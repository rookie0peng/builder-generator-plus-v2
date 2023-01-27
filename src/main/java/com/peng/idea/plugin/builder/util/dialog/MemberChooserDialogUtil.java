package com.peng.idea.plugin.builder.util.dialog;

import com.intellij.codeInsight.generation.PsiElementClassMember;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.project.Project;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/20 15:08
 * </pre>
 */
public class MemberChooserDialogUtil {

    static final String TITLE = "Select Fields to Be Available in Builder";

    public static MemberChooser<PsiElementClassMember> getMemberChooserDialog(List<PsiElementClassMember> elements, Project project) {
        PsiElementClassMember[] psiElementClassMembers = elements.toArray(new PsiElementClassMember[elements.size()]);
        MemberChooser<PsiElementClassMember> memberChooserDialog = createNewInstance(project, psiElementClassMembers);
        memberChooserDialog.setCopyJavadocVisible(false);
        memberChooserDialog.selectElements(psiElementClassMembers);
        memberChooserDialog.setTitle(TITLE);
        return memberChooserDialog;
    }

    private static MemberChooser<PsiElementClassMember> createNewInstance(Project project, PsiElementClassMember[] psiElementClassMembers) {
        return new MemberChooser<>(psiElementClassMembers, false, true, project, false);
    }
}
