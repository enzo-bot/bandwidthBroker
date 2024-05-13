package bandwidthBroker;

public class Main {
	
	public static void main(String[] args) {
		QueryAlgorithm algo = new QueryAlgorithm();
		
		try {
			System.out.println("Trying to allocate resources");
			algo.allocateResources("10.0.0.0/8","192.0.0.0/24","BE", 0.5);
			algo.allocateResources("10.0.0.0/8","192.0.0.0/24","BE", 0.5);
			algo.allocateResources("10.0.0.0/8","192.0.0.0/24","BE", 4); // Too much
		} catch (NotEnoughResourcesException e) {
			System.out.println("Some allocations didn't worked");
		}

		try {
			System.out.println("Trying to release resources");
			algo.releaseResources("10.0.0.0/8","192.0.0.0/24","BE", 0.5);
			algo.releaseResources("10.0.0.0/8","192.0.0.0/24","BE", 2); // Too much
		} catch (WrongReleaseQueryException e) {
			System.out.println("Some release queries didn't worked");
		}
	}
	
}
