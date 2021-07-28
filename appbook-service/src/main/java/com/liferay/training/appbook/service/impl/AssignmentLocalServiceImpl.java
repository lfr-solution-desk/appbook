/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.training.appbook.service.impl;

import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.training.appbook.model.Assignment;
import com.liferay.training.appbook.service.base.AssignmentLocalServiceBaseImpl;
import com.liferay.training.appbook.validator.AssignmentValidator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The implementation of the assignment local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.training.appbook.service.AssignmentLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssignmentLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.training.appbook.model.Assignment",
	service = AopService.class
)
public class AssignmentLocalServiceImpl extends AssignmentLocalServiceBaseImpl {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Use
	 * <code>com.liferay.training.appbook.service.AssignmentLocalService</code>
	 * via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use
	 * <code>com.liferay.training.appbook.service.AssignmentLocalServiceUtil</code
	 * >.
	 */

	@Override
	public Assignment addAssignment(Assignment assignment) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public Assignment updateAssignment(Assignment assignment) {
		throw new UnsupportedOperationException("Not supported.");
	}

	private void updateAsset(
			Assignment assignment, ServiceContext serviceContext)
			throws PortalException {

		assetEntryLocalService.updateEntry(
				serviceContext.getUserId(), serviceContext.getScopeGroupId(),
				assignment.getCreateDate(), assignment.getModifiedDate(),
				Assignment.class.getName(), assignment.getAssignmentId(),
				assignment.getUuid(), 0, serviceContext.getAssetCategoryIds(),
				serviceContext.getAssetTagNames(), true, true,
				assignment.getCreateDate(), null, null, null,
				ContentTypes.TEXT_HTML,
				assignment.getTitle(),
				assignment.getDescription(), null, null, null, 0, 0,
				serviceContext.getAssetPriority());
	}

	public Assignment addAssignment(long groupId, Long articleId, ServiceContext serviceContext) throws PortalException {

		// Validate assignment parameters.

		//_assignmentValidator.validate(titleMap, descriptionMap, dueDate);

		// Get group and user.

		Group group = groupLocalService.getGroup(groupId);

		long userId = serviceContext.getUserId();

		User user = userLocalService.getUser(userId);

		// Generate primary key for the assignment.

		long assignmentId = counterLocalService.increment(Assignment.class.getName());

		// Create assigment. This doesn't yet persist the entity.

		Assignment assignment = createAssignment(assignmentId);

		// Populate fields.

		assignment.setCompanyId(group.getCompanyId());
		assignment.setCreateDate(serviceContext.getCreateDate(new Date()));
		assignment.setDueDate(null);
		assignment.setDescription(null);
		assignment.setGroupId(groupId);
		assignment.setModifiedDate(serviceContext.getModifiedDate(new Date()));
		assignment.setTitle(null);
		assignment.setUserId(userId);
		assignment.setUserName(user.getScreenName());
		assignment.setIconType(null);
		assignment.setArticleId(articleId);

		// Persist assignment to database.

		assignment = super.addAssignment(assignment);

		// Add permission resources.

		boolean portletActions = false;
		boolean addGroupPermissions = true;
		boolean addGuestPermissions = true;

		resourceLocalService.addResources(group.getCompanyId(), groupId, userId, Assignment.class.getName(),
				assignment.getAssignmentId(), portletActions, addGroupPermissions, addGuestPermissions);

		updateAsset(assignment, serviceContext);

		return assignment;
	}

	public Assignment deleteAssignment(Assignment assignment) throws PortalException {

		// Delete permission resources.

		resourceLocalService.deleteResource(assignment, ResourceConstants.SCOPE_INDIVIDUAL);

		// Delete the Assignment

		assetEntryLocalService.deleteEntry(Assignment.class.getName(), assignment.getAssignmentId());

		return super.deleteAssignment(assignment);
	}

	public Assignment updateAssignment(long assignmentId, String title,
									   String description, Date dueDate, ServiceContext serviceContext) throws PortalException {

		//_assignmentValidator.validate(titleMap, descriptionMap, dueDate);

		// Get the Assignment by id.

		Assignment assignment = getAssignment(assignmentId);

		// Set updated fields and modification date.

		assignment.setModifiedDate(new Date());
		assignment.setTitle(title);
		assignment.setDueDate(dueDate);
		assignment.setDescription(description);

		assignment = super.updateAssignment(assignment);

		updateAsset(assignment, serviceContext);

		return assignment;
	}

	public List<Assignment> getAssignmentsByGroupId(long groupId) {

		return assignmentPersistence.findByGroupId(groupId);
	}

	public List<Assignment> getAssignmentsByGroupId(long groupId, int start, int end) {

		return assignmentPersistence.findByGroupId(groupId, start, end);
	}

	public List<Assignment> getAssignmentsByGroupId(long groupId, int start, int end,
													OrderByComparator<Assignment> orderByComparator) {

		return assignmentPersistence.findByGroupId(groupId, start, end, orderByComparator);
	}

	public List<Assignment> getAssignmentsByKeywords(long groupId, String keywords, int start, int end,
													 OrderByComparator<Assignment> orderByComparator) {

		return assignmentLocalService.dynamicQuery(getKeywordSearchDynamicQuery(groupId, keywords), start, end,
				orderByComparator);
	}

	public long getAssignmentsCountByKeywords(long groupId, String keywords) {
		return assignmentLocalService.dynamicQueryCount(getKeywordSearchDynamicQuery(groupId, keywords));
	}

	private DynamicQuery getKeywordSearchDynamicQuery(long groupId, String keywords) {

		DynamicQuery dynamicQuery = dynamicQuery().add(RestrictionsFactoryUtil.eq("groupId", groupId));

		if (Validator.isNotNull(keywords)) {
			Disjunction disjunctionQuery = RestrictionsFactoryUtil.disjunction();

			disjunctionQuery.add(RestrictionsFactoryUtil.like("title", "%" + keywords + "%"));
			disjunctionQuery.add(RestrictionsFactoryUtil.like("description", "%" + keywords + "%"));
			dynamicQuery.add(disjunctionQuery);
		}

		return dynamicQuery;
	}

	@Reference
	AssignmentValidator _assignmentValidator;

	@Override
	public Assignment addAssignment(long groupId, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
									Date dueDate, ServiceContext serviceContext) throws PortalException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Assignment addAssignment(long groupId, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
									Date dueDate, String iconType, ServiceContext serviceContext) throws PortalException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Assignment updateAssignment(long assignmentId, Map<Locale, String> titleMap,
									   Map<Locale, String> descriptionMap, Date dueDate, ServiceContext serviceContext) throws PortalException {
		// TODO Auto-generated method stub
		return null;
	}
}