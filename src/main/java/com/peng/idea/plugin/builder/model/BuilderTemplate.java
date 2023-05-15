package com.peng.idea.plugin.builder.model;

import com.intellij.util.xmlb.annotations.Tag;
import com.peng.idea.plugin.builder.util.GsonUtil;
import com.peng.idea.plugin.builder.util.constant.BuilderConstant;

import java.util.List;
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

    private String builderMethodName = BuilderConstant.Template.INTERNAL_BUILDER_METHOD_NAME;

    private String methodPrefix;



    private Boolean srcClassBuilder = true;

    private Boolean innerBuilder;

    private Boolean butMethod;

    private Boolean useSingleField;

    public BuilderTemplate() {

    }

    public BuilderTemplate(BuilderTemplate builderTemplate) {
        if (builderTemplate != null) {
            this.id = builderTemplate.id;
            this.templateName = builderTemplate.templateName;
            this.className = builderTemplate.className;
            this.builderMethodName = builderTemplate.builderMethodName;
            this.methodPrefix = builderTemplate.methodPrefix;

            this.srcClassBuilder = builderTemplate.srcClassBuilder;
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

    public String getBuilderMethodName() {
        return builderMethodName;
    }

    public void setBuilderMethodName(String builderMethodName) {
        this.builderMethodName = builderMethodName;
    }

    public String getMethodPrefix() {
        return methodPrefix;
    }

    public void setMethodPrefix(String methodPrefix) {
        this.methodPrefix = methodPrefix;
    }

    public Boolean getSrcClassBuilder() {
        return srcClassBuilder;
    }

    public void setSrcClassBuilder(Boolean srcClassBuilder) {
        this.srcClassBuilder = srcClassBuilder;
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
        private String builderMethodName = BuilderConstant.Template.INTERNAL_BUILDER_METHOD_NAME;
        private String methodPrefix;
        private Boolean srcClassBuilder = true;
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

        public BuilderTemplateBuilder builderMethodName(String builderMethodName) {
            this.builderMethodName = builderMethodName;
            return this;
        }

        public BuilderTemplateBuilder methodPrefix(String methodPrefix) {
            this.methodPrefix = methodPrefix;
            return this;
        }

        public BuilderTemplateBuilder srcClassBuilder(Boolean srcClassBuilder) {
            this.srcClassBuilder = srcClassBuilder;
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
            builderTemplate.setBuilderMethodName(builderMethodName);
            builderTemplate.setMethodPrefix(methodPrefix);
            builderTemplate.setSrcClassBuilder(srcClassBuilder);
            builderTemplate.setInnerBuilder(innerBuilder);
            builderTemplate.setButMethod(butMethod);
            builderTemplate.setUseSingleField(useSingleField);
            return builderTemplate;
        }
    }

    public static final class ImmutableBuilderTemplate extends BuilderTemplate {

        public static final ImmutableBuilderTemplate INTERNAL_BUILDER_TEMPLATE = new ImmutableBuilderTemplate(
                BuilderTemplate.builder()
                        .id("---internal---")
                        .templateName("")
                        .className(BuilderConstant.Template.INTERNAL_CLASS_NAME)
                        .builderMethodName(BuilderConstant.Template.INTERNAL_BUILDER_METHOD_NAME)
                        .methodPrefix(BuilderConstant.METHOD_PREFIX)

                        .srcClassBuilder(true)
                        .innerBuilder(false)
                        .butMethod(false)
                        .useSingleField(false)

                        .build()
        );

        public ImmutableBuilderTemplate() {

        }

        public ImmutableBuilderTemplate(BuilderTemplate builderTemplate) {
            super.setId(builderTemplate.getId());
            super.setTemplateName(builderTemplate.getTemplateName());
            super.setClassName(builderTemplate.getClassName());
            super.setBuilderMethodName(builderTemplate.getBuilderMethodName());
            super.setMethodPrefix(builderTemplate.getMethodPrefix());

            super.setSrcClassBuilder(builderTemplate.getSrcClassBuilder());
            super.setInnerBuilder(builderTemplate.getInnerBuilder());
            super.setButMethod(builderTemplate.getButMethod());
            super.setUseSingleField(builderTemplate.getUseSingleField());
        }

        static UnsupportedOperationException uoe() { return new UnsupportedOperationException(); }

        @Override
        public void setId(String id) {
            throw uoe();
        }

        @Override
        public void setTemplateName(String templateName) {
            super.setTemplateName(templateName);
        }

        @Override
        public void setClassName(String className) {
            super.setClassName(className);
        }

        @Override
        public void setBuilderMethodName(String builderMethodName) {
            super.setBuilderMethodName(builderMethodName);
        }

        @Override
        public void setMethodPrefix(String methodPrefix) {
            super.setMethodPrefix(methodPrefix);
        }

        @Override
        public void setSrcClassBuilder(Boolean srcClassBuilder) {
            super.setSrcClassBuilder(srcClassBuilder);
        }

        @Override
        public void setInnerBuilder(Boolean innerBuilder) {
            super.setInnerBuilder(innerBuilder);
        }

        @Override
        public void setButMethod(Boolean butMethod) {
            super.setButMethod(butMethod);
        }

        @Override
        public void setUseSingleField(Boolean useSingleField) {
            super.setUseSingleField(useSingleField);
        }

        public static void main(String[] args) {
            BuilderTemplate build = BuilderTemplate.builder().id("q111").build();
            ImmutableBuilderTemplate immutableBuilderTemplate = new ImmutableBuilderTemplate(build);
            System.out.println("over, " + GsonUtil.GSON.toJson(immutableBuilderTemplate));

        }

    }

    public static void main(String[] args) {
        BuilderTemplate build = BuilderTemplate.builder().id("q111").build();
        ImmutableBuilderTemplate immutableBuilderTemplate = new ImmutableBuilderTemplate(build);
        System.out.println("over, " + GsonUtil.GSON.toJson(immutableBuilderTemplate));

    }
}

