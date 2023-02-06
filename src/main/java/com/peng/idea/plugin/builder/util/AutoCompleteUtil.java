package com.peng.idea.plugin.builder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.isNull;
import static com.peng.idea.plugin.builder.util.CollectionUtil.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/5/3 20:49
 * </pre>
 */
public class AutoCompleteUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoCompleteUtil.class);

    private static final Robot ROBOT;

    static {
        try {
            ROBOT = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame();
        frame.setTitle("Auto Completion Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 500, 400);

        ArrayList<String> items = new ArrayList<String>();
        Locale[] locales = Locale.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            String item = locales[i].getDisplayName();
            items.add(item);
        }
        System.out.println("items: " + items);
        JTextField txtInput = new JTextField();
        setupAutoComplete(txtInput, items);
        txtInput.setColumns(30);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(txtInput, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private static boolean isAdjusting(JComboBox<String> cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    private static void setAdjusting(JComboBox<String> cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    public static void setupAutoComplete(final JTextField txtInput, final List<String> items) {
        final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        final JComboBox<String> cbInput = new JComboBox<>(model) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        setAdjusting(cbInput, false);
        for (String item : items) {
            model.addElement(item);
        }
        cbInput.setSelectedItem(null);
        // comboBox 选中后，将内容传递到 textField
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAdjusting(cbInput)) {
                    if (cbInput.getSelectedItem() != null) {
                        txtInput.setText(cbInput.getSelectedItem().toString());
                    }
                }
            }
        });
        // comboBox 输入框自动跳过tab选中
        cbInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ROBOT.keyPress(KeyEvent.VK_TAB);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        // textField 按键事件处理
        txtInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
//                LOGGER.info("txtInput--keyPressed: {}", e.getKeyCode());
                setAdjusting(cbInput, true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (cbInput.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (cbInput.isPopupVisible()) {
                        e.setSource(cbInput);
                        cbInput.dispatchEvent(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            txtInput.setText(isNull(cbInput.getSelectedItem()) ? "" : cbInput.getSelectedItem().toString());
                            cbInput.setPopupVisible(false);
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbInput.setPopupVisible(false);
                }
                setAdjusting(cbInput, false);
            }
        });
        // textField 内容变更时，修改 comboBox 的候选框
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
//                LOGGER.info("txtInput--insertUpdate now");
                updateList();
            }

            public void removeUpdate(DocumentEvent e) {
//                LOGGER.info("txtInput--removeUpdate now");
                updateList();
            }

            public void changedUpdate(DocumentEvent e) {
//                LOGGER.info("txtInput--changedUpdate now");
                updateList();
            }

            private void updateList() {
                setAdjusting(cbInput, true);
                model.removeAllElements();
                String input = txtInput.getText();
                if (!input.isEmpty()) {
                    for (String item : items) {
                        if (item.toLowerCase().startsWith(input.toLowerCase())) {
                            model.addElement(item);
                        }
                    }
                } else {
                    safeCollection(items).forEach(model::addElement);
                }
                cbInput.setPopupVisible(false);
                cbInput.setPopupVisible(model.getSize() > 0);
                setAdjusting(cbInput, false);
            }
        });
        // 鼠标进入 textField 输入框时，修改 comboBox 的候选框
        txtInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                LOGGER.info("txtInput--mouseClicked now");
                setAdjusting(cbInput, true);
                model.removeAllElements();
                String input = txtInput.getText();
                if (!input.isEmpty()) {
                    for (String item : items) {
                        if (item.toLowerCase().startsWith(input.toLowerCase())) {
                            model.addElement(item);
                        }
                    }
                } else {
                    safeCollection(items).forEach(model::addElement);
                }
                cbInput.setPopupVisible(model.getSize() > 0);
                setAdjusting(cbInput, false);
            }
        });
        // textField 焦点事件处理
        txtInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
//                LOGGER.info("txtInput--focusGained now");
                setAdjusting(cbInput, true);
                model.removeAllElements();
                String input = txtInput.getText();
                if (!input.isEmpty()) {
                    for (String item : items) {
                        if (item.toLowerCase().startsWith(input.toLowerCase())) {
                            model.addElement(item);
                        }
                    }
                } else {
                    safeCollection(items).forEach(model::addElement);
                }
                cbInput.setPopupVisible(false);
                cbInput.setPopupVisible(model.getSize() > 0);
                setAdjusting(cbInput, false);
            }

            @Override
            public void focusLost(FocusEvent e) {
//                LOGGER.info("txtInput--focusLost now");
                model.removeAllElements();
                cbInput.setPopupVisible(false);
                setAdjusting(cbInput, false);
            }
        });

        txtInput.setLayout(new BorderLayout());
        txtInput.add(cbInput, BorderLayout.SOUTH);


    }
}
