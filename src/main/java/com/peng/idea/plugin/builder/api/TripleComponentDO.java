package com.peng.idea.plugin.builder.api;

import javax.swing.*;
import java.util.StringJoiner;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/17
 * </pre>
 */
public class TripleComponentDO {

    /**
     * class name
     */
    private JTextField clazzName;

    /**
     * fully qualified name
     */
    private JTextField qualifiedName;

    /**
     * check box
     */
    private JCheckBox checkBox;

    public static TripleComponentDOBuilder builder() {
        return TripleComponentDOBuilder.aTripleComponentDO();
    }

    public JTextField getClazzName() {
        return clazzName;
    }

    public void setClazzName(JTextField clazzName) {
        this.clazzName = clazzName;
    }

    public JTextField getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(JTextField qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TripleComponentDO.class.getSimpleName() + "[", "]")
                .add("clazzName=" + clazzName)
                .add("qualifiedName=" + qualifiedName)
                .add("checkBox=" + checkBox)
                .toString();
    }

    public static final class TripleComponentDOBuilder {
        private JTextField clazzName;
        private JTextField qualifiedName;
        private JCheckBox checkBox;

        private TripleComponentDOBuilder() {
        }

        public static TripleComponentDOBuilder aTripleComponentDO() {
            return new TripleComponentDOBuilder();
        }

        public TripleComponentDOBuilder clazzName(JTextField clazzName) {
            this.clazzName = clazzName;
            return this;
        }

        public TripleComponentDOBuilder qualifiedName(JTextField qualifiedName) {
            this.qualifiedName = qualifiedName;
            return this;
        }

        public TripleComponentDOBuilder checkBox(JCheckBox checkBox) {
            this.checkBox = checkBox;
            return this;
        }

        public TripleComponentDO build() {
            TripleComponentDO tripleComponentDO = new TripleComponentDO();
            tripleComponentDO.setClazzName(clazzName);
            tripleComponentDO.setQualifiedName(qualifiedName);
            tripleComponentDO.setCheckBox(checkBox);
            return tripleComponentDO;
        }
    }
}
