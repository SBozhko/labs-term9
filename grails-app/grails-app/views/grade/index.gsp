
<%@ page import="grails.app.Grade" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'grade.label', default: 'Grade')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-grade" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-grade" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<th><g:message code="grade.course.label" default="Course" /></th>
					
						<g:sortableColumn property="mark" title="${message(code: 'grade.mark.label', default: 'Mark')}" />
					
						<th><g:message code="grade.student.label" default="Student" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${gradeInstanceList}" status="i" var="gradeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${gradeInstance.id}">${fieldValue(bean: gradeInstance, field: "course")}</g:link></td>
					
						<td>${fieldValue(bean: gradeInstance, field: "mark")}</td>
					
						<td>${fieldValue(bean: gradeInstance, field: "student")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${gradeInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
