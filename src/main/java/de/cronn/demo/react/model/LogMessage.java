package de.cronn.demo.react.model;

/**
 * Sample POJO
 *
 * @author Hanno Fellmann, cronn GmbH
 */
public class LogMessage {
	public final String title, message;
	public final int id;

	public LogMessage(int id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}
}
