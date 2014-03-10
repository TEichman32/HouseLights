package com.jones.matt.house.lights.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.widget.Button;

/**
 * Control X10 house lights via Ajax call to external REST server
 */
public class HouseLights implements EntryPoint
{
	private FlexTable myContent;

	public void onModuleLoad()
	{
		MGWT.applySettings(MGWTSettings.getAppSetting());
		myContent = new FlexTable();
		myContent.addStyleName("base");
		for(int ai = 0; ai < getLightData().size(); ai++)
		{
			generateRow(getLightData().getLabel(ai), "Off", getHandler(ai, "off"), "On", getHandler(ai, "on"));
		}
		setupGarageDoor();
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
		int aRow = myContent.getRowCount();
		myContent.setWidget(aRow, 0, new Label(theLabel));
		myContent.getFlexCellFormatter().setVerticalAlignment(aRow, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		myContent.getFlexCellFormatter().setWidth(aRow, 0, "1%");
		Button button = new Button(theLabel1);
		button.addTapHandler(theTapHandler1);
		myContent.setWidget(aRow, 1, button);
		button = new Button(theLabel2);
		button.addTapHandler(theTapHandler2);
		myContent.setWidget(aRow, 2, button);
	}

	/**
	 * Setup the buttons to open/close the garage door
	 */
	private void setupGarageDoor()
	{
		int aRow = myContent.getRowCount();
		myContent.setWidget(aRow, 0, new Label("Garage Door"));
		myContent.getFlexCellFormatter().setVerticalAlignment(aRow, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		myContent.getFlexCellFormatter().setWidth(aRow, 0, "1%");
		myContent.setWidget(aRow, 1, new GarageDoorButton());
		myContent.getFlexCellFormatter().setColSpan(aRow, 1, 2);
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
