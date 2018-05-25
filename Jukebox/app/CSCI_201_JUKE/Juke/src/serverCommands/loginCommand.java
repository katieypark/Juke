package serverCommands;

import server.DatabaseConnector;

public class loginCommand extends ServerCommand {

	private static final long serialVersionUID = 4898896471224568719L;

	private String username;
	private String password;
	
	public loginCommand(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	public ServerReply execute(DatabaseConnector dc) {
		boolean success = dc.login(username, password);
		return new ServerReply("Bool", success);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
