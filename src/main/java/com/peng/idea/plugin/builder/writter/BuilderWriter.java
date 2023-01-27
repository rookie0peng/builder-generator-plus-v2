package com.peng.idea.plugin.builder.writter;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:27
 * </pre>
 */
public class BuilderWriter {

    static final String CREATE_BUILDER_STRING = "Create Builder";

    public static void writeBuilder(BuilderContext context, PsiClass existingBuilder, List<PsiMethod> existingBuildMethods) {
        CommandProcessor commandProcessor = PsiClassUtil.getCommandProcessor();
        commandProcessor.executeCommand(context.getProject(), new BuilderWriterRunnable(context, existingBuilder, existingBuildMethods), CREATE_BUILDER_STRING, null);
    }
}
