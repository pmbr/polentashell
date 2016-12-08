package com.polenta.shell;

import com.polenta.shell.app.PolentaShellApp;
import com.polenta.shell.app.PolentaShellCLIApp;
import com.polenta.shell.app.PolentaShellGUIApp;

public class PolentaShell {
	
	public static void main(String[] args) throws Exception  {
		PolentaShellApp app;
		boolean gui = false;
		for (String arg: args) {
			if (arg.equalsIgnoreCase("--gui")) {
				gui = true;
				break;
			}
		}
		if (gui) {
			app = new PolentaShellGUIApp();
		} else {
			app = new PolentaShellCLIApp();
		}
		app.startApp(args);	
	}
	

}
