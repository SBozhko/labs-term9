<%@ page import="grails.app.Student" %>



<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'age', 'error')} required">
	<label for="age">
		<g:message code="student.age.label" default="Age" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="age" type="number" value="${studentInstance.age}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'firstName', 'error')} ">
	<label for="firstName">
		<g:message code="student.firstName.label" default="First Name" />
		
	</label>
	<g:textField name="firstName" value="${studentInstance?.firstName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'gpa', 'error')} required">
	<label for="gpa">
		<g:message code="student.gpa.label" default="Gpa" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="gpa" value="${fieldValue(bean: studentInstance, field: 'gpa')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'lastName', 'error')} ">
	<label for="lastName">
		<g:message code="student.lastName.label" default="Last Name" />
		
	</label>
	<g:textField name="lastName" value="${studentInstance?.lastName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'middleName', 'error')} ">
	<label for="middleName">
		<g:message code="student.middleName.label" default="Middle Name" />
		
	</label>
	<g:textField name="middleName" value="${studentInstance?.middleName}"/>
</div>

