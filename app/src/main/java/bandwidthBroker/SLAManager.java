package bandwidthBroker;

import java.util.ResourceBundle;

/**
 * @author Enzo
 *
 * Manage the SLAs in SLAs.properties file
 */
public class SLAManager{
	
	private ResourceBundle slas;
	
	public SLAManager() {
		this.slas = ResourceBundle.getBundle("bandwidthBroker.SLAs");
	}
	
	public String getRouter(String network) {
		String query = "Network_" + network;
		return this.slas.getString(query);
	}
	
	public int getBandwidthBE_up(String network) {
		String query = "bandwidthBE_" + network + "_up";
		return Integer.valueOf(this.slas.getString(query));
	}
	
	public int getBandwidthBE_down(String network) {
		String query = "bandwidthBE_" + network + "_down";
		return Integer.valueOf(this.slas.getString(query));
	}
	
	public int getBandwidthPREMIUM_up(String network) {
		String query = "bandwidthPREMIUM_" + network;
		return Integer.valueOf(this.slas.getString(query));
	}
	
	public int getBandwidthPREMIUM_down(String network) {
		String query = "bandwidthPREMIUM_" + network + "_down";
		return Integer.valueOf(this.slas.getString(query));
	}
}
