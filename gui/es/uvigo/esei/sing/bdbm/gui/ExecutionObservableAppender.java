package es.uvigo.esei.sing.bdbm.gui;

import java.util.List;
import java.util.Observable;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;

public class ExecutionObservableAppender extends Observable implements Appender<ILoggingEvent> {
	private final AppenderBase<ILoggingEvent> delegatedAppender;
	
	public ExecutionObservableAppender() {
		this.delegatedAppender = new AppenderBase<ILoggingEvent>() {
			protected void append(ILoggingEvent eventObject) {
				ExecutionObservableAppender.this.notifyChange(eventObject.getFormattedMessage());
			}
		};
	}
	
	protected synchronized void notifyChange(String message) {
		this.setChanged();
		this.notifyObservers(message);
	}
	
	@Override
	public void start() {
		this.delegatedAppender.start();
	}

	@Override
	public void stop() {
		this.delegatedAppender.stop();
	}

	@Override
	public boolean isStarted() {
		return this.delegatedAppender.isStarted();
	}

	@Override
	public void setContext(Context context) {
		this.delegatedAppender.setContext(context);
	}

	@Override
	public Context getContext() {
		return this.delegatedAppender.getContext();
	}

	@Override
	public void addStatus(Status status) {
		this.delegatedAppender.addStatus(status);
	}

	@Override
	public void addInfo(String msg) {
		this.delegatedAppender.addInfo(msg);
	}

	@Override
	public void addInfo(String msg, Throwable ex) {
		this.delegatedAppender.addInfo(msg, ex);
	}

	@Override
	public void addWarn(String msg) {
		this.delegatedAppender.addWarn(msg);
	}

	@Override
	public void addWarn(String msg, Throwable ex) {
		this.delegatedAppender.addWarn(msg, ex);
	}

	@Override
	public void addError(String msg) {
		this.delegatedAppender.addError(msg);
	}

	@Override
	public void addError(String msg, Throwable ex) {
		this.delegatedAppender.addError(msg, ex);
	}

	@Override
	public void addFilter(Filter<ILoggingEvent> newFilter) {
		this.delegatedAppender.addFilter(newFilter);
	}

	@Override
	public void clearAllFilters() {
		this.delegatedAppender.clearAllFilters();
	}

	@Override
	public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
		return this.delegatedAppender.getCopyOfAttachedFiltersList();
	}

	@Override
	public FilterReply getFilterChainDecision(ILoggingEvent event) {
		return this.delegatedAppender.getFilterChainDecision(event);
	}

	@Override
	public String getName() {
		return this.delegatedAppender.getName();
	}

	@Override
	public void doAppend(ILoggingEvent event) throws LogbackException {
		this.delegatedAppender.doAppend(event);
	}

	@Override
	public void setName(String name) {
		this.delegatedAppender.setName(name);
	}
}
