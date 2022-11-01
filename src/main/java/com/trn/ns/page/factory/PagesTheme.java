package com.trn.ns.page.factory;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;

import com.trn.ns.page.constants.NSGenericConstants;
import com.trn.ns.page.constants.ThemeConstants;
import com.trn.ns.page.constants.ViewerPageConstants;
import com.trn.ns.web.page.WebActions;

public class PagesTheme extends WebActions{

	private WebDriver driver;

	public PagesTheme(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}

	public boolean verifyButtonTheme(WebElement element,final String disableEnable, String theme) {

		if(!element.isDisplayed())
			scrollIntoView(element);

		boolean status =true;

		String textColor = getCssValue(element, NSGenericConstants.CSS_PROP_COLOR);
		
		switch (theme) {
		case ThemeConstants.EUREKA_THEME_NAME:
		{
			status= status && getCssValue(element, NSGenericConstants.CSS_PRO_BACKGROUND).equals(ThemeConstants.EUREKA_BUTTON_BACKGROUND);
			status = status && getCssValue(element, NSGenericConstants.BACKGROUND_COLOR).equals(ThemeConstants.EUREKA_BUTTON_BACKGROUND_COLOR);
			
			if(!textColor.equals(ThemeConstants.THEME_BUTTON_TEXT_COLOR))
				status = status && textColor.equals(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
			else
				status = status && textColor.equals(ThemeConstants.THEME_BUTTON_TEXT_COLOR);
		}
		break;

		case ThemeConstants.DARK_THEME_NAME:
		{
			status= status && getCssValue(element, NSGenericConstants.CSS_PRO_BACKGROUND).equals(ThemeConstants.DARK_BUTTON_BACKGROUND);
			status = status && getCssValue(element, NSGenericConstants.BACKGROUND_COLOR).equals(ThemeConstants.DARK_BUTTON_BACKGROUND_COLOR);
			
			if(!textColor.equals(ThemeConstants.THEME_BUTTON_TEXT_COLOR))
				status = status && textColor.equals(ThemeConstants.DARK_LABEL_FONT_COLOR);
			else
				status = status && textColor.equals(ThemeConstants.THEME_BUTTON_TEXT_COLOR);
			
		}	
		break;

		default:
			break;
		}
		
		if(disableEnable.equalsIgnoreCase(NSGenericConstants.ENABLE_TEXT)) {
			status = status && getCssValue(element, NSGenericConstants.OPACITY).equals("1");
			status = status && !isAttribtuePresent(element, NSGenericConstants.ICON_DISABLE);
		}
		else {

			status = status && getCssValue(element, NSGenericConstants.OPACITY).equals("0.65");
			status = status && isAttribtuePresent(element, NSGenericConstants.ICON_DISABLE);
		}
		return status;
	}

	public boolean verifyButtonIsFilled(WebElement element, String theme) {

		boolean status =true;

			switch (theme) {
		case ThemeConstants.EUREKA_THEME_NAME:
			status= status && getCssValue(element, NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			break;

		case ThemeConstants.DARK_THEME_NAME:
			status= status && getCssValue(element, NSGenericConstants.FILL).equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
			break;

		default:
			break;
		}
		return status;
	}

	public boolean verifyButtonIsNotFilled(WebElement element, String theme) {

		boolean status =true;

		switch (theme) {
		case ThemeConstants.EUREKA_THEME_NAME:
			status= status && getCssValue(element, NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_ICON_BACKGROUND_COLOR);
			break;

		case ThemeConstants.DARK_THEME_NAME:
			status= status && getCssValue(element, NSGenericConstants.FILL).equals(ThemeConstants.DARK_ICON_BACKGROUND_COLOR);
			break;

		default:
			break;
		}

		return status;
	}

	public boolean verifyThemeOnTextbox(List<WebElement> items, String theme) {

		boolean status = true;
		for(WebElement item : items) {

			if(!item.isDisplayed())
				scrollIntoView(item);	
			if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
				status = status && getBackgroundColor(item).equals(ThemeConstants.DARK_BACKGROUND_COLOR);
				status = status && getBorderColorOfWebElemnt(item).equals(ThemeConstants.DARK_BORDER_COLOR);
			}else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
				status = status && getBackgroundColor(item).equals(ThemeConstants.EUREKA_BACKGROUND_COLOR);
				status = status && getBorderColorOfWebElemnt(item).equals(ThemeConstants.EUREKA_BORDER_COLOR);
			}
		}
		return status;

	}

	public boolean verifyThemeOnLabel(List<WebElement> items, String theme) {

		boolean status = true;
		for(WebElement item : items) {

			if(!item.isDisplayed())
				scrollIntoView(item);	
			if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
				status = status && getColorOfRows(item).equals(ThemeConstants.DARK_LABEL_FONT_COLOR);
			else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
				status = status && getColorOfRows(item).equals(ThemeConstants.EUREKA_LABEL_FONT_COLOR);

			status = status && getFontsizeOfWebElement(item).equals(ViewerPageConstants.FONT_SIZE_FOR_TEXT);
			if(!status)
				break;

		}
		return status;

	}

