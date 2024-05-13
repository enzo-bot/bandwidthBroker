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
	private HashMap<String,Double> networksUsageBE_up;
	private HashMap<String,Double> networksUsageBE_down;
	private HashMap<String,Double> networksUsagePREMIUM_up;
	private HashMap<String,Double> networksUsagePREMIUM_down;
	
	public NetworksManager() {
		this.savedNetworks = ResourceBundle.getBundle("bandwidthBroker.Networks");
		this.networksUsageBE_up = new HashMap<String,Double>();
		this.networksUsageBE_down = new HashMap<String,Double>();
		this.networksUsagePREMIUM_up = new HashMap<String,Double>();
		this.networksUsagePREMIUM_down = new HashMap<String,Double>();
		
		// Initialization of the usage variables
		for(int i=1 ; i<this.savedNetworks.keySet().size() ; i++) {
			this.networksUsageBE_up.put(this.savedNetworks.getString(String.valueOf(i)), (double)0);
			this.networksUsageBE_down.put(this.savedNetworks.getString(String.valueOf(i)), (double)0);
		}
		
		for(int i=1 ; i<this.savedNetworks.keySet().size() ; i++) {
			this.networksUsagePREMIUM_up.put(this.savedNetworks.getString(String.valueOf(i)), (double)0);
			this.networksUsagePREMIUM_down.put(this.savedNetworks.getString(String.valueOf(i)), (double)0);
		}
	}
	
	public double getUsageBE_up(String network) {
		return this.networksUsageBE_up.get(network);
	}
	
	public double getUsageBE_down(String network) {
		return this.networksUsageBE_down.get(network);
	}
	
	public double getUsagePREMIUM_up(String network) {
		return this.networksUsagePREMIUM_up.get(network);
	}
	
	public double getUsagePREMIUM_down(String network) {
		return this.networksUsagePREMIUM_down.get(network);
	}
	
	public void addUsageBE(String network, double asked) {
		this.networksUsageBE_up.replace(network, this.networksUsageBE_up.get(network)+asked);
		this.networksUsageBE_down.replace(network, this.networksUsageBE_down.get(network)+asked);
	}
	
	public void addUsagePREMIUM(String network, double asked) {
		this.networksUsagePREMIUM_up.replace(network, this.networksUsagePREMIUM_up.get(network)+asked);
		this.networksUsagePREMIUM_down.replace(network, this.networksUsagePREMIUM_down.get(network)+asked);
	}
	
	public void subUsageBE(String network, double asked) {
		this.networksUsageBE_up.replace(network, this.networksUsageBE_up.get(network)-asked);
		this.networksUsageBE_down.replace(network, this.networksUsageBE_down.get(network)-asked);
	}
	
	public void subUsagePREMIUM(String network, double asked) {
		this.networksUsagePREMIUM_up.replace(network, this.networksUsagePREMIUM_up.get(network)-asked);
		this.networksUsagePREMIUM_down.replace(network, this.networksUsagePREMIUM_down.get(network)-asked);
	}
}
