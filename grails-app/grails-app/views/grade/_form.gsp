<%@ page import="grails.app.Grade" %>



<div class="fieldcontain ${hasErrors(bean: gradeInstance, field: 'course', 'error')} required">
	<label for="course">
		<g:message code="grade.course.label" default="Course" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="course" name="course.id" from="${grails.app.Course.list()}" optionKey="id" required="" value="${gradeInstance?.course?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: gradeInstance, field: 'mark', 'error')} required">
	<label for="mark">
		<g:message code="grade.mark.label" default="Mark" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="mark" type="number" value="${gradeInstance.mark}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: gradeInstance, field: 'student', 'error')} required">
	<label for="student">
		<g:message code="grade.student.label" default="Student" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="student" name="student.id" from="${grails.app.Student.list()}" optionKey="id" required="" value="${gradeInstance?.student?.id}" class="many-to-one"/>
</div>

