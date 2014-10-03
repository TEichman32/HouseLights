package com.jones.matt.house.lights.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
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

	public void onModuleLoad()
	{
		MGWT.applySettings(MGWTSettings.getAppSetting());
		myContent = new RootFlexPanel();
		for(int ai = 0; ai < getLightData().size(); ai++)
		{
			generateRow(getLightData().getLabel(ai), "Off", getHandler(ai, "off"), "On", getHandler(ai, "on"));
		}
		myContent.add(new GarageDoorButton());
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
	private void generateRow(String theLabel, String theLabel1, TapHandler theTapHandler1, String theLabel2, TapHandler theTapHandler2)
	{
		FlexPanel aButtonHolder = new FlexPanel();
		aButtonHolder.setOrientation(FlexPropertyHelper.Orientation.HORIZONTAL);
		Button button = new Button(theLabel);
		button.setImportant(true);
		button.addStyleName("button-grow");
		button.addTapHandler(theTapHandler1);
		aButtonHolder.add(button);
		button = new Button(theLabel2);
		button.setConfirm(true);
		button.addStyleName("button-grow");
		button.addTapHandler(theTapHandler2);
		aButtonHolder.add(button);
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
				new DefaultRequestBuilder(getLightData().getUrl(theIndex, theOperation)).send();
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
}
