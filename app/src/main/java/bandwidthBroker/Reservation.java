package bandwidthBroker;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Reservation {

	static String passwd = "7nains";
	static String user = "root";
	static String CE1 = "193.168.1.254";
	static String CE2 = "193.168.2.254";

	public void reservationJSchBoth(String ipSource, String ipDest){
		this.reservationJSch(CE2, ipDest, ipSource);
		this.reservationJSch(CE1, ipSource, ipDest);

	}
	public void desalouerJSchBoth(String ipSource, String ipDest){
		this.desalouerJSch(CE1, ipSource, ipDest);
		this.desalouerJSch(CE2, ipDest, ipSource);
	}

	

	public void reservationJSch(String ipRouteur, String ipSource, String ipDest){
			Session session = null;
			ChannelExec channel = null;
			// String cmd1 =  "tc class add dev eth1 parent 1:1 classid 1:1" + idFile + " htb " +
			// 				" rate " + Communication.COMMUNICATION_BW.toString() + " ceil " + Communication.COMMUNICATION_BW.toString();
			// String cmd2 = "tc filter add dev eth1 parent 1:0 protocol ip prio 1 handle " + idFile +  //TODO vérifier que l'interface est bien eth0
			// 				" fw flowid 1:1" + idFile;
			// String cmd3 = "iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
			// 				" -j MARK --set-mark " + idFile;
			// String cmd4 = "iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
			// 				" -j TOS --set-tos " + Communication.TOS_PREMIUM.toString();
			//String cmd5 = "exit";
		  //String cmd = cmd1 + " ; " + cmd2 + " ; " + cmd3 + " ; " + cmd4 + " ; " + cmd5;
		  String cmd1 = "iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp  -j MARK --set-mark 11 ";
		  String cmd2 = "iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp  -j DSCP --set-dscp 46 ";
			String cmd3 = "echo '" + cmd1 + "' >> BB_cmd";
			String cmd4 = "echo '" + cmd2 + "' >> BB_cmd";
			try {
					session = new JSch().getSession("root", ipRouteur, 22);
					session.setPassword("7nains");
					session.setConfig("StrictHostKeyChecking", "no");
					session.connect();
					System.out.println("Succesfully connected to " + ipRouteur);
					System.out.println("Connected");
					channel = (ChannelExec) session.openChannel("exec");
					System.out.println("Channel created");
					channel.setCommand(cmd1 + ";" + cmd2 + ";" + cmd3 + ";" + cmd4 + ";");
					System.out.println("Command Ready");
					ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
					channel.setOutputStream(responseStream);
					channel.connect();
					
					while (channel.isConnected()) {
							Thread.sleep(100);
					}
					String responseString = new String(responseStream.toByteArray());
					System.out.println(responseString);
					System.out.println("Channel connect completed");
					System.out.println("Executing command: " + cmd1 + ";" + cmd2 + ";" + cmd3 + ";" + cmd4 + ";");

			}catch (Exception e){
					e.printStackTrace();
			}finally {
					if (session != null) {
							session.disconnect();
							System.out.println("Deconnexion");
					}
					if (channel != null) {
							channel.disconnect();
							System.out.println("Fermeture channel");
					}
			}
	}

	public void desalouerJSch(String ipRouteur, String ipSource, String ipDest){
			Session session = null;
			ChannelExec channel = null;
			// String cmd1 = "tc filter delete dev eth1 parent 1:0 protocol ip prio 1 handle " + idFile +
			// 				" fw flowid 1:1" + idFile;
			// String cmd2 = "tc class delete dev eth1 parent 1:1 classid 1:1" + idFile + " htb " +
			// 				" rate " + Communication.COMMUNICATION_BW.toString() + " ceil " + Communication.COMMUNICATION_BW.toString();
			// String cmd3 = "iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
			// 				" -j MARK --set-mark " + idFile;
			// String cmd4 = "iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
			// 				" -j TOS --set-tos " + Communication.TOS_PREMIUM.toString();
			// String cmd5 = "exit";
		  //String cmd = cmd1 + " ; " + cmd2 + " ; " + cmd3 + " ; " + cmd4 + " ; " + cmd5;
		  String cmd1 = "iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp  -j MARK --set-mark 11 ";
		  String cmd2 = "iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp  -j DSCP --set-dscp 46 ";
			String cmd3 = "echo '" + cmd1 + "' >> BB_cmd";
			String cmd4 = "echo '" + cmd2 + "' >> BB_cmd";
			try {
					session = new JSch().getSession("root", ipRouteur, 22);
					session.setPassword("7nains");
					session.setConfig("StrictHostKeyChecking", "no");
					session.connect();
					System.out.println("Succesfully connected to " + ipRouteur);
					System.out.println("Connected");
					channel = (ChannelExec) session.openChannel("exec");
					System.out.println("Channel created");
					channel.setCommand(cmd1 + ";" + cmd2 + ";" + cmd3 + ";" + cmd4 + ";");
					channel.connect();
			    while (channel.isConnected()) {
						Thread.sleep(100);
					}
					System.out.println("Execution command completed");
					System.out.println("Executing command: " + cmd1 + ";" + cmd2 + ";" + cmd3 + ";" + cmd4 + ";");
			}catch (Exception e){
					e.printStackTrace();
			}finally {
					if (session != null) {
							session.disconnect();
							System.out.println("Deconnexion");
					}
					if (channel != null) {
							channel.disconnect();
							System.out.println("Fermeture channel");
					}
			}
	}
}
