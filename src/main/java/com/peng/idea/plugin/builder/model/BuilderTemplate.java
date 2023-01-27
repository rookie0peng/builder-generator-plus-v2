package com.peng.idea.plugin.builder.model;

import com.intellij.util.xmlb.annotations.Tag;

import java.util.Objects;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:07
 * </pre>
 */
@Tag("BuilderTemplate")
public class BuilderTemplate implements Entity {

    private String id;

    private String templateName;

    private String className;

    private String methodPrefix;

    private Boolean innerBuilder;

    // ******************** 分割线 ********************

    private Boolean butMethod;

    private Boolean useSingleField;

    public BuilderTemplate() {

    }

    public BuilderTemplate(BuilderTemplate builderTemplate) {
        if (builderTemplate != null) {
            this.id = builderTemplate.id;
            this.templateName = builderTemplate.templateName;
            this.className = builderTemplate.className;
            this.methodPrefix = builderTemplate.methodPrefix;
            this.innerBuilder = builderTemplate.innerBuilder;

            this.butMethod = builderTemplate.butMethod;
            this.useSingleField = builderTemplate.useSingleField;
        }
    }

    public static BuilderTemplateBuilder builder() {
        return BuilderTemplateBuilder.aBuilderTemplate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodPrefix() {
        return methodPrefix;
    }

    public void setMethodPrefix(String methodPrefix) {
        this.methodPrefix = methodPrefix;
    }

    public Boolean getInnerBuilder() {
        return innerBuilder;
    }

    public void setInnerBuilder(Boolean innerBuilder) {
        this.innerBuilder = innerBuilder;
    }

    public Boolean getButMethod() {
        return butMethod;
    }

    public void setButMethod(Boolean butMethod) {
        this.butMethod = butMethod;
    }

    public Boolean getUseSingleField() {
        return useSingleField;
    }

    public void setUseSingleField(Boolean useSingleField) {
        this.useSingleField = useSingleField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuilderTemplate that = (BuilderTemplate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.templateName;
    }

    public static final class BuilderTemplateBuilder {
        private String id;
        private String templateName;
        private String className;
        private String methodPrefix;
        private Boolean innerBuilder;
        private Boolean butMethod;
        private Boolean useSingleField;

        private BuilderTemplateBuilder() {
        }

        public static BuilderTemplateBuilder aBuilderTemplate() {
            return new BuilderTemplateBuilder();
        }

        public BuilderTemplateBuilder id(String id) {
            this.id = id;
            return this;
        }

        public BuilderTemplateBuilder templateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public BuilderTemplateBuilder className(String className) {
            this.className = className;
            return this;
        }

        public BuilderTemplateBuilder methodPrefix(String methodPrefix) {
            this.methodPrefix = methodPrefix;
            return this;
        }

        public BuilderTemplateBuilder innerBuilder(Boolean innerBuilder) {
            this.innerBuilder = innerBuilder;
            return this;
        }

        public BuilderTemplateBuilder butMethod(Boolean butMethod) {
            this.butMethod = butMethod;
            return this;
        }

        public BuilderTemplateBuilder useSingleField(Boolean useSingleField) {
            this.useSingleField = useSingleField;
            return this;
        }

        public BuilderTemplate build() {
            BuilderTemplate builderTemplate = new BuilderTemplate();
            builderTemplate.setId(id);
            builderTemplate.setTemplateName(templateName);
            builderTemplate.setClassName(className);
            builderTemplate.setMethodPrefix(methodPrefix);
            builderTemplate.setInnerBuilder(innerBuilder);
            builderTemplate.setButMethod(butMethod);
            builderTemplate.setUseSingleField(useSingleField);
            return builderTemplate;
        }
    }
}

