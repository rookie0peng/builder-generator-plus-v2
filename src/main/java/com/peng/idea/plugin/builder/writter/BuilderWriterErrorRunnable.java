package com.peng.idea.plugin.builder.writter;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.java.JavaBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2023/1/19 16:32
 * </pre>
 */
public class BuilderWriterErrorRunnable implements Runnable {

    static final String INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE = "intention.error.cannot.create.class.message";
    static final String INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE = "intention.error.cannot.create.class.title";

    private final Project project;
    private final String className;

    public BuilderWriterErrorRunnable(Project project, String className) {
        this.project = project;
        this.className = className;
    }

    @Override
    public void run() {
        Messages.showErrorDialog(project,
                JavaBundle.message(INTENTION_ERROR_CANNOT_CREATE_CLASS_MESSAGE, className),
                JavaBundle.message(INTENTION_ERROR_CANNOT_CREATE_CLASS_TITLE));
    }
}

