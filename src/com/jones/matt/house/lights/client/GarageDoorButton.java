package com.jones.matt.house.lights.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;

/**
 * Button for garage door, displays Open or Closed based on status.  Queries server
 * for status every 5 seconds to update button text if open or closed.
 */
public class GarageDoorButton extends Button implements TapHandler
{
	public static final int kDelay = 5000;

	public GarageDoorButton()
	{
		super("Open");
		addTapHandler(this);
		new StatusTimer().run();
	}

	public void onTap(TapEvent theEvent)
	{
		String aUrl = getText().equals("Close") ? getClose() : getOpen();
		new DefaultRequestBuilder(aUrl).send();
	}

	/**
	 * Callback sets button text, schedules timer to check for new status
	 */
	private class StatusCallback implements RequestCallback
	{
		public void onResponseReceived(Request theRequest, Response theResponse)
		{
			setText(theResponse.getText().equals("false") ? "Open" : "Close");
			new StatusTimer().schedule(kDelay);
		}

		public void onError(Request theRequest, Throwable theException)
		{
			new StatusTimer().schedule(kDelay);
		}
	}

	/**
	 * Timer makes RPC to check status, calls our callback
	 */
	private class StatusTimer extends Timer
	{
		@Override
		public void run()
		{
			new DefaultRequestBuilder(getStatus())
			{
				protected RequestCallback createCallback()
				{
					return new StatusCallback();
				}
			}.send();
		}
	}

	private static native String getClose() /*-{
		return $wnd.CloseUrl;
	}-*/;

	private static native String getOpen() /*-{
		return $wnd.OpenUrl;
	}-*/;

	private static native String getStatus() /*-{
		return $wnd.StatusUrl;
	}-*/;
}
