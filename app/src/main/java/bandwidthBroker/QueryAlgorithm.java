package bandwidthBroker;

/**
 * @author Enzo
 *
 * Compute the queries for resource allocation and release
 */
public class QueryAlgorithm {
	private SLAManager slas;
	private NetworksManager networks;
	private Reservation reservation;
	
	public QueryAlgorithm() {
		slas = new SLAManager();
		networks = new NetworksManager();
		reservation = new Reservation();
	}
	
	public boolean isFreeBE(String network, double asked) {
		return Double.valueOf(this.networks.getUsageBE_up(network)) + asked <= this.slas.getBandwidthBE_up(network)
				&& Double.valueOf(this.networks.getUsageBE_down(network)) + asked <= this.slas.getBandwidthBE_down(network);
	}
	
	public boolean isFreePREMIUM(String network, double asked) {
		return Double.valueOf(this.networks.getUsagePREMIUM_up(network)) + asked <= this.slas.getBandwidthPREMIUM_up(network)
				&& Double.valueOf(this.networks.getUsagePREMIUM_down(network)) + asked <= this.slas.getBandwidthPREMIUM_down(network);
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
		// si C'est BE
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
				this.reservation.reservationJSchBoth(emitterNetwork, receiverNetwork);
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
			if(this.networks.getUsageBE_up(emitterNetwork)-asked>=0 
					&& this.networks.getUsageBE_down(emitterNetwork)-asked>=0
					&& this.networks.getUsageBE_up(receiverNetwork)-asked>=0
					&& this.networks.getUsageBE_down(receiverNetwork)-asked>=0) {
				this.networks.subUsageBE(emitterNetwork, asked);
				this.networks.subUsageBE(receiverNetwork, asked);
			} else {
				throw new WrongReleaseQueryException();
			}
		} else if(type.equals("PREMIUM")) {
			if(this.networks.getUsagePREMIUM_up(emitterNetwork)-asked>=0
					&& this.networks.getUsagePREMIUM_down(emitterNetwork)-asked>=0
					&& this.networks.getUsagePREMIUM_up(receiverNetwork)-asked>=0
					&& this.networks.getUsagePREMIUM_down(receiverNetwork)-asked>=0) {
				this.networks.subUsagePREMIUM(emitterNetwork, asked);
				this.networks.subUsagePREMIUM(receiverNetwork, asked);
				this.reservation.desalouerJSchBoth(emitterNetwork, receiverNetwork);
			} else {
				throw new WrongReleaseQueryException();
			}
		}
	}
}
