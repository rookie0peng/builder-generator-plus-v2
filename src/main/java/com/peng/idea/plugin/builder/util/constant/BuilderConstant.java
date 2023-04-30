package com.peng.idea.plugin.builder.util.constant;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/17
 * </pre>
 */
public final class BuilderConstant {

    public static final String BUILDER_SUFFIX = "Builder";

    public static final String METHOD_PREFIX = "with";

    public static final class GenerateBuilder {

        public static final String DIALOG_NAME = "GenerateBuilder";

        public static final class PopupChooserTitle {

            public static final String BUILDER_ALREADY_EXISTS = "Builder already exists";

            public static final String BUILDER_NOT_FOUND = "Builder not found";
        }

    }

    public static final class RegenerateBuilder {

        public static final String DIALOG_NAME = "RegenerateBuilder";


    }

    public static final class GotoBuilder {

        public static final String DIALOG_NAME = "GotoBuilder";


    }


    public static final class RemoveBuilder {

        public static final String DIALOG_NAME = "RemoveBuilder";

        public static final class DialogComponentKey {

            public static final String SRC_PSI_CLASS = "srcPsiClass";

            public static final String EDITOR_PSI_CLASS = "editorPsiClass";

            public static final String DST_PSI_CLASS = "dstPsiClass";
        }
    }
}
