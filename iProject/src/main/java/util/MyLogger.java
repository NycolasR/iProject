package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * 
 * @author NPG
 *
 *	Esta classe é responsavel for formular e fazer registro de logs.
 *
 */
public class MyLogger {
	
	private static final Logger LOGGER = Logger.getLogger( MyLogger.class.getName() );

	public static synchronized Logger getInstance() {
//		setLogger(); TODO descomentar se for testar Logger
		return LOGGER;
	}
	
	private MyLogger() {
		
	}
	
	private static void setLogger(){ // Este metodo set configurações do Logger.
		LOGGER.setLevel(Level.FINE);

		try {
			FileHandler fhandler = new FileHandler("Log.xml");
			SimpleFormatter sformatter = new SimpleFormatter();
			fhandler.setFormatter(sformatter);
			LOGGER.addHandler(fhandler);

		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		} catch (SecurityException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
 
}
