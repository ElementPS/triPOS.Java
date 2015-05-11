package triPOS.Java;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;

public class PopulateRequestXMLActionListener implements ActionListener{
	    private JTextArea txtRequest;
	    private BooleanRef isJSON;

	    public PopulateRequestXMLActionListener(JTextArea txtRequest, BooleanRef isJSON) {
	        super();
	        this.txtRequest = txtRequest;
	        this.isJSON = isJSON;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
			
			try { 
				this.isJSON.data = false;
				SaleRequest saleRequest = new SaleRequest();
				saleRequest = saleRequest.GetSaleRequest();
				String xmlRequest = saleRequest.SaleRequestToXML(saleRequest);
				this.txtRequest.setText(xmlRequest);
				} 
				catch (Exception ex) {  
					this.txtRequest.setText(ex.getMessage());
				}
	    }
}
