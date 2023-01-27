package com.peng.idea.plugin.builder.writter;

import com.intellij.openapi.application.Application;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;

import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:28
 * </pre>
 */
public class BuilderWriterRunnable implements Runnable {

    private final BuilderContext context;
    private final PsiClass existingBuilder;
    private final List<PsiMethod> existingBuildMethods;

    public BuilderWriterRunnable(BuilderContext context, PsiClass existingBuilder, List<PsiMethod> existingBuildMethods) {
        this.context = context;
        this.existingBuilder = existingBuilder;
        this.existingBuildMethods = existingBuildMethods;
    }

    @Override
    public void run() {
        Application application = PsiClassUtil.getApplication();
        application.runWriteAction(new BuilderWriterComputable(context, existingBuilder, existingBuildMethods));
    }
}
