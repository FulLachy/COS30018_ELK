package CarCharge;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import javax.swing.JSplitPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.List;

import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JList;

/**
 * This JFrame is used to display buttons that start and stop the simulation, 
 * display messages between agents, select the number of cars and slots to be 
 * used per simulation and display the current optimal schedule.
 */
public class MainFrame extends JFrame implements MainFrameInterface {

	private JLabel schduleLabel;
	private JScrollPane scrollPane_1;
	private JButton btnStartController, btnStopSimulation, btnAddCar, btnAddSlot, btnClearMessages;
	private JTextPane textPannel;
	private JSplitPane splitPane_2;
	private JSplitPane splitPane_3;
	private JList<Integer> list;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable table;
	private JTabbedPane tabPannel;
	private DefaultTableModel dtm;
	private int interval = 30;
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Car Scheduling System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //program exits when frame closes
		setBounds(100, 100, 660, 397);
		contentPane = new JPanel();
		contentPane.setLocation(0, 0);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
        //start simulation and stop simulation buttons
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.NORTH);
		//Start controller button (left)
		btnStartController = new JButton("Start Simulation");
		btnStartController.setActionCommand("StartSimulation");
		//		btnJadeController.addActionListener(controller);
		btnStartController.setBackground(SystemColor.LIGHT_GRAY);
		splitPane.setLeftComponent(btnStartController);
		
		//Stop Controller button (right)
		btnStopSimulation = new JButton("Stop Simulation");
		btnStopSimulation.setActionCommand("StopSimulation");
		splitPane.setRightComponent(btnStopSimulation);
		btnStopSimulation.setEnabled(false);
		btnStopSimulation.setBackground(SystemColor.LIGHT_GRAY);
		//		btnSimulation.addActionListener(controller);
		

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane_1, BorderLayout.WEST);
		scrollPane_1 = new JScrollPane();
		splitPane_1.setRightComponent(scrollPane_1);

		textPannel = new JTextPane();
		textPannel.setEditable(false);
		textPannel.setText("Messages From Agents in decending order");
		textPannel.setEnabled(false);
		scrollPane_1.setViewportView(textPannel);

		tabPannel = new JTabbedPane(JTabbedPane.TOP);
		splitPane_1.setLeftComponent(tabPannel);

		//btnClearMessages = new JButton("space to manage messages from agent");
		//tabPannel.addTab("Manage Messages", null, btnClearMessages, null);
		//btnClearMessages.setActionCommand("ClearMessages");
		//								btnClearMessages.addActionListener(controller);
		//btnClearMessages.setEnabled(false);

		//Adds panel tab for car number selection, pre-decided numbers up to 100
		splitPane_2 = new JSplitPane();
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabPannel.addTab("Add Cars", null, splitPane_2, null);
		

		btnAddCar = new JButton("Add Car");
		splitPane_2.setLeftComponent(btnAddCar);
		btnAddCar.setEnabled(false);
		btnAddCar.setActionCommand("AddCar");
		//										btnAddCar.addActionListener(controller);
		
		DefaultListModel<Integer> carsNum = new DefaultListModel();
		carsNum.addElement(1);
		carsNum.addElement(2);
		carsNum.addElement(5);
		carsNum.addElement(10);
		carsNum.addElement(20);
		carsNum.addElement(50);
		carsNum.addElement(100);
		list = new JList<Integer>();
		list.setModel(carsNum);
		splitPane_2.setRightComponent(list);

		
		//Adds panel tab for selection of number of slots, choose pre-decided numbers up to 30
		splitPane_3 = new JSplitPane();
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabPannel.addTab("Add Slots", null, splitPane_3, null);
		

		btnAddSlot = new JButton("Add Slots");
		splitPane_2.setLeftComponent(btnAddSlot);
		btnAddSlot.setEnabled(false);
		btnAddSlot.setActionCommand("AddSlot");

		DefaultListModel<Integer> slotNum = new DefaultListModel();
		slotNum.addElement(1);
		slotNum.addElement(5);
		slotNum.addElement(10);
		slotNum.addElement(20);
		slotNum.addElement(30);
		list = new JList<Integer>();
		list.setModel(slotNum);
		splitPane_3.setRightComponent(list);


		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane_3, BorderLayout.CENTER);

		/*schduleLabel = new JLabel("space to show something like the current number of vehicle being charged. ");
		splitPane_3.setLeftComponent(schduleLabel);
*/
		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		splitPane_3.setBottomComponent(scrollPane);

		// Make Table
		//TODO to change to reflect the fact we can now edit the number of slot available
		Object[][] TabeleData = getTableTimeFormat(interval);
		table = new JTable();
		dtm = new DefaultTableModel(TabeleData,
				new String[] { "Time", "Station 1", "Station 2", "Station 3", "Station 4" });
		table.setModel(dtm);
		table.getColumn("Time").setPreferredWidth(30);
		scrollPane.setViewportView(table);
		this.setVisible(true);
	}

	/**
	 * Makes a Table with The First Column is all the times for 0:00 to 24:00
	 * depending on the Time Interval.
	 * 
	 * @param interval
	 * @return
	 */
	private Object[][] getTableTimeFormat(int interval) {
		int hours = 0;
		int minutes = 0;
		ArrayList<Object[]> timeSlots = new ArrayList<Object[]>();
		for (minutes = 0; hours < 24; minutes = minutes + interval) {
			if (minutes >= 60) {
				hours++;
				minutes = minutes - 60;
			}
			Object[] timeSlot = new Object[] { String.format("%02d", hours) + " : " + String.format("%02d", minutes) };
			timeSlots.add(timeSlot);
		}

		return (Object[][]) timeSlots.toArray(new Object[timeSlots.size()][]);
	}

	@Override
	public void EnableSimulationButton() {
		btnStartController.setEnabled(false);
		btnStopSimulation.setEnabled(true);
	}

	@Override
	public void EnableDisplay() {
		btnAddCar.setEnabled(true);
		btnStopSimulation.setText("Stop Simulation");
		textPannel.setEnabled(true);
		btnClearMessages.setEnabled(true);
	}

}
