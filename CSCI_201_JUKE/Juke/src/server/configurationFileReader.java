package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class configurationFileReader {
	
	private String hostname;
	private int port;
	
	public configurationFileReader() {
		hostname = null;
		port = -1;
	}
	
	public void readConfig(String filepath) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(filepath);
			br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null) {
				line.trim(); //removes trailing and leading whitespace
				if (line.startsWith("#")) {
					// ignore the comment
				}
				else if (line.startsWith("port")) {
					// receive port number
					StringTokenizer st = new StringTokenizer(line, "|");
					st.nextToken(); // description, "port"
					port = Integer.parseInt(st.nextToken());

				}
				else if (line.startsWith("ip")) {
					// receive ip address
					StringTokenizer st = new StringTokenizer(line, "|");
					st.nextToken(); // description, "ip"
					hostname = st.nextToken();
				}
				else if (line.length() == 0) {
					// ignore blank line
				}
				else {
					System.out.println("Unrecognized line in configuration file: " + line);
				}

				line = br.readLine();
			}
		} catch (FileNotFoundException fnfe) {
			System.out.println("FNFE while reading configuration file: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("IOE while reading configuration file: " + ioe.getMessage());
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException ioe) {
				System.out.println("IOE: " + ioe.getMessage());
			}
		}
	}
	
	// returns -1 if not found in config or readConfig not called first
	public int getPort() {
		return port;
	}
	
	// returns null if not found in config or readConfig not called first
	public String getHostname() {
		return hostname;
	}
}