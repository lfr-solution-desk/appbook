package com.liferay.training.appbook.web.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.training.appbook.web.constants.AppbookPortletKeys;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author lcarbone
 */
@Component(
		immediate = true,
		property = {
				"com.liferay.portlet.css-class-wrapper=appbook-portlet",
				"com.liferay.portlet.header-portlet-css=/css/main.css",
				"com.liferay.portlet.instanceable=true",
				"javax.portlet.display-name=Appbook",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=" + AppbookPortletKeys.APPBOOK,
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user",
				"com.liferay.portlet.display-category=category.appbook",
				"javax.portlet.init-param.add-process-action-success-action=false"
		},
		service = Portlet.class
)
public class AppbookPortlet extends MVCPortlet {

	@Activate
	@Modified
	protected void activate() {
		System.out.println("Activate");
	}
}