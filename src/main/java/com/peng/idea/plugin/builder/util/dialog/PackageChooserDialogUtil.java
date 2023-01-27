package com.peng.idea.plugin.builder.util.dialog;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 21:26
 * </pre>
 */
public class PackageChooserDialogUtil {

    public static PackageChooserDialog getPackageChooserDialog(String message, Project project) {
        return createNewInstance(message, project);
    }

    private static PackageChooserDialog createNewInstance(String message, Project project) {
        return new PackageChooserDialog(message, project);
    }
}
