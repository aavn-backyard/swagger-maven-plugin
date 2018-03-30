package com.github.kongchen.swagger.docgen.mavenplugin;

import org.apache.maven.plugins.annotations.Parameter;

public class AdditionalDependency {
	@Parameter
	private String additionalClassPath;
	
	@Parameter
	private String includeJarsOnFolder;
	
	public String getAdditionalClassPath() {
		return additionalClassPath;
	}
	public void setAdditionalClassPath(String additionalClassPath) {
		this.additionalClassPath = additionalClassPath;
	}
	public String getIncludeJarsOnFolder() {
		return includeJarsOnFolder;
	}
	public void setIncludeJarsOnFolder(String includeJarsOnFolder) {
		this.includeJarsOnFolder = includeJarsOnFolder;
	}
	
}
