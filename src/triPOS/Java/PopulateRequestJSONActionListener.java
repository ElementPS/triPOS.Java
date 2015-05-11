package triPOS.Java;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

public class PopulateRequestJSONActionListener implements ActionListener{
	    private JTextArea txtRequest;
	    private BooleanRef isJSON;

	    public PopulateRequestJSONActionListener(JTextArea txtRequest, BooleanRef isJSON) {
	        super();
	        this.txtRequest = txtRequest;
	        isJSON.data = true;	     
	        this.isJSON = isJSON;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
			
			try { 
				this.isJSON.data = true;
				SaleRequest saleRequest = new SaleRequest();
				saleRequest = saleRequest.GetSaleRequest();
				String jsonRequest = saleRequest.SaleRequestToJSON(saleRequest);
				this.txtRequest.setText(jsonRequest);
				} 
				catch (Exception ex) { 
					this.txtRequest.setText(ex.getMessage());
				}
	    }
}
