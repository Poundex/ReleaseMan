<%@ page import="net.poundex.releaseman.Release" %>



<div class="fieldcontain ${hasErrors(bean: configureReleaseCommandInstance, field: 'project', 'error')} required">
	<label for="project">
		<g:message code="release.project.label" default="Project" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="project" name="project.id" from="${net.poundex.releaseman.Project.list()}" optionKey="id" required="" value="${configureReleaseCommandInstance?.project?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: configureReleaseCommandInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="release.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${configureReleaseCommandInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: configureReleaseCommandInstance, field: 'repo', 'error')} required">
	<label for="repo">
		<g:message code="release.repo.label" default="Source Repository" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="repo" name="repo.id" from="${net.poundex.releaseman.vcs.SourceRepository.list()}" optionKey="id" required="" value="${configureReleaseCommandInstance?.repo?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: configureReleaseCommandInstance, field: 'revision', 'error')} required">
    <label for="revision">
        <g:message code="release.revision.label" default="Revision" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="revision" value="${configureReleaseCommandInstance?.revision}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: configureReleaseCommandInstance, field: 'releaseVersion', 'error')} required">
	<label for="releaseVersion">
		<g:message code="release.releaseVersion.label" default="Release Version" />
		<span class="required-indicator">*</span>
	</label>
    <g:textField name="releaseVersion" value="${configureReleaseCommandInstance?.releaseVersion}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: configureReleaseCommandInstance, field: 'nextSnapshotVersion', 'error')} required">
	<label for="nextSnapshotVersion">
		<g:message code="release.nextSnapshotVersion.label" default="Next Snapshot Version" />
		<span class="required-indicator">*</span>
	</label>
    <g:textField name="nextSnapshotVersion" value="${configureReleaseCommandInstance?.nextSnapshotVersion}"/>
</div>