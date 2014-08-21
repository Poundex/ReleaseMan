
<%@ page import="net.poundex.releaseman.Release" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'release.label', default: 'Release')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-release" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-release" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<th><g:message code="release.project.label" default="Project" /></th>
					
						<g:sortableColumn property="name" title="${message(code: 'release.name.label', default: 'Name')}" />
					
						<th><g:message code="release.revision.label" default="Revision" /></th>
					
						<th><g:message code="release.releaseVersion.label" default="Release Version" /></th>
					
						<th><g:message code="release.nextSnapshotVersion.label" default="Next Snapshot Version" /></th>
					
						<th><g:message code="release.releaseRevision.label" default="Release Revision" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${releaseInstanceList}" status="i" var="releaseInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${releaseInstance.id}">${fieldValue(bean: releaseInstance, field: "project")}</g:link></td>
					
						<td>${fieldValue(bean: releaseInstance, field: "name")}</td>
					
						<td>${fieldValue(bean: releaseInstance, field: "revision")}</td>
					
						<td>${fieldValue(bean: releaseInstance, field: "releaseVersion")}</td>
					
						<td>${fieldValue(bean: releaseInstance, field: "nextSnapshotVersion")}</td>
					
						<td>${fieldValue(bean: releaseInstance, field: "releaseRevision")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${releaseInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
