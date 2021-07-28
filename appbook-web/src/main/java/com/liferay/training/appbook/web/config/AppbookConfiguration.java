package com.liferay.training.appbook.web.config;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(category = "appbook-category", scope = ExtendedObjectClassDefinition.Scope.GROUP)
 
@Meta.OCD(id = "com.liferay.training.appbook.web.config.AppbookConfiguration", localization = "content/Language", name = "app-book-configuration")
 
public interface AppbookConfiguration {
 
    @Meta.AD(deflt = "", 
            name = "structureId",
            required = false)
 
    public String structureId();
}