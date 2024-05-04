package bandwidthBroker;

/**
 * @author Enzo
 *
 * Compute the queries for resource allocation and release
 */
public class QueryAlgorithm {
	private SLAManager slas;
	private NetworksManager networks;
	
	public QueryAlgorithm() {
		slas = new SLAManager();
		networks = new NetworksManager();
	}
	
	public boolean isFreeBE(String network, double asked) {
		return Double.valueOf(this.networks.getUsageBE(network)) + asked < this.slas.getBandwidthBE(network);
	}
	
	public boolean isFreePREMIUM(String network, double asked) {
		return Double.valueOf(this.networks.getUsagePREMIUM(network)) + asked < this.slas.getBandwidthPREMIUM(network);
	}
	
	/**
	 * Try to allocate resources if possible
	 * Type of services are BE (Best effort) and PREMIUM
	 * 
	 * @param emitterNetwork
	 * @param receiverNetwork
	 * @param type
	 * @param asked
	 * @throws NotEnoughResourcesException
	 */
	public void allocateResources(String emitterNetwork, String receiverNetwork,
			String type, double asked) throws NotEnoughResourcesException {
		if(type.equals("BE")) {
			if(this.isFreeBE(emitterNetwork, asked) && this.isFreeBE(receiverNetwork, asked)) {
				this.networks.addUsageBE(emitterNetwork, asked);
				this.networks.addUsageBE(receiverNetwork, asked);
			} else {
				throw new NotEnoughResourcesException();
			}
		} else if(type.equals("PREMIUM")) {
			if(this.isFreePREMIUM(emitterNetwork, asked) && this.isFreePREMIUM(receiverNetwork, asked)) {
				this.networks.addUsagePREMIUM(emitterNetwork, asked);
				this.networks.addUsagePREMIUM(receiverNetwork, asked);
			} else {
				throw new NotEnoughResourcesException();
			}
		}
	}
	
	/**
	 * Try to release resources if possible
	 * Type of services are BE (Best effort) and PREMIUM
	 * 
	 * @param emitterNetwork
	 * @param receiverNetwork
	 * @param type
	 * @param asked
	 * @throws WrongReleaseQueryException
	 */
	public void releaseResources(String emitterNetwork, String receiverNetwork, 
			String type, double asked) throws WrongReleaseQueryException {
		if(type.equals("BE")) {
			if(this.networks.getUsageBE(emitterNetwork)>0 && this.networks.getUsageBE(receiverNetwork)>0) {
				this.networks.subUsageBE(emitterNetwork, asked);
				this.networks.subUsageBE(receiverNetwork, asked);
			} else {
				throw new WrongReleaseQueryException();
			}
		} else if(type.equals("PREMIUM")) {
			if(this.networks.getUsagePREMIUM(emitterNetwork)>0 && this.networks.getUsagePREMIUM(receiverNetwork)>0) {
				this.networks.subUsagePREMIUM(emitterNetwork, asked);
				this.networks.subUsagePREMIUM(receiverNetwork, asked);
			} else {
				throw new WrongReleaseQueryException();
			}
		}
	}
}
