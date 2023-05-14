package com.peng.idea.plugin.builder.util.constant;

import com.peng.idea.plugin.builder.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/4/30 22:25
 * </pre>
 */
public final class Constants {

    public static final class Settings {

        public static final String BUILDER = "Builder Generator Plus";

        public static final String BUILDER_TEMPLATE = "Builder Template Plus";
    }

    public static final String BUILDER_SETTINGS_RECENTS_KEY = "BuilderSettingsComponent.RecentsKey";

    public static final class Template {

        public static final String INTERNAL_CLASS_NAME = "${internalClassName}";

        public static final String INTERNAL_BUILDER_METHOD_NAME = "${internalBuilderMethodName}";

        public static final String SRC_CLASS_NAME = "${srcClassName}";

        private static final List<String> DYNAMIC_VALUES = ListUtil.newArrayList(INTERNAL_CLASS_NAME, SRC_CLASS_NAME);

        public static List<String> getDynamicValues() {
            return new ArrayList<>(DYNAMIC_VALUES);
        }
    }



}
