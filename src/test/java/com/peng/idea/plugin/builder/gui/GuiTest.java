package com.peng.idea.plugin.builder.gui;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/4/8
 * </pre>
 */
public class GuiTest {

    private String a;

    public static final class GuiTestBuilder {
        private String a;

        private GuiTestBuilder() {
        }

        public static GuiTestBuilder aGuiTest() {
            return new GuiTestBuilder();
        }

        public GuiTestBuilder withA(String a) {
            this.a = a;
            return this;
        }

        public GuiTest build() {
            GuiTest guiTest = new GuiTest();
            guiTest.a = this.a;
            return guiTest;
        }
    }
}
