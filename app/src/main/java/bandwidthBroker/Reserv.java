// package bandwidthBroker;
//
// import BandwithBroker.com.Communication;
// import com.jcraft.jsch.ChannelExec;
// import com.jcraft.jsch.JSch;
// import com.jcraft.jsch.Session;
//
// import java.io.BufferedReader;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.io.OutputStream;
// import java.nio.charset.StandardCharsets;
//
// public class Reservation {
//
//     Runtime shell = Runtime.getRuntime();
//     private Reservation(){}
//
//     private static Reservation instance = new Reservation();
//
//
//     public static Reservation getInstance() {
//         return instance;
//     }
//
//
//     public void reserver(String ipRouteur, String ipSource, String ipDest, String portDest, String idFile){
//         OutputStream out;
//         InputStream in;
//         String identity = "root@" +  ipRouteur;
//         try{
//             Process process = shell.exec(new String[]{"ssh",identity});
//             out = process.getOutputStream();
//             System.out.println("Connected");
//             //out.write("yes".getBytes(StandardCharsets.UTF_8));
//             out.write(("tc class add dev eth1 parent 1:1 classid 1:1" + idFile + " htb " +
//                     " rate " + Communication.COMMUNICATION_BW.toString() + " ceil " + Communication.COMMUNICATION_BW.toString()).getBytes(StandardCharsets.UTF_8));
//             out.write(("tc filter add dev eth1 parent 1:0 protocol ip prio 1 handle " + idFile +  //TODO vérifier que l'interface est bien eth0
//                      " fw flowid 1:1" + idFile).getBytes(StandardCharsets.UTF_8));
//             System.out.println("Stack added");
//             out.write(("iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " --dport " + portDest +
//                     " -j MARK --set-mark " + idFile + " --set-tos " + Communication.TOS_PREMIUM.toString()).getBytes(StandardCharsets.UTF_8));
//             System.out.println("Paramétrage communication");
//             out.write("exit".getBytes(StandardCharsets.UTF_8));
//             System.out.println("Connection closed");
//             //process.destroy(); //TODO voir si besoin de supprimer le process ou si kill tout seul
//         }catch(Exception e){
//             e.printStackTrace(); //TODO implémenter gestion exception
//         }
//     }
//
//     public void reservationJSch(String ipRouteur, String ipSource, String ipDest, String portDest, String idFile){
//         Session session = null;
//         ChannelExec channel = null;
//         String cmd1 =  "tc class add dev eth1 parent 1:1 classid 1:1" + idFile + " htb " +
//                 " rate " + Communication.COMMUNICATION_BW.toString() + " ceil " + Communication.COMMUNICATION_BW.toString();
//         String cmd2 = "tc filter add dev eth1 parent 1:0 protocol ip prio 1 handle " + idFile +  //TODO vérifier que l'interface est bien eth0
//                 " fw flowid 1:1" + idFile;
//         String cmd3 = "iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
//                 " -j MARK --set-mark " + idFile;
//         String cmd4 = "iptables -A PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
//                 " -j TOS --set-tos " + Communication.TOS_PREMIUM.toString();
//         String cmd5 = "exit";
//         try {
//             session = new JSch().getSession("root", ipRouteur, 22);
//             session.setPassword("7nains");
//             session.setConfig("StrictHostKeyChecking", "no");
//             session.connect();
//             System.out.println("Connected");
//             channel = (ChannelExec) session.openChannel("exec");
//             System.out.println("Channel created");
//             channel.setCommand(cmd1  + " ; " + cmd2 + " ; " + cmd3 + " ; " + cmd4 + " ; " + cmd5 );
//             System.out.println("Command Ready");
//             channel.connect();
//             System.out.println("Channel connect completed");
//         }catch (Exception e){
//             e.printStackTrace();
//         }finally {
//             if (session != null) {
//                 session.disconnect();
//                 System.out.println("Deconnexion");
//             }
//             if (channel != null) {
//                 channel.disconnect();
//                 System.out.println("Fermeture channel");
//             }
//         }
//     }
//
//     public void desalouerJSch(String ipRouteur, String ipSource, String ipDest, String portDest, String idFile){
//         Session session = null;
//         ChannelExec channel = null;
//         String cmd1 = "tc filter delete dev eth1 parent 1:0 protocol ip prio 1 handle " + idFile +
//                 " fw flowid 1:1" + idFile;
//         String cmd2 = "tc class delete dev eth1 parent 1:1 classid 1:1" + idFile + " htb " +
//                 " rate " + Communication.COMMUNICATION_BW.toString() + " ceil " + Communication.COMMUNICATION_BW.toString();
//         String cmd3 = "iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
//                 " -j MARK --set-mark " + idFile;
//         String cmd4 = "iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " -p udp --dport " + portDest +
//                 " -j TOS --set-tos " + Communication.TOS_PREMIUM.toString();
//         String cmd5 = "exit";
//         try {
//             session = new JSch().getSession("root", ipRouteur, 22);
//             session.setPassword("7nains");
//             session.setConfig("StrictHostKeyChecking", "no");
//             session.connect();
//             System.out.println("Connected");
//             channel = (ChannelExec) session.openChannel("exec");
//             System.out.println("Channel created");
//             channel.setCommand(cmd1 + " ; " + cmd2 + " ; " + cmd3 + " ; " + cmd4 + " ; " + cmd5);
//             channel.connect();
//             System.out.println("Execution command completed");
//         }catch (Exception e){
//             e.printStackTrace();
//         }finally {
//             if (session != null) {
//                 session.disconnect();
//                 System.out.println("Deconnexion");
//             }
//             if (channel != null) {
//                 channel.disconnect();
//                 System.out.println("Fermeture channel");
//             }
//         }
//     }
//
//     public void desalouer(String ipRouteur, String ipSource, String ipDest, String portDest, String idFile){ //TODO Tester si la suppresion iptables et file fonctionne
//         OutputStream out;
//         try {
//             Process process = shell.exec(new String[]{"ssh", ipRouteur});
//             out = process.getOutputStream();
//             out.write("yes".getBytes(StandardCharsets.UTF_8));
//             out.write(("tc filter delete dev eth0 parent 1:0 protocol ip prio 1 handle " + idFile +
//                     " fw flowid 1:1" + idFile).getBytes(StandardCharsets.UTF_8));
//             out.write(("tc class delete dev eth0 parent 1:1 classid 1:1" + idFile + " htb " + //TODO vérifier que l'interface est bien eth0
//                     " rate " + Communication.COMMUNICATION_BW.toString() + " ceil " + Communication.COMMUNICATION_BW.toString()).getBytes(StandardCharsets.UTF_8));
//             out.write(("iptables -D PREROUTING -t mangle -s " + ipSource + " -d " + ipDest + " --dport " + portDest +
//                     " -j MARK --set-mark " + idFile + " --set-tos " + Communication.TOS_PREMIUM.toString()).getBytes(StandardCharsets.UTF_8));
//             out.write("exit".getBytes(StandardCharsets.UTF_8));
//             //process.destroy(); //TODO voir si besoin de supprimer le process ou si kill tout seul
//         }catch (Exception e){
//             e.printStackTrace(); //TODO implémenter gestion exception
//         }
//     }
// }
