package sg.edu.nus.cs2103t.omnitask.ui;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.model.Task;

@SuppressWarnings("serial")
public class UIMainImpl extends JFrame implements UI {
	
	private Controller controller;
	
	private JTable taskTable;
	
	private JTextField outputField;
	
	private JTextField inputField;
	
	private ArrayList<Task> tasks;
	
	public UIMainImpl(Controller controller) {
		this.controller = controller;
		tasks = new ArrayList<Task>();
	}

	@Override
	public void showMessage(String msg) {
		outputField.setText(msg);
	}

	@Override
	public void showError(String msg) {
		outputField.setText("Error: " + msg);
	}

	@Override
	public void start() {
		setupUI();
        
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIMainImpl.this.setVisible(true);
            }
        });
		
		controller.processUserInput("display");
		showMessage("Welcome to OmniTask. Type 'help' to get help.");
		
		// Make sure input is focused
		inputField.grabFocus();
	}
	
	private void setupUI() {
		setTitle("OmniTask");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        taskTable = new JTable(new TaskTableModel());
        
        // TODO: Make code more decoupled?
        TableColumn column = null;
        for (int i = 0; i < 2; i++) {
            column = taskTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(10);
            } else {
                column.setPreferredWidth(600);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(taskTable);
        taskTable.setFillsViewportHeight(true);
        
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Process user input by passing it to controller
				if (!inputField.getText().trim().equals("")) {
					controller.processUserInput(inputField.getText());
					inputField.setText("");
				}
			}
        	
        });
        
        outputField = new JTextField();
        outputField.setEditable(false);
        
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addComponent(scrollPane)
                .addComponent(inputField)
                .addComponent(outputField)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
        		.addComponent(scrollPane, 400, 400, 1000)
        		.addGap(10)
        		.addComponent(outputField, 40, 40, 40)
                .addComponent(inputField, 40, 40, 40)
        );
    }

	@Override
	public void exit() {

	}

	@Override
	public void updateTaskListings(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		taskTable.updateUI();
	}
	
	private class TaskTableModel extends AbstractTableModel {
		// TODO: Decouple this line somehow?
		private String[] columnNames = new String[]{"id", "name"};

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
	        return columnNames[col];
	    }

		@Override
		public int getRowCount() {
			return tasks.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			if (col == 0) {
				return tasks.get(row).getId();
			} else if (col == 1) {
				return tasks.get(row).getName();
			} else {
				new Exception("Not implemented.").printStackTrace();
				return null;
			}
		}
		
	}

}