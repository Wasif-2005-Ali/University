package View;

import java.awt.Component;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Add" : value.toString());
        setBackground(new Color(30, 81, 40));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 12));
        return this;
    }

}