	public boolean verifyThemeOnLabel(WebElement items, String theme) {

		boolean status = true;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = status && getColorOfRows(items).equals(ThemeConstants.DARK_LABEL_FONT_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = status && getColorOfRows(items).equals(ThemeConstants.EUREKA_LABEL_FONT_COLOR);


		return status;

	}
	
	
	public boolean verifyThemeOnLabel(WebElement items,String prop, String theme) {

		boolean status = true;

		String val = getCssValue(items, prop);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = status && val.equals(ThemeConstants.DARK_ICON_BG_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = status && val.equals(ThemeConstants.PAGE_BACKGROUND);


		return status;

	}
	
	public boolean verifyThemeOnTableHeader(WebElement items, String theme) {
		boolean status=false;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getColorOfRows(items).equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
			status = getColorOfRows(items).equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);


		return status;

	}

	public boolean verifyThemeOnTableHeader(WebElement items,String cssPara,String theme) {
		boolean status=false;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getCssValue(items,cssPara).equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
			status = getCssValue(items,cssPara).equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);


		return status;

	}

	public boolean verifyThemeOnTooltip(WebElement tooltip, String theme) {

		boolean status = true;
		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && getBackgroundColor(tooltip).equalsIgnoreCase(ThemeConstants.PAGE_BACKGROUND);
			status = status && getColorOfRows(tooltip).equalsIgnoreCase(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
			status = status && getCssValue(tooltip,NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_TOOLTIP_SHADOW_COLOR);
		}else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && getBackgroundColor(tooltip).equalsIgnoreCase(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
			status = status && getColorOfRows(tooltip).equalsIgnoreCase(ThemeConstants.DARK_LABEL_FONT_COLOR);
			status = status && getCssValue(tooltip,NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_TOOLTIP_SHADOW_COLOR);
		}
		return status;

	}

