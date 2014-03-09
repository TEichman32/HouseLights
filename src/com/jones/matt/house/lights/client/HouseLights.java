package com.jones.matt.house.lights.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.*;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.LayoutPanel;

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
		myContent.setWidth("100%");
		for(int ai = 0; ai < getLightData().size(); ai++)
		{
			generateRow(ai);
		}
		RootPanel.get().add(myContent);
	}

	/**
	 * Generate a single row of light controls
	 *
	 * @param theIndex
	 */
	private void generateRow(int theIndex)
	{
		int aRow = myContent.getRowCount();
		Label aLabel = new Label(getLightData().getLabel(theIndex));
		aLabel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
		aLabel.getElement().getStyle().setMargin(5, Style.Unit.PX);
		myContent.setWidget(aRow, 0, aLabel);
		myContent.getFlexCellFormatter().setVerticalAlignment(aRow, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		myContent.getFlexCellFormatter().setWidth(aRow, 0, "1%");
		Button button = new Button("Off");
		button.addTapHandler(getHandler(theIndex, "off"));
		myContent.setWidget(aRow, 1, button);
		button = new Button("On");
		button.addTapHandler(getHandler(theIndex, "on"));
		myContent.setWidget(aRow, 2, button);
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
				RequestBuilder aBuilder = new RequestBuilder(RequestBuilder.HEAD,
						getLightData().getUrl(theIndex, theOperation));
				aBuilder.setCallback(new RequestCallback()
				{
					public void onResponseReceived(Request request, Response response){}

					public void onError(Request request, Throwable exception){}
				});
				try
				{
					aBuilder.send();
				}
				catch (RequestException e)
				{
					e.printStackTrace();
				}
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
