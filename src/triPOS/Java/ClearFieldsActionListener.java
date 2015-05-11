package triPOS.Java;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

public class ClearFieldsActionListener implements ActionListener{
	    private JTextArea txtRequest;
	    private JTextArea txtResponse;

	    public ClearFieldsActionListener(JTextArea txtRequest, JTextArea txtResponse) {
	        super();
	        this.txtRequest = txtRequest;
	        this.txtResponse = txtResponse;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
			
			try { 
				this.txtRequest.setText("");
				this.txtResponse.setText("");
				} 
				catch (Exception ex) { 
				}
	    }
}
