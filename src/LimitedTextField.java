import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;

/**
 * 添加了输入限制，包括长度限制和正则匹配限制
 */
public class LimitedTextField extends JTextField implements DocumentListener {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_NUMBER = 1;
    public static final int TYPE_NUMBER_SIGNED = 2;
    public static final int TYPE_NUMBER_DECIMAL = 3;
    public static final int TYPE_NUMBER_DECIMAL_SIGNED = 4;
    public static final int TYPE_EMAIL = 5;
    public static final int TYPE_DATE = 6;
    public static final int TYPE_TIME = 7;
    public static final int TYPE_WORD = 8;

    private int maxLength = 0;
    private int inputType = TYPE_NONE;
    private String oldText = "";
    private String regex = null;

    public LimitedTextField() {
    }

    public LimitedTextField(int columns, int maxLength) {
        this(columns, maxLength, TYPE_NONE);
    }

    public LimitedTextField(int columns, int maxLength, int inputType) {
        this(null, columns, maxLength, inputType);
    }

    public LimitedTextField(String text, int columns, int maxLength) {
        this(text, columns, maxLength, TYPE_NONE);
    }

    public LimitedTextField(String text, int columns, int maxLength, int inputType) {
        super(text, columns);
        setMaxLength(maxLength);
        setInputType(inputType);
        getDocument().addDocumentListener(this);
    }

    /**
     * 获取最大长度
     * @return 返回最大长度
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * 设置最大长度
     * @param maxLength 最大长度
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        String text = getText();
        if (text.length() > maxLength) {
            setText(text.substring(0, maxLength));
        }
    }

    /**
     * 获取输入类型
     * @return 返回输入类型
     */
    public int getInputType() {
        return inputType;
    }

    /**
     * 设置输入类型
     * @param inputType 输入类型
     */
    public void setInputType(int inputType) {
        this.inputType = inputType;

        switch (inputType) {
            case TYPE_NUMBER: regex = "\\d*"; break;
            case TYPE_NUMBER_SIGNED: regex = "-?\\d*"; break;
            case TYPE_NUMBER_DECIMAL: regex = "\\d+\\.?\\d*"; break;
            case TYPE_NUMBER_DECIMAL_SIGNED: regex = "-?\\d+\\.?\\d*"; break;
            case TYPE_EMAIL: regex = "[\\w-]+(@(([\\w-]*)|([\\w-]+((\\.[\\w-]*)|(\\.[\\w-]+((\\.[\\w-]*)|(\\.[\\w-]+\\.[\\w-]*)))))))?"; break;
            case TYPE_DATE: regex = "[1-9](\\d{0,3}|\\d{3}-(|0(|[13578](|-(|0(|[1-9])|[1-2](|[0-9])|3(|[0-1])))|[469](|-(|0(|[1-9])|[1-2](|[0-9])|3(|0)))|2(|-(|0(|[1-9])|2(|[0-9]))))|1(|1(|-(|0(|[1-9])|[1-2](|[0-9])|3(|0)))|[02](|-(|0(|[1-9])|[1-2](|[0-9])|3(|[0-1]))))))"; break;
            case TYPE_TIME: regex = "(\\d{0,2}|\\d{2}:(|(\\d{0,2}|\\d{2}:(|(\\d{0,2}|\\d{2})))))"; break;
            case TYPE_WORD: regex = "\\w*"; break;
            default: regex = null;
        }
        setRegex(regex);
    }

    /**
     * 设置匹配的正则表达式
     * @param regex 匹配的正则表达式
     */
    public void setRegex(String regex) {
        this.regex = regex;
        oldText = "";
        setText("");
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        final String text = getText();
        if (text.equals(oldText)) return;

        // 长度限制
        if (maxLength > 0 && text.length() > maxLength) {
            final String str = text.substring(0, maxLength);
            EventQueue.invokeLater(() -> setText(str));
            return;
        }

        // 输入类型限制
        if (regex != null) {
            if (!text.matches(regex)) {
                EventQueue.invokeLater(() -> setText(oldText));
                return;
            }
        }

        oldText = text;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        oldText = getText();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