	public boolean verifyThemeOnTableHeader(List<WebElement> items, String theme) {

		boolean status = true;
		for(WebElement item : items) {

			if(!item.isDisplayed())
				scrollIntoView(item);	
			if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
				status = status && getColorOfRows(item).equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
			else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
				status = status && getColorOfRows(item).equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);

		}
		return status;

	}

	public boolean verifyThemeForCheckbox(WebElement item,boolean checkUncheck, String theme) {

		boolean status = true;

		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && getColorOfRows(item).equalsIgnoreCase(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);
			status = status && getColorOfRows(item.findElement(By.cssSelector("svg"))).equalsIgnoreCase(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);
		}else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && getColorOfRows(item).equalsIgnoreCase(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
			status = status && getColorOfRows(item.findElement(By.cssSelector("svg"))).equalsIgnoreCase(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
		}

		if(!checkUncheck) {
			By checkbox = By.cssSelector("svg rect");
			status = status && isElementPresent(item.findElement(checkbox));
			String value = getCssValue(item.findElement(checkbox),NSGenericConstants.STROKE);

			if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
				status = status && value.equalsIgnoreCase(ThemeConstants.EUREKA_CHECKBOX_BORDER);
			else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) 
				status = status && value.equalsIgnoreCase(ThemeConstants.DARK_CHECKBOX_BORDER);
		}
		else{

			By checkbox = By.cssSelector("svg g rect");
			status = status && isElementPresent(item.findElement(checkbox));
			String value = getCssValue(item.findElement(checkbox),NSGenericConstants.FILL);
			if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))		
				status = status && value.equalsIgnoreCase(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) 
				status = status && value.equalsIgnoreCase(ThemeConstants.DARK_BUTTON_BORDER_COLOR);

		}
		return status;

	}

	public boolean verifyThemeForNotification(WebElement item, String theme) {

		boolean status = true;

		String background = getBackgroundColor(item);
		String shadow = getCssValue(item, NSGenericConstants.BOX_SHADOW);
		String rounderCorner = getCssValue(item, NSGenericConstants.CSS_BORDER_RADIUS);
		
		
		System.out.println(background);
		System.out.println(shadow);
		System.out.println(rounderCorner);
		
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
			status = status && shadow.contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR);


		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.PAGE_BACKGROUND);
			status = status && shadow.contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
		}
		status = status && rounderCorner.equals(ThemeConstants.ROUNDED_CORNER);
		return status;

	}

	public boolean verifyThemeForNotification(WebElement item, String cornerPixel,String theme) {

		boolean status = true;

		String background = getBackgroundColor(item);
		String shadow = getCssValue(item, NSGenericConstants.BOX_SHADOW);
		String rounderCorner = getCssValue(item, NSGenericConstants.CSS_BORDER_RADIUS);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
			status = status && shadow.contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR);


		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.PAGE_BACKGROUND);
			status = status && shadow.contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
		}

		status = status && rounderCorner.equals(cornerPixel);

		return status;

	}

	public boolean verifyThemeForText(WebElement item, String theme) {

		boolean status = true;

		String value = getColorOfRows(item);
		String background = getBackgroundColor(item);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) { 
			status = status && value.equals(ThemeConstants.DARK_LABEL_FONT_COLOR);
			status = status && background.equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && value.equals(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
			status = status && background.equals(ThemeConstants.PAGE_BACKGROUND);

		}


		return status;

	}

	public boolean verifyThemeForSelectedText(WebElement item, String theme) {

		boolean status = true;

		String value = getColorOfRows(item);
		String background = getBackgroundColor(item);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) { 
			status = status && value.equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
			status = status && background.equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && value.equals(ThemeConstants.PAGE_BACKGROUND);
			status = status && background.equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);
		}


		return status;

	}


	public boolean verifyThemeForTitle(WebElement item, String theme) {

		boolean status = true;

		String value = getColorOfRows(item);
		String background = getBackgroundColor(item);
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) { 
			status = status && value.equals(ThemeConstants.DARK_TABLE_HEADER_TEXT_COLOR);
			status = status && background.equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && value.equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR);
			status = status && background.equals(ThemeConstants.PAGE_BACKGROUND);

		}


		return status;

	}

	public boolean verifyThemeForBorder(WebElement item, String theme) {
		boolean status = true;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getCssValue(item, NSGenericConstants.FILL).equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR);

		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = getCssValue(item, NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);

		return status;

	}

	public boolean verifyThemeForBorder(WebElement item, String cssParam, String theme) {
		boolean status = true;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getCssValue(item, cssParam).equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR)||getCssValue(item, cssParam).equals(ThemeConstants.DARK_TOOL_PANEL_BORDER);

		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = getCssValue(item, cssParam).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR)||getCssValue(item, cssParam).equals(ThemeConstants.EUREKA_TOOL_PANEL_BORDER);

		return status;

	}

	public boolean verifyThemeForSectionWithBorder(WebElement item, String theme) {
		boolean status = false;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getCssValue(item, NSGenericConstants.CSS_PROP_BORDER_COLOR).equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR);

		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = getCssValue(item, NSGenericConstants.CSS_PROP_BORDER_COLOR).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);

		return status;

	}

	public boolean verifyThemeForSectionWithoutBorder(WebElement item, String theme) {
		boolean status = false;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getCssValue(item, NSGenericConstants.CSS_PROP_BORDER_COLOR).equals(ThemeConstants.DARK_ICON_BACKGROUND_COLOR);

		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = getCssValue(item, NSGenericConstants.CSS_PROP_BORDER_COLOR).equals(ThemeConstants.EUREKA_ICON_BACKGROUND_COLOR);
		
		return status;

	}

	public boolean verifyThemeForHeaderIcons(WebElement item, String theme) {
		boolean status = false;
		
		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = getBackgroundColor(item).equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
			status = status && getCssValue(item, NSGenericConstants.BORDER).contains(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
			status = status && getCssValue(item, NSGenericConstants.CSS_PROP_COLOR).contains(ThemeConstants.DARK_LABEL_FONT_COLOR);
		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = getBackgroundColor(item).equals(ThemeConstants.PAGE_BACKGROUND);
			status = status && getCssValue(item, NSGenericConstants.BORDER).contains(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			status = status && getCssValue(item, NSGenericConstants.CSS_PROP_COLOR).contains(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
		}
	
		return status;

	}

	public boolean verifyThemeForPopup(WebElement item, String theme) {

		boolean status = true;

		String background = getBackgroundColor(item);
		String rounderCorner = getCssValue(item, NSGenericConstants.CSS_BORDER_RADIUS);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = status && background.equals(ThemeConstants.DARK_TOOLTIP_BACKGROUND_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = status && background.equals(ThemeConstants.EUREKA_ICON_BG_COLOR);

		status = status && rounderCorner.equals(ThemeConstants.ROUNDED_CORNER_POPUP);

		return status;

	}

	public boolean verifyFillForPopupBasedOnTheme(WebElement item, String theme) {

		boolean status = true;

		String background = getCssValue(item,NSGenericConstants.FILL);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = status && background.equals(ThemeConstants.DARK_VIEWBOX_NO_BACKGROUND);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME))
			status = status && background.equals(ThemeConstants.EUREKA_VIEWBOX_NO_BACKGROUND);

		return status;

	}

	public boolean verifyThemeForActiveTab(WebElement item, String theme) {

		boolean status = true;

		String background = getBackgroundColor(item);
		String color = getColorOfRows(item);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.DARK_BACKGROUND_COLOR);
			status = status && color.equals(ThemeConstants.DARK_LABEL_FONT_COLOR);

		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.EUREKA_BACKGROUND_COLOR);
			status = status && color.equals(ThemeConstants.EUREKA_TABLE_HEADER_TEXT_COLOR)|| status && color.equals(ThemeConstants.EUREKA_LABEL_FONT_COLOR);
		}

		return status;

	}

	public boolean verifyThemeForInActiveTab(WebElement item, String theme) {

		boolean status = true;

		String background = getBackgroundColor(item);
		String color = getColorOfRows(item);

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.DARK_TABLE_BACKGROUND);
		}
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && background.equals(ThemeConstants.EUREKA_TABLE_BACKGROUND);

		}
		status = status && color.equals(ThemeConstants.INACTIVE_TEXT_COLOR);
		return status;

	}


	public boolean verifyThemeForDialogPopUP(WebElement item, String theme) {
		boolean status = getCssValue(item,NSGenericConstants.CSS_BORDER_RADIUS).equalsIgnoreCase(ThemeConstants.ROUNDED_CORNOR_PENDING_DIALOG);
		if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) {
			status = status && getCssValue(item,NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.EUREKA_DIALOG_BORDER_SHADOW);
			status = status && getCssValue(item,NSGenericConstants.BACKGROUND_COLOR).equalsIgnoreCase(ThemeConstants.EUREKA_ICON_BG_COLOR);

		}else if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME)) {
			status = status && getCssValue(item,NSGenericConstants.BOX_SHADOW).equalsIgnoreCase(ThemeConstants.DARK_DIALOG_BORDER_SHADOW);
			status = status && getCssValue(item,NSGenericConstants.BACKGROUND_COLOR).equalsIgnoreCase(ThemeConstants.DARK_ICON_BG_COLOR);
		}
		return status;

	}

	public boolean verifyThemeForEyeIcon(WebElement ele, String theme) {

		boolean status =true;

		switch (theme) {
		case ThemeConstants.EUREKA_THEME_NAME:
			status= getCssValue(ele, NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_BUTTON_BORDER_COLOR);
			break;

		case ThemeConstants.DARK_THEME_NAME:
			status= getCssValue(ele, NSGenericConstants.FILL).equals(ThemeConstants.DARK_BUTTON_BORDER_COLOR);
			break;

		default:
			break;
		}
		return status;
	}


	public boolean verifyThemeOnToolBoxArrow(WebElement items, String theme) {
		boolean status=true;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status= getCssValue(items, NSGenericConstants.FILL).equals(ThemeConstants.DARK_CHECKBOX_BORDER);


		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
			status= getCssValue(items, NSGenericConstants.FILL).equals(ThemeConstants.EUREKA_CHECKBOX_BORDER);


		return status;
	}
	
	public boolean verifyThemeForTabFont(WebElement items, String theme) {
		boolean status=true;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status= getCssValue(items, NSGenericConstants.CSS_PROP_COLOR).equals(ThemeConstants.DARK_OPTION_FONT_COLOR);

		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
			status= getCssValue(items, NSGenericConstants.CSS_PROP_COLOR).equals(ThemeConstants.EUREKA_OPTION_FONT_COLOR);


		return status;
	}

	public boolean verifyThemeForSlider(WebElement items, String theme) {
		boolean status=false;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getBackgroundColor(items).equals(ThemeConstants.DARK_OPTION_FONT_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
			status = getBackgroundColor(items).equals(ThemeConstants.EUREKA_OPTION_FONT_COLOR);
		return status;

	}
	
	public boolean verifyThemeForIcon(WebElement items, String theme) {
		boolean status=false;

		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
			status = getBackgroundColor(items).equals(ThemeConstants.DARK_ICON_BG_COLOR);
		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
			status = getBackgroundColor(items).equals(ThemeConstants.EUREKA_ICON_BG_COLOR);
		return status;

	}
	

     public boolean verifyThemeForFindingRows(WebElement items, String theme) {
 		boolean status=false;

 		if(theme.equalsIgnoreCase(ThemeConstants.DARK_THEME_NAME))
 			status = getBackgroundColor(items).equals(ThemeConstants.DARK_POPUP_BACKGROUND);
 		else if(theme.equalsIgnoreCase(ThemeConstants.EUREKA_THEME_NAME)) 
 			status = getBackgroundColor(items).equals(ThemeConstants.EUREKA_POPUP_BACKGROUND);
 		return status;

 	}
    

	@DataProvider(name="ThemeName") 
	public static Object[][] getThemeName() {
		return new Object[][]{{new String(ThemeConstants.DARK_THEME_NAME)}, {new String(ThemeConstants.EUREKA_THEME_NAME)}};
	}

	

}