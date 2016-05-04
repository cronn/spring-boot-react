package de.cronn.demo.react.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.cronn.demo.react.model.LogMessage;

/**
 * Service providing a simple POJO
 *
 * @author Hanno Fellmann, cronn GmbH
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StatusService {
	private final AtomicInteger lowestComponent = new AtomicInteger(0);

	public StatusService() {
		new Timer().schedule(new TimerTask() {
			public void run() {
				lowestComponent.addAndGet(1);
			}
		}, 2000, 2000);
	}

	public List<LogMessage> listNewestLogs() {
		final ArrayList<LogMessage> ret = new ArrayList<LogMessage>();
		final int start = lowestComponent.get();
		IntStream
			.range(0, 5)
			.map(i -> ((5-i) + start))
			.mapToObj(id -> new LogMessage(id, "Log entry " + id, "Some really bad error occured. Please read this log carefully."))
			.forEach(ret::add);
		return ret;
	}
}
