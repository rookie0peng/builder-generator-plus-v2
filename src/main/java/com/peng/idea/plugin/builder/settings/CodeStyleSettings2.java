package com.peng.idea.plugin.builder.settings;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.JavaCodeStyleSettings;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:22
 * </pre>
 */
public class CodeStyleSettings2 {

    public static final CodeStyleSettings2 INSTANCE = new CodeStyleSettings2();

    private final String fieldNamePrefix;

    private final String parameterNamePrefix;

    public CodeStyleSettings2() {
        JavaCodeStyleSettings customSettings = CodeStyleSettings.getDefaults().getCustomSettings(JavaCodeStyleSettings.class);
        this.fieldNamePrefix = customSettings.FIELD_NAME_PREFIX;
        this.parameterNamePrefix = customSettings.PARAMETER_NAME_PREFIX;
    }

    public String getFieldNamePrefix() {
        return fieldNamePrefix;
    }

    public String getParameterNamePrefix() {
        return parameterNamePrefix;
    }
}
