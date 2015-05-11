package triPOS.Java;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TriPOSMain extends JFrame {

	private String pathToTriPOSConfig = "C:\\Program Files (x86)\\Vantiv\\triPOS Service\\triPOS.config";
	private BooleanRef isJSON;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TriPOSMain frame = new TriPOSMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public TriPOSMain() {
		setTitle("triPOS.JAVA");
		isJSON = new BooleanRef();
		isJSON.data = true;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 423);
		getContentPane().setLayout(null);
		
		JTextArea txtRequest = new JTextArea();				
		JScrollPane sp = new JScrollPane(txtRequest);
		sp.setBounds(10, 104, 199, 270);
		getContentPane().add(sp);
		
		JTextArea txtResponse = new JTextArea();
		JScrollPane sp2 = new JScrollPane(txtResponse);
		sp2.setBounds(219, 104, 199, 270);
		getContentPane().add(sp2);
		
		JLabel lblRequest = new JLabel("Request");
		lblRequest.setBounds(10, 79, 83, 14);
		getContentPane().add(lblRequest);
		
		JLabel lblResponse = new JLabel("Response");
		lblResponse.setBounds(219, 79, 103, 14);
		getContentPane().add(lblResponse);
		
		JButton btnPopulateXML = new JButton("Populate Request (XML)");
		btnPopulateXML.addActionListener(new PopulateRequestXMLActionListener( 
				txtRequest, isJSON));		
		btnPopulateXML.setBounds(10, 11, 199, 23);
		getContentPane().add(btnPopulateXML);
		
		JButton btnPopulateJSON = new JButton("Populate Request (JSON)");
		btnPopulateJSON.addActionListener(new PopulateRequestJSONActionListener( 
				txtRequest, isJSON));		
		btnPopulateJSON.setBounds(10, 45, 199, 23);
		getContentPane().add(btnPopulateJSON);		
		
		JButton btnSendRequest = new JButton("Send Request");
		btnSendRequest.addActionListener(new TriPOSMakeRequestActionListener( 
				"http://localhost:8080/api/v1/sale",
				txtRequest,
				txtResponse,
				new TriPOSConfiguration(pathToTriPOSConfig),
				isJSON));	
		
		btnSendRequest.setBounds(219, 11, 199, 23);
		getContentPane().add(btnSendRequest);
		
		JButton btnClear = new JButton("Clear Data");
		btnClear.addActionListener(new ClearFieldsActionListener(txtRequest, txtResponse));
		btnClear.setBounds(219, 45, 199, 23);
		getContentPane().add(btnClear);

	}
}
