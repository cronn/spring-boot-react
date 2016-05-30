package de.cronn.demo.react.model;

public class DemoResponse {
	public String stringEntry = "value1";
	public int intEntry = 12345;
	public String[] arrayEntry = {"red", "green", "blue"};
	public LogMessage objectValue = new LogMessage(0, "Log message", "Things happen.");
}
