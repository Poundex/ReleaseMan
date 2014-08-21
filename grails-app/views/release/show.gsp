
<%@ page import="net.poundex.releaseman.Release" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'release.label', default: 'Release')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-release" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-release" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list release">
			
				<g:if test="${releaseInstance?.project}">
				<li class="fieldcontain">
					<span id="project-label" class="property-label"><g:message code="release.project.label" default="Project" /></span>
					
						<span class="property-value" aria-labelledby="project-label"><g:link controller="project" action="show" id="${releaseInstance?.project?.id}">${releaseInstance?.project?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="release.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${releaseInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.revision}">
				<li class="fieldcontain">
					<span id="revision-label" class="property-label"><g:message code="release.revision.label" default="Revision" /></span>
					
						<span class="property-value" aria-labelledby="revision-label"><g:link controller="revision" action="show" id="${releaseInstance?.revision?.id}">${releaseInstance?.revision?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.releaseVersion}">
				<li class="fieldcontain">
					<span id="releaseVersion-label" class="property-label"><g:message code="release.releaseVersion.label" default="Release Version" /></span>
					
						<span class="property-value" aria-labelledby="releaseVersion-label"><g:link controller="simpleVersion" action="show" id="${releaseInstance?.releaseVersion?.id}">${releaseInstance?.releaseVersion?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.nextSnapshotVersion}">
				<li class="fieldcontain">
					<span id="nextSnapshotVersion-label" class="property-label"><g:message code="release.nextSnapshotVersion.label" default="Next Snapshot Version" /></span>
					
						<span class="property-value" aria-labelledby="nextSnapshotVersion-label"><g:link controller="simpleVersion" action="show" id="${releaseInstance?.nextSnapshotVersion?.id}">${releaseInstance?.nextSnapshotVersion?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.releaseRevision}">
				<li class="fieldcontain">
					<span id="releaseRevision-label" class="property-label"><g:message code="release.releaseRevision.label" default="Release Revision" /></span>
					
						<span class="property-value" aria-labelledby="releaseRevision-label"><g:link controller="revision" action="show" id="${releaseInstance?.releaseRevision?.id}">${releaseInstance?.releaseRevision?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.snapshotRevision}">
				<li class="fieldcontain">
					<span id="snapshotRevision-label" class="property-label"><g:message code="release.snapshotRevision.label" default="Snapshot Revision" /></span>
					
						<span class="property-value" aria-labelledby="snapshotRevision-label"><g:link controller="revision" action="show" id="${releaseInstance?.snapshotRevision?.id}">${releaseInstance?.snapshotRevision?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.QABuilds}">
				<li class="fieldcontain">
					<span id="QABuilds-label" class="property-label"><g:message code="release.QABuilds.label" default="QAB uilds" /></span>
					
						<g:each in="${releaseInstance.QABuilds}" var="Q">
						<span class="property-value" aria-labelledby="QABuilds-label"><g:link controller="CIBuild" action="show" id="${Q.id}">${Q?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.deployJobs}">
				<li class="fieldcontain">
					<span id="deployJobs-label" class="property-label"><g:message code="release.deployJobs.label" default="Deploy Jobs" /></span>
					
						<g:each in="${releaseInstance.deployJobs}" var="d">
						<span class="property-value" aria-labelledby="deployJobs-label"><g:link controller="CIBuild" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.releaseBuilds}">
				<li class="fieldcontain">
					<span id="releaseBuilds-label" class="property-label"><g:message code="release.releaseBuilds.label" default="Release Builds" /></span>
					
						<g:each in="${releaseInstance.releaseBuilds}" var="r">
						<span class="property-value" aria-labelledby="releaseBuilds-label"><g:link controller="CIBuild" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${releaseInstance?.sanityBuilds}">
				<li class="fieldcontain">
					<span id="sanityBuilds-label" class="property-label"><g:message code="release.sanityBuilds.label" default="Sanity Builds" /></span>
					
						<g:each in="${releaseInstance.sanityBuilds}" var="s">
						<span class="property-value" aria-labelledby="sanityBuilds-label"><g:link controller="CIBuild" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:releaseInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${releaseInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    <g:link class="button" action="doMakeRelease" resource="${releaseInstance}"><g:message code="default.button.makerelease.label" default="Make Release" /></g:link>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
