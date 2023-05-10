package com.peng.idea.plugin.builder.psi;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.peng.idea.plugin.builder.settings.CodeStyleSettings2;
import com.peng.idea.plugin.builder.util.psi.BuilderGenerateUtil;
import com.peng.idea.plugin.builder.util.psi.PsiClassUtil;
import com.peng.idea.plugin.builder.util.verifier.PsiFieldVerifierUtil;
import com.peng.idea.plugin.builder.writter.BuilderContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.intellij.openapi.util.text.StringUtil.isVowel;
import static java.util.Objects.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:29
 * </pre>
 */
public class BuilderPsiClassBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderPsiClassBuilder.class);

    private static final String PRIVATE_STRING = "private";
    private static final String SPACE = " ";
    private static final String A_PREFIX = " a";
    private static final String AN_PREFIX = " an";
    private static final String SEMICOLON = ",";
    private PsiFieldsModifier psiFieldsModifier = new PsiFieldsModifier();
    private ButMethodCreator butMethodCreator;
    private MethodCreator methodCreator;

    private PsiClass srcClass = null;
    private String builderClassName = null;

    private List<PsiField> psiFieldsForSetters = null;
    private List<PsiField> psiFieldsForConstructor = null;
    private List<PsiField> allSelectedPsiFields = null;
    private PsiMethod bestConstructor = null;

    private PsiClass builderClass = null;
    private PsiElementFactory elementFactory = null;
    private String srcClassName = null;
    private String srcClassFieldName = null;
    private String builderMethodName = null;

    private boolean srcClassBuilder = true;
    private boolean useSingleField = false;
    private boolean isInline = false;

    public static BuilderPsiClassBuilder _aBuilder(BuilderContext context) {
        return new BuilderPsiClassBuilder().aBuilder(context);
    }

    public static BuilderPsiClassBuilder _anInnerBuilder(BuilderContext context) {
        return new BuilderPsiClassBuilder().anInnerBuilder(context);
    }

    public BuilderPsiClassBuilder aBuilder(BuilderContext context) {
        initializeFields(context);
        JavaDirectoryService javaDirectoryService = PsiClassUtil.getJavaDirectoryService();
        builderClass = javaDirectoryService.createClass(context.getTargetDirectory(), builderClassName);
        appendImportBuilderClass(context.getPsiClassFromEditor(), builderClass);
        PsiModifierList modifierList = builderClass.getModifierList();
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
        return this;
    }



    public BuilderPsiClassBuilder anInnerBuilder(BuilderContext context) {
        initializeFields(context);
        builderClass = elementFactory.createClass(builderClassName);
        PsiModifierList modifierList = builderClass.getModifierList();
        modifierList.setModifierProperty(PsiModifier.FINAL, true);
        modifierList.setModifierProperty(PsiModifier.STATIC, true);
        return this;
    }

    private void initializeFields(BuilderContext context) {
        JavaPsiFacade javaPsiFacade = PsiClassUtil.getJavaPsiFacade(context.getProject());
        elementFactory = javaPsiFacade.getElementFactory();
        srcClass = context.getPsiClassFromEditor();
        builderClassName = context.getClassName();
        srcClassName = context.getPsiClassFromEditor().getName();
        srcClassFieldName = StringUtils.uncapitalize(srcClassName);
        builderMethodName = context.getBuilderMethodName();
        psiFieldsForSetters = context.getPsiFieldsForBuilder().getFieldsForSetters();
        psiFieldsForConstructor = context.getPsiFieldsForBuilder().getFieldsForConstructor();
        allSelectedPsiFields = context.getPsiFieldsForBuilder().getAllSelectedFields();
        srcClassBuilder = context.isSrcClassBuilder();
        useSingleField = context.isUseSingleField();
        bestConstructor = context.getPsiFieldsForBuilder().getBestConstructor();
        methodCreator = new MethodCreator(elementFactory, builderClassName);
        butMethodCreator = new ButMethodCreator(elementFactory);
        isInline = allSelectedPsiFields.size() == psiFieldsForConstructor.size();
    }

    public BuilderPsiClassBuilder withFields() {
        if (useSingleField) {
            String fieldText = "private " + srcClassName + " " + srcClassFieldName + ";";
            PsiField singleField = elementFactory.createFieldFromText(fieldText, srcClass);
            builderClass.add(singleField);
        } else if (isInnerBuilder(builderClass)) {
            psiFieldsModifier.modifyFieldsForInnerClass(allSelectedPsiFields, builderClass);
        } else {
            psiFieldsModifier.modifyFields(psiFieldsForSetters, psiFieldsForConstructor, builderClass);
        }
        return this;
    }

    /**
     * 设置私有构造器
     * @return
     */
    public BuilderPsiClassBuilder withPrivateConstructor() {
        PsiMethod constructor;
        if (useSingleField) {
            constructor = elementFactory.createMethodFromText(builderClassName + "() { " + srcClassFieldName + " = new " + srcClassName + "(); }", srcClass);
        } else {
            constructor = elementFactory.createConstructor();
        }
        constructor.getModifierList().setModifierProperty(PRIVATE_STRING, true);
        builderClass.add(constructor);
        return this;
    }

    /**
     * 设置初始化方法
     * @return
     */
    public BuilderPsiClassBuilder withInitializingMethod() {
//        String builderMethodName = BuilderGenerateUtil.builderMethodName(srcClassName);
        PsiMethod staticMethod = elementFactory.createMethodFromText(
                "public static " + builderClassName + " " + builderMethodName + "() { return new " + builderClassName + "(); }", srcClass);
        builderClass.add(staticMethod);
        return this;
    }

    /**
     * 在源类设置静态初始化方法
     * @return
     */
    public BuilderPsiClassBuilder withSrcClassBuilder() {
        if (srcClassBuilder) {
            //        String prefix = isVowel(srcClassName.toLowerCase(Locale.ENGLISH).charAt(0)) ? AN_PREFIX : A_PREFIX;
            PsiMethod staticSrcMethod = elementFactory.createMethodFromText(
                    "public static " + builderClassName + " builder() { return " + builderClassName + "." + builderMethodName + "(); }", srcClass);
            srcClass.add(staticSrcMethod);
        }
        return this;
    }

    public BuilderPsiClassBuilder withSetMethods(String methodPrefix) {
        if (useSingleField || isInnerBuilder(builderClass)) {
            for (PsiField psiFieldForAssignment : allSelectedPsiFields) {
                createAndAddMethod(psiFieldForAssignment, methodPrefix);
            }
        } else {
            for (PsiField psiFieldForSetter : psiFieldsForSetters) {
                createAndAddMethod(psiFieldForSetter, methodPrefix);
            }
            for (PsiField psiFieldForConstructor : psiFieldsForConstructor) {
                createAndAddMethod(psiFieldForConstructor, methodPrefix);
            }
        }
        return this;
    }

    private boolean isInnerBuilder(PsiClass aClass) {
        return aClass.hasModifierProperty(PsiModifier.STATIC);
    }

    public BuilderPsiClassBuilder withButMethod() {
        PsiMethod method = butMethodCreator.butMethod(builderClassName, builderClass, srcClass, srcClassFieldName, useSingleField);
        builderClass.add(method);
        return this;
    }

    private void createAndAddMethod(PsiField psiField, String methodPrefix) {
        builderClass.add(methodCreator.createMethod(psiField, methodPrefix, srcClassFieldName, useSingleField));
    }

    public PsiClass build() {
        if (useSingleField) {
            return buildUseSingleField();
        } else if (isInline) {
            return buildIsInline();
        } else {
            return buildDefault();
        }
    }

    private PsiClass buildUseSingleField() {
        String buildMethodText = "public " + srcClassName + " build() { "
                + "return " + srcClassFieldName + ";"
                + " }";
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText, srcClass);
        builderClass.add(buildMethod);
        return builderClass;
    }

    private PsiClass buildIsInline() {
        StringBuilder buildMethodText = new StringBuilder();
        buildMethodText.append("public ").append(srcClassName).append(" build() { ");
        buildMethodText.append("return ");
        appendConstructor(buildMethodText);
        buildMethodText.append(" }");
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText.toString(), srcClass);
        builderClass.add(buildMethod);
        return builderClass;
    }

    private PsiClass buildDefault() {
        StringBuilder buildMethodText = new StringBuilder();
        buildMethodText.append("public ").append(srcClassName).append(" build() { ");
        buildMethodText.append(srcClassName).append(SPACE).append(srcClassFieldName).append(" = ");
        appendConstructor(buildMethodText);
        appendSetMethodsOrAssignments(buildMethodText);
        buildMethodText.append("return ").append(srcClassFieldName).append(";");
        buildMethodText.append(" }");
        PsiMethod buildMethod = elementFactory.createMethodFromText(buildMethodText.toString(), srcClass);
        builderClass.add(buildMethod);
        return builderClass;
    }

    private void appendConstructor(StringBuilder buildMethodText) {
        String constructorParameters = createConstructorParameters();
        buildMethodText.append("new ").append(srcClassName).append("(").append(constructorParameters).append(");");
    }

    private void appendSetMethodsOrAssignments(StringBuilder buildMethodText) {
        appendSetMethods(buildMethodText, psiFieldsForSetters);
        if (isInnerBuilder(builderClass)) {
            Set<PsiField> fieldsSetViaAssignment = new HashSet<PsiField>(allSelectedPsiFields);
            fieldsSetViaAssignment.removeAll(psiFieldsForSetters);
            fieldsSetViaAssignment.removeAll(psiFieldsForConstructor);
            appendAssignments(buildMethodText, fieldsSetViaAssignment);
        }
    }

    private void appendSetMethods(StringBuilder buildMethodText, Collection<PsiField> fieldsToBeSetViaSetter) {
        for (PsiField psiFieldsForSetter : fieldsToBeSetViaSetter) {
            String fieldNamePrefix = CodeStyleSettings2.INSTANCE.getFieldNamePrefix();
            String fieldName = psiFieldsForSetter.getName();
            String fieldNameWithoutPrefix = fieldName.replaceFirst(fieldNamePrefix, "");
            String fieldNameUppercase = StringUtils.capitalize(fieldNameWithoutPrefix);
            buildMethodText.append(srcClassFieldName).append(".set").append(fieldNameUppercase).append("(").append(fieldName).append(");");
        }
    }

    private void appendAssignments(StringBuilder buildMethodText, Collection<PsiField> fieldsSetViaAssignment) {
        for (PsiField field : fieldsSetViaAssignment) {
            buildMethodText.append(srcClassFieldName).append(".")
                    .append(field.getName()).append("=").append("this.")
                    .append(field.getName()).append(";");
        }
    }

    private void appendImportBuilderClass(PsiClass srcClass, PsiClass builderClass) {
        LOGGER.info("appendImportBuilderClass start");
        String srcQualifiedName = srcClass.getQualifiedName();
        String builderQualifiedName = builderClass.getQualifiedName();
        if (isNull(srcQualifiedName) || isNull(builderQualifiedName))
            return;
        String srcPath = srcQualifiedName.substring(0, srcQualifiedName.lastIndexOf('.'));
        String builderPath = builderQualifiedName.substring(0, builderQualifiedName.lastIndexOf('.'));
        // 同一个包 或者 内部类
        if (Objects.equals(srcPath, builderPath) || builderQualifiedName.contains(srcQualifiedName + ".")) {
            // 不需要添加 import
            return;
        } else {
            // import builder 类
            PsiFile containingFile = srcClass.getContainingFile();
            if (containingFile instanceof PsiJavaFile) {
                PsiJavaFile psiJavaFile = (PsiJavaFile) containingFile;
                psiJavaFile.importClass(builderClass);
            }
        }
        LOGGER.info("appendImportBuilderClass end");
    }

    private String createConstructorParameters() {
        if (bestConstructor == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (PsiParameter psiParameter : bestConstructor.getParameterList().getParameters()) {
            boolean parameterHasMatchingField = false;
            for (PsiField psiField : psiFieldsForConstructor) {
                if (PsiFieldVerifierUtil.areNameAndTypeEqual(psiField, psiParameter)) {
                    sb.append(psiField.getName()).append(SEMICOLON);
                    parameterHasMatchingField = true;
                    break;
                }
            }
            if (!parameterHasMatchingField) {
                sb.append(getDefaultValue(psiParameter.getType())).append(SEMICOLON);
            }
        }
        removeLastSemicolon(sb);
        return sb.toString();
    }

    private String getDefaultValue(PsiType type) {
        if (type.equals(PsiTypes.booleanType())) {
            return "false";
        } else if (type.equals(PsiTypes.byteType()) || type.equals(PsiTypes.shortType()) || type.equals(PsiTypes.intType())) {
            return "0";
        } else if (type.equals(PsiTypes.longType())) {
            return "0L";
        } else if (type.equals(PsiTypes.floatType())) {
            return "0.0f";
        } else if (type.equals(PsiTypes.doubleType())) {
            return "0.0d";
        } else if (type.equals(PsiTypes.charType())) {
            return "'\\u0000'";
        }
        return "null";
    }

    private void removeLastSemicolon(StringBuilder sb) {
        if (sb.toString().endsWith(SEMICOLON)) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}

