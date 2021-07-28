package com.liferay.training.appbook.web.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.training.appbook.web.constants.AppbookPortletKeys;
import com.liferay.training.appbook.web.constants.MVCCommandNames;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * MVC command for showing the assignments list.
 * 
 * @author liferay
 */
@Component(
    immediate = true, 
    property = {
        "javax.portlet.name=" + AppbookPortletKeys.APPBOOK,
        "mvc.command.name=" + MVCCommandNames.SELECT_SITE
    }, 
    service = MVCActionCommand.class
)
public class SelectSiteMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		
		String siteId = ParamUtil.getString(actionRequest, "siteSelector");

		actionRequest.setAttribute("siteId", siteId);
	}

}