package bandwidthBroker;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Enzo
 *
 * Manage the current usage of the network
 */
public class NetworksManager {
	private ResourceBundle savedNetworks;
	private HashMap<String,Double> networksUsageBE;
	private HashMap<String,Double> networksUsagePREMIUM;
	
	public NetworksManager() {
		this.savedNetworks = ResourceBundle.getBundle("bandwidthBroker.Networks");
		this.networksUsageBE = new HashMap<String,Double>();
		this.networksUsagePREMIUM = new HashMap<String,Double>();
		
		for(int i=1 ; i<this.savedNetworks.keySet().size() ; i++) {
			this.networksUsageBE.put(this.savedNetworks.getString(String.valueOf(i)), (double)0);
		}
		
		for(int i=1 ; i<this.savedNetworks.keySet().size() ; i++) {
			this.networksUsagePREMIUM.put(this.savedNetworks.getString(String.valueOf(i)), (double)0);
		}
	}
	
	public double getUsageBE(String network) {
		return this.networksUsageBE.get(network);
	}
	
	public double getUsagePREMIUM(String network) {
		return this.networksUsagePREMIUM.get(network);
	}
	
	public void addUsageBE(String network, double asked) {
		this.networksUsageBE.replace(network, this.networksUsageBE.get(network)+asked);
	}
	
	public void addUsagePREMIUM(String network, double asked) {
		this.networksUsagePREMIUM.replace(network, this.networksUsagePREMIUM.get(network)+asked);
	}
	
	public void subUsageBE(String network, double asked) {
		this.networksUsageBE.replace(network, this.networksUsageBE.get(network)-asked);
	}
	
	public void subUsagePREMIUM(String network, double asked) {
		this.networksUsagePREMIUM.replace(network, this.networksUsagePREMIUM.get(network)-asked);
	}
	
}
