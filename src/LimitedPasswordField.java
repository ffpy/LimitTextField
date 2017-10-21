import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * 添加的输入长度限制
 */
public class LimitedPasswordField extends JPasswordField implements DocumentListener {
    private int maxLength = 0;
    private char echoChar = '•';

    public LimitedPasswordField() {
    }

    public LimitedPasswordField(String text, int maxLength) {
        this(text, maxLength, 0);
    }

    public LimitedPasswordField(int columns, int maxLength) {
        this(null, columns, maxLength);
    }

    public LimitedPasswordField(String text, int columns, int maxLength) {
        super(text, columns);
        setMaxLength(maxLength);
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
        insertUpdate(null);
    }

    public void setEchoChar(char ch) {
        echoChar = ch;
        super.setEchoChar(ch);
    }

    /**
     * 设置密码是否明文显示
     * @param isVisual 是否明文显示
     */
    public void setVisualPassword(boolean isVisual) {
        if (isVisual) {
            super.setEchoChar((char) 0);
        } else {
            super.setEchoChar(echoChar);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        final String text = String.valueOf(getPassword());
        // 长度限制
        if (maxLength > 0 && text.length() > maxLength) {
            final String str = text.substring(0, maxLength);
            EventQueue.invokeLater(() -> setText(str));
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
