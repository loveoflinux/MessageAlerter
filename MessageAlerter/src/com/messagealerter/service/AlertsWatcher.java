package com.messagealerter.service;

import java.util.List;

import com.messagealerter.data.Alert;

public interface AlertsWatcher {
	
	public void onAlertsChanged(List<Alert> alerts);

}
