package CarCharge;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JSplitPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	private JScrollPane scrollPane_1;
	private JButton btnStartController, btnAddCars, btnClearMessages;
	private JTextPane textPannel;
	private JSplitPane splitPane_2;
	private JSplitPane splitPane_3;
	private JList<String> carlist;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable table;
	private JTabbedPane tabPannel;
	private DefaultTableModel dtm;
	private int interval = 30;
	private DefaultListModel<String> slotNum;
	//dialogitems
	private javax.swing.JButton btnAddCar;
    private javax.swing.JLabel lblEndTime;
    private javax.swing.JLabel lblMinCharge;
    private javax.swing.JLabel lblStartTime;
    private javax.swing.JLabel lblStationType;
    private javax.swing.JLabel lblcurrentCharge;
    private javax.swing.JRadioButton rbtnLarge;
    private javax.swing.JRadioButton rbtnMedium;
    private javax.swing.JRadioButton rbtnSmall;
    private javax.swing.ButtonGroup rbtngrpStation;
    private javax.swing.JTextField txtCurrCharge;
    private javax.swing.JTextField txtEtime;
    private javax.swing.JTextField txtMinCharge;
    private javax.swing.JTextField txtSTime;
    //Mem vars
    private Process mSA;
    private int carcount = 0;
    private LinkedList<Process> caragentsPlist = new LinkedList<Process>();
    private Thread currthread;
	private JLabel lblRegistrationNumber;
	private JTextField txtRegistrationNumber;
    
    
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		initJade();
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
		//Start controller button (left)
	
		
		//Stop Controller button (right)

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
		
		slotNum = new DefaultListModel();
		carlist = new JList<String>(slotNum);
				
		carlist.setPreferredSize(new Dimension(100,150));
		
		//list.a
		splitPane_2.setRightComponent(carlist);
		
		//Adds panel tab for selection of number of slots, choose pre-decided numbers up to 30
		splitPane_3 = new JSplitPane();
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);

		btnAddCars = new JButton("Add Car");
		splitPane_2.setLeftComponent(btnAddCars);
		btnAddCars.setEnabled(true);
		btnAddCars.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			    btnAddCarAction();
			  } 
			} );

		//DefaultListModel<String> slotNum = new DefaultListModel();
		//slotNum.addElement("1");
		//list = new JList<String>(slotNum);		
		splitPane_3.setRightComponent(null);


		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane_3, BorderLayout.CENTER);

		/*schduleLabel = new JLabel("space to show something like the current number of vehicle being charged. ");
		splitPane_3.setLeftComponent(schduleLabel);
*/
		scrollPane = new JScrollPane();
		scrollPane.setEnabled(false);
		splitPane_3.setBottomComponent(scrollPane);
		splitPane_3.setTopComponent(null);

		// Make Table
		//TODO to change to reflect the fact we can now edit the number of slot available
		Object[][] TabeleData = getTableTimeFormat(interval);
		table = new JTable();
		dtm = new DefaultTableModel(TabeleData,
				new String[] { "Time", "Large station", "Medium station", "Small station"});
		table.setModel(dtm);
		table.getColumn("Time").setPreferredWidth(30);	
		carlist.addListSelectionListener(new ListSelectionListener() {	
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					textPannel.setText(""); //CLear old text
					if(currthread != null) {
						currthread.stop();
					}
					System.out.println(carlist.getSelectedIndex());
					Runnable task = new Runnable() {
		                public void run() {
		                    //TODO
		                	InputStream i = caragentsPlist.get(carlist.getSelectedIndex()).getInputStream();
		                	InputStreamReader isr = new InputStreamReader(i);
		                	BufferedReader br = new BufferedReader(isr);
		                	 try {		                		 
								while((br.readLine())!=null){
									textPannel.setText(textPannel.getText() + br.readLine()+ "\n");									
								 }
							} catch (IOException e) {
								e.printStackTrace();
							}
		                }
		            };
		            currthread = new Thread(task);					
		            currthread.start();	
				}			
			}
		});
		scrollPane.setViewportView(table);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	        	System.out.println("Stopping orphaned threads");
	            if(currthread != null) {
	            	currthread.stop();
	            }
	            for(Process p :caragentsPlist) {
	            	p.destroy();	            	
	            }
	            mSA.destroy();
	        }
	    }, "Shutdown-thread"));
		this.setVisible(true);
	}

	private void initJade() {
		try {
			Process p = Runtime.getRuntime().exec("java -cp lib\\jade.jar;bin jade.Boot -gui -agents MasterSchedulingAgent:CarCharge.MasterSchedulingAgent");
			mSA = p;					
		} catch (IOException e1) {					
			e1.printStackTrace();
		}		
	}

	protected void btnAddCarAction() {
		final JDialog frame = new JDialog(this, "Add car", true);
		rbtngrpStation = new javax.swing.ButtonGroup();
        lblStationType = new javax.swing.JLabel();
        rbtnLarge = new javax.swing.JRadioButton();
        rbtnMedium = new javax.swing.JRadioButton();
        rbtnSmall = new javax.swing.JRadioButton();
        lblRegistrationNumber = new javax.swing.JLabel();
        lblcurrentCharge = new javax.swing.JLabel();
        lblMinCharge = new javax.swing.JLabel();
        lblStartTime = new javax.swing.JLabel();
        lblEndTime = new javax.swing.JLabel();
        txtRegistrationNumber = new javax.swing.JTextField();
        txtMinCharge = new javax.swing.JTextField();
        txtCurrCharge = new javax.swing.JTextField();
        txtSTime = new javax.swing.JTextField();
        txtEtime = new javax.swing.JTextField();
        btnAddCar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        lblStationType.setText("Station Type: ");
        
        lblRegistrationNumber.setText("Registration Number: ");

        rbtnLarge.setText("Large");      

        rbtnMedium.setText("Medium");

        rbtnSmall.setText("Small");
        
        rbtnLarge.setActionCommand("Large");      

        rbtnMedium.setActionCommand("Medium");

        rbtnSmall.setActionCommand("Small");

        lblcurrentCharge.setText("Current Charge: ");

        lblMinCharge.setText("Minimum Charge:");

        lblStartTime.setText("Start Time:");

        lblEndTime.setText("End Time:");

        btnAddCar.setText("Add Car");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStationType)
                .addGap(18, 18, 18)
                .addComponent(rbtnLarge)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnMedium)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnSmall)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegistrationNumber)
                            .addComponent(lblcurrentCharge)
                            .addComponent(lblEndTime)
                            .addComponent(lblMinCharge)
                            .addComponent(lblStartTime))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRegistrationNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtCurrCharge, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEtime, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtMinCharge, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(btnAddCar))
                .addGap(44, 44, 44))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStationType)
                    .addComponent(rbtnLarge)
                    .addComponent(rbtnMedium)
                    .addComponent(rbtnSmall))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblRegistrationNumber)
                        .addComponent(txtRegistrationNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcurrentCharge)
                    .addComponent(txtCurrCharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMinCharge)
                    .addComponent(txtMinCharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStartTime)
                    .addComponent(txtSTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndTime)
                    .addComponent(txtEtime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnAddCar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rbtngrpStation.add(rbtnLarge);
        rbtngrpStation.add(rbtnSmall);
        rbtngrpStation.add(rbtnMedium);
        btnAddCar.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {					
				carcount +=1;
				slotNum.addElement(txtRegistrationNumber.getText());			
				String cmd = "cmd /c \"java -cp lib\\jade.jar;bin jade.Boot -container -host localhost -exitwhenempty true -agents carag"+carcount+":CarCharge.CarAgent(\""
						+ rbtngrpStation.getSelection().getActionCommand() + "\"," + txtMinCharge.getText()
						+ "," + txtCurrCharge.getText() + "," + txtSTime.getText() + "," + txtEtime.getText() + ")\"";
				System.out.println(cmd);
				try {
					Process p = Runtime.getRuntime().exec(cmd);
					caragentsPlist.add(p);					
				} catch (IOException e1) {					
					e1.printStackTrace();
				}
				frame.dispose();
			}
		});
		frame.pack();
		frame.setVisible(true);
		
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
		timeSlots.remove(timeSlots.size() -1);
		return (Object[][]) timeSlots.toArray(new Object[timeSlots.size()][]);
	}

	@Override
	public void EnableSimulationButton() {
		btnStartController.setEnabled(false);
	}

	@Override
	public void EnableDisplay() {
		btnAddCar.setEnabled(true);		
		textPannel.setEnabled(true);
		btnClearMessages.setEnabled(true);
	}

}
