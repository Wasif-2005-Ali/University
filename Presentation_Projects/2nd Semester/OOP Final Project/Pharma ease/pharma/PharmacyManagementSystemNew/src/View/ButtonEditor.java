package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.awt.Font;

import javax.swing.*;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;

import com.toedter.calendar.JDateChooser;

import Controller.InventoryController;
import Model.Inventory;
import Model.Seller;

import javax.swing.DefaultCellEditor;


class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private JFrame frame; // parent frame for dialog
    private int sellerid;
    private Inventory inventory;
    public ButtonEditor(int sellerid,JCheckBox checkBox, JTable table, JFrame frame) {
        super(checkBox);
        this.table = table;
        this.frame = frame;
        this.sellerid = sellerid;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                int row = table.getSelectedRow();
                if (row != -1) {
                    String medId = table.getValueAt(row, 0).toString();
                    String medName = table.getValueAt(row, 1).toString();
                    showInventoryForm(medId, medName,row);
                }
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        label = (value == null) ? "Add" : value.toString();
        button.setText(label);
        return button;
    }

    public Object getCellEditorValue() {
        return label;
    }

    private void showInventoryForm(String medId, String medName, int row) {
        JDialog inventoryDialog = new JDialog(frame, "Add " + medName + " to Inventory", true);
        inventoryDialog.setSize(400, 400);
        inventoryDialog.setLocationRelativeTo(frame); // center on parent
        inventoryDialog.setLayout(null);

        inventoryDialog.add(createLabel("Quantity:",30,30,100));
        JTextField qtyField = createTextField("Enter Quantity", 200, 30, 150, 30);
        inventoryDialog.add(qtyField);

        inventoryDialog.add(createLabel("Manufacture Date:",30,80,130));
        JDateChooser mfdDateField = new JDateChooser(); 
        mfdDateField.setBounds(200, 80, 150, 30);
        inventoryDialog.add(mfdDateField);

        inventoryDialog.add(createLabel("Expiry Date:",30,130,130));
        JDateChooser expDateField = new JDateChooser();
        expDateField.setBounds(200, 130, 150, 30);
        inventoryDialog.add(expDateField);

        inventoryDialog.add(createLabel("Batch Number:",30,180,100));
        JTextField batchField = createTextField("Batch Number", 200, 180, 150, 30);
        inventoryDialog.add(batchField);

        JButton saveBtn = new JButton("Add to Inventory");
        saveBtn.setBounds(200,250,130,30);
        saveBtn.setBackground(new Color(101,200,101));
        saveBtn.setFont(new Font("arial",Font.BOLD,12));
        saveBtn.setForeground(Color.white);
        inventoryDialog.add(saveBtn);

        // Filler or empty label to align layout
        inventoryDialog.add(new JLabel());
        
        saveBtn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

        saveBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
           	 
                int quantity = Integer.parseInt(qtyField.getText());
                
                String mfd = sdf.format(mfdDateField.getDate());
               
                String expiryDateStr = sdf.format(expDateField.getDate());
                
                String batch = batchField.getText().trim();
                               
 
                if(qtyField.getText().isEmpty()||mfdDateField.getDate() == null||
                		expDateField.getDate() == null || batchField.getText().isEmpty())
                {
                	JOptionPane.showMessageDialog(null, "please fill all fields");
                }
                else
                {
                	
                	
                	
                	int confirm = JOptionPane.showConfirmDialog(
                	        null,
                	        "Do you want to add this medicine to the inventory?",
                	        "Confirm Addition",
                	        JOptionPane.YES_NO_OPTION
                	    );
                	
                	 if (confirm == JOptionPane.YES_OPTION)
                	 {
                		 
                		 
                		inventory = new Inventory();
                     	inventory.setMedicineId(row);
                     	inventory.setBatchNumber(batch);
                     	inventory.setQuantity(quantity);
                     	inventory.setSellerId(sellerid);


                     	Date utilDate = expDateField.getDate();
                     	LocalDate localExpDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                     	inventory.setExpiryDate(localExpDate);
                     	
                     	Date utilMfdDate = mfdDateField.getDate();
                     	LocalDate localMfdDate = utilMfdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                     	inventory.setManufacturingDate(localMfdDate);
                		
                     	new InventoryController().addInventory(inventory);
                     	
                     	 JOptionPane.showMessageDialog(null, "Medicine added successfully to inventory!");
                		 
                		 inventoryDialog.dispose(); 
                	 }
                	 else {
                	        JOptionPane.showMessageDialog(null, "Operation cancelled. Medicine not added.");
                	    }
                
                	
                }
                
            	
			}
		});

       
        
        inventoryDialog.setVisible(true);
    }
    
    public JLabel createLabel(String text,int x,int y,int length)
    {
    	JLabel newLabel = new JLabel(text);
    	newLabel.setBounds(x,y,length,30);
    	
    	return newLabel;
    }
    
    public JTextField createTextField(String text, int x, int y, int width, int height) {
        JTextField textField = new JTextField(text);
        textField.setBounds(x, y, width, height);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setCaretColor(Color.BLACK);
        textField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        textField.setMargin(new Insets(5, 5, 5, 5));
        textField.setToolTipText("Enter " + text.toLowerCase());

        // Optional: clear default text on click
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(text)) {
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(text);
                }
            }
        });

        return textField;
    }

}

