package com.messagealerter.service;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.messagealerter.R;
import com.messagealerter.activity.AcknowledgeAlert;
import com.messagealerter.activity.Rules;
import com.messagealerter.data.Alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class AlertThread extends Thread {
	
	private static final int NOTIFICATION = 100;
	
	private final Context mContext;
	private final MediaPlayer mMediaPlayer;
	private final NotificationManager mNotificationManager;
	private final Notification mNotification;
	
	//synchronized
	private final LinkedBlockingQueue<Alert> mAlerts = new LinkedBlockingQueue<Alert>();
	
	//synchronized
	private final List<AlertsWatcher> mAlertsWatchers = new LinkedList<AlertsWatcher>();
	
	//synchronized by this
	private volatile Alert mPlayingAlert = null;
		
	public AlertThread(Context context) {
		super("AlertThread");
		mContext = context;
		mNotification = new Notification(R.drawable.icon,context.getString(R.string.notification_running_ticker),
				System.currentTimeMillis());
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mMediaPlayer = new MediaPlayer();
	}

	@Override
	public void run() {
		
		try {
			
			while (true) {
				
				Alert alert = mAlerts.take();
				
				playRingtone(alert);
				
				updateNotification();
				
				synchronized (mNotification) {
					try {
						mNotification.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			
			mNotificationManager.cancel(NOTIFICATION);
			
		}
	}
	
	public void feedAlert(Alert alert) {
		synchronized (mAlerts) {
			mAlerts.offer(alert);
			notifyWatchers();
		}
	}
	
	public void acknowledgeAlert(int alert) {

		synchronized (mAlerts) {
			for (Alert a : getQueuedAlerts()) {
				if (a.getId() == alert) {
					
					synchronized (this) {
						if (a.equals(mPlayingAlert)) {
							mMediaPlayer.reset();
							mPlayingAlert = null;
							synchronized (mNotification) {
								mNotification.notify();
							}
						}
					}
					
					mAlerts.remove(a);
					notifyWatchers();
	
					updateNotification();
					return;
				}
				
			}
		}

	}

	private void playRingtone(Alert alert) {
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(mContext, alert.getRingtone());
			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			synchronized (this) {
				mPlayingAlert = alert;
			}
		}
	}
	
	private void updateNotification() {
		
		int size;
		synchronized (mAlerts) {
			size = mAlerts.size();
		}
		
		synchronized (this) {
			if (mPlayingAlert != null) size++;
		}
		
		
		if (size == 0) {
			
			PendingIntent notificationIntent = PendingIntent.getActivity(mContext, 0,
					new Intent(mContext, Rules.class), 0);
			
			mNotification.setLatestEventInfo(mContext, mContext.getString(R.string.notification_running_title),
					mContext.getString(R.string.notification_running_text), notificationIntent);
			
		} else {
			
			PendingIntent notificationIntent = PendingIntent.getActivity(mContext, 0,
					new Intent(mContext, AcknowledgeAlert.class), 0);
			
			mNotification.setLatestEventInfo(mContext, mContext.getString(R.string.notification_alert_title),
					mContext.getString(R.string.notification_alert_text).replace("#", String.valueOf(size)),
					notificationIntent);
			
		}
		
		mNotificationManager.notify(NOTIFICATION, mNotification);
		
	}
	
	// call this only from synchronized by mAlerts code
	private List<Alert> getQueuedAlerts() {
		List<Alert> alerts = new LinkedList<Alert>();
		
		synchronized (this) {
			if (mPlayingAlert != null)
				alerts.add(mPlayingAlert);
		}

		alerts.addAll(mAlerts);
		
		return alerts;
	}
	
	// call this only from synchronized by mAlerts code
	private void notifyWatchers() {
		synchronized (mAlertsWatchers) {
			
			if (!mAlertsWatchers.isEmpty()) {
				
				List<Alert> alerts = Collections.unmodifiableList(getQueuedAlerts());
				
				for (AlertsWatcher watcher : mAlertsWatchers)
					watcher.onAlertsChanged(alerts);
				
			}
			
		}
	}
	
	public void registerWatcher(AlertsWatcher watcher) {
		synchronized (mAlertsWatchers) {
			mAlertsWatchers.add(watcher);
		}
		synchronized (mAlerts) {
			watcher.onAlertsChanged(getQueuedAlerts());
		}
	}
	
	public void unregisterWatcher(AlertsWatcher watcher) {
		synchronized (mAlertsWatchers) {
			mAlertsWatchers.remove(watcher);		
		}		
	}
	
}
