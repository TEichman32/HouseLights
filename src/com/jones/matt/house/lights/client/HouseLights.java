package com.jones.matt.house.lights.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapEvent;
import com.googlecode.mgwt.dom.client.recognizer.longtap.LongTapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPanel;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPropertyHelper;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;

/**
 * Control X10 house lights via Ajax call to external REST server
 */
public class HouseLights implements EntryPoint
{
	private RootFlexPanel myContent;

	private long myTime = -1;

	public void onModuleLoad()
	{
		MGWT.applySettings(MGWTSettings.getAppSetting());
		myContent = new RootFlexPanel();
		for(int ai = 0; ai < getLightData().size(); ai++)
		{
			generateRow(ai);
		}
		myContent.add(new GarageDoorButton());
		myContent.add(new WeatherLabel());
		RootPanel.get().add(myContent);
	}

	/**
	 * Generate a single row of controls
	 *
	 * @param theLabel label for the row
	 * @param theLabel1 label for the first button
	 * @param theTapHandler1 tap handler for the first button
	 * @param theLabel2 label for the second button
	 * @param theTapHandler2 tap handler for the second button
	 */
	private void generateRow(final int theIndex)
	{
		FlexPanel aButtonHolder = new FlexPanel();
		aButtonHolder.setOrientation(FlexPropertyHelper.Orientation.HORIZONTAL);
		Button aButton = new Button(getLightData().getLabel(theIndex));
		aButton.setImportant(true);
		aButton.addStyleName("button-grow");
		aButton.addLongTapHandler(new LongTapHandler()
		{
			public void onLongTap(LongTapEvent event)
			{
				new DefaultRequestBuilder(getLightData().getUrl(theIndex, "movie")).send();
				myTime = System.currentTimeMillis();

			}
		});
		aButton.addTapHandler(getHandler(theIndex, "off"));
		aButtonHolder.add(aButton);
		aButton = new Button("On");
		aButton.setConfirm(true);
		aButton.addStyleName("button-grow");
		aButton.addTapHandler(getHandler(theIndex, "on"));
		aButtonHolder.add(aButton);
		myContent.add(aButtonHolder);
	}

	/**
	 * Get a tap handler that will make a ajax call to turn light on/off
	 *
	 * @param theIndex
	 * @param theOperation [on|off]
	 * @return
	 */
	private TapHandler getHandler(final int theIndex, final String theOperation)
	{
		return new TapHandler()
		{
			public void onTap(TapEvent event)
			{
				if (myTime < System.currentTimeMillis() - 4000)
				{
					new DefaultRequestBuilder(getLightData().getUrl(theIndex, theOperation)).send();
				}
				myTime = -1;
			}
		};
	}

	/**
	 * Get the data from our html page (set there for easier changing w/o recompile
	 *
	 * @return
	 */
	private static native LightDataOverlay getLightData() /*-{
		return $wnd.LightData;
	}-*/;

	public static final native int getPollingDelay() /*-{
		return $wnd.PollDelay;
	}-*/;
}
