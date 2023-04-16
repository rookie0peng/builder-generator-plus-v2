package com.peng.idea.plugin.builder.writter;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;
import com.peng.idea.plugin.builder.psi.BuilderPsiClassBuilder;
import com.peng.idea.plugin.builder.util.psi.GuiHelperUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.peng.idea.plugin.builder.util.CollectionUtil.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:28
 * </pre>
 */
public class BuilderWriterComputable implements Computable<PsiElement> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderWriterComputable.class);

    /**
     * 新建builder的上下文
     */
    private final BuilderContext context;
    /**
     * 已存在的builder
     */
    private final PsiClass existingBuilder;

    private final List<PsiMethod> existingBuildMethods;

    BuilderWriterComputable(BuilderContext context, PsiClass existingBuilder, List<PsiMethod> existingBuildMethods) {
        this.context = context;
        this.existingBuilder = existingBuilder;
        this.existingBuildMethods = existingBuildMethods;
    }

    @Override
    public PsiElement compute() {
        return createBuilder();
    }

    private PsiElement createBuilder() {
        try {
            GuiHelperUtil.includeCurrentPlaceAsChangePlace(context.getProject());
            PsiClass targetClass;
            if (existingBuilder != null) {
                existingBuilder.delete();
            }
            safeStream(existingBuildMethods).filter(Objects::nonNull).forEach(PsiElement::delete);
            if (context.isInner()) {
                targetClass = getInnerBuilderPsiClass();
                context.getPsiClassFromEditor().add(targetClass);
            } else {
                targetClass = getBuilderPsiClass();
                navigateToClassAndPositionCursor(context.getProject(), targetClass);
            }
            return targetClass;
        } catch (IncorrectOperationException e) {
            showErrorMessage(context.getProject(), context.getClassName());
            LOGGER.error("BuilderWriterComputable.createBuilder() exception: ", e);
            return null;
        }
    }

    /**
     * 源类的内部builder类
     * @return
     */
    private PsiClass getInnerBuilderPsiClass() {
        BuilderPsiClassBuilder builder = BuilderPsiClassBuilder._anInnerBuilder(context)
                .withFields()
                .withPrivateConstructor()
                .withInitializingMethod()
                .withSrcClassBuilder()
                .withSetMethods(context.getMethodPrefix());
        addButMethodIfNecessary(builder);
        return builder.build();
    }

    private PsiClass getBuilderPsiClass() {
        BuilderPsiClassBuilder builder = BuilderPsiClassBuilder._aBuilder(context)
                .withFields()
                .withPrivateConstructor()
                .withInitializingMethod()
                .withSrcClassBuilder()
                .withSetMethods(context.getMethodPrefix());
        addButMethodIfNecessary(builder);
        return builder.build();
    }

    private void addButMethodIfNecessary(BuilderPsiClassBuilder builder) {
        if (context.isHasButMethod()) {
            builder.withButMethod();
        }
    }

    private void navigateToClassAndPositionCursor(Project project, PsiClass targetClass) {
        GuiHelperUtil.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
    }

    private void showErrorMessage(Project project, String className) {
        Application application = PsiClassUtil.getApplication();
        application.invokeLater(new BuilderWriterErrorRunnable(project, className));
    }
}
