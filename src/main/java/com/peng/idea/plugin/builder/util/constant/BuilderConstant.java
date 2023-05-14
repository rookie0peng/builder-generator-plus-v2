package com.peng.idea.plugin.builder.util.constant;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.peng.idea.plugin.builder.util.ListUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/17
 * </pre>
 */
public final class BuilderConstant {

    public static final String BUILDER_SETTINGS_RECENTS_KEY = "BuilderSettingsComponent.RecentsKey";

    public static final String BUILDER_SUFFIX = "Builder";

    public static final String METHOD_PREFIX = "with";

    public static final String A_PREFIX = "a";

    public static final String AN_PREFIX = "an";

    public static final class GenerateBuilder {

        public static final String DIALOG_NAME = "GenerateBuilder";

        public static final String POPUP_NAME = "Generate builder...";

        public static final Icon ICON = IconLoader.getIcon("/actions/addFile.svg", AllIcons.class);

        public static final class PopupChooserTitle {

            public static final String BUILDER_ALREADY_EXISTS = "Builder already exists";

            public static final String BUILDER_NOT_FOUND = "Builder not found";
        }

    }

    public static final class RegenerateBuilder {

        public static final String DIALOG_NAME = "RegenerateBuilder";

        public static final String POPUP_NAME = "Regenerate builder...";

        public static final Icon ICON = IconLoader.getIcon("/actions/addFile_dark.svg", AllIcons.class);

    }

    public static final class JumpToBuilder {

        public static final String DIALOG_NAME = "JumpToBuilder";

        public static final String POPUP_NAME = "Jump to builder...";

        public static final Icon ICON = IconLoader.getIcon("/actions/find.svg", AllIcons.class);


    }


    public static final class RemoveBuilder {

        public static final String DIALOG_NAME = "RemoveBuilder";

        public static final String POPUP_NAME = "Remove builder...";

        public static final Icon ICON = IconLoader.getIcon("/actions/deleteTag.svg", AllIcons.class);

        public static final class DialogComponentKey {

            public static final String SRC_PSI_CLASS = "srcPsiClass";

            public static final String EDITOR_PSI_CLASS = "editorPsiClass";

            public static final String DST_PSI_CLASS = "dstPsiClass";
        }
    }

    public static final class Settings {

        public static final String BUILDER = "Builder Generator Plus";

        public static final String BUILDER_TEMPLATE = "Builder Template Plus";
    }

    public static final class Template {

        public static final String INTERNAL_CLASS_NAME = "${internalClassName}";

        public static final String INTERNAL_BUILDER_METHOD_NAME = "${internalBuilderMethodName}";

        public static final String SRC_CLASS_NAME = "${srcClassName}";

        private static final List<String> DYNAMIC_VALUES = ListUtil.newArrayList(INTERNAL_CLASS_NAME, SRC_CLASS_NAME);

        public static List<String> getDynamicValues() {
            return new ArrayList<>(DYNAMIC_VALUES);
        }

        public static final class Value {


            public static final String INTERNAL_BUILDER_METHOD_NAME = "builder";
        }
    }
}
