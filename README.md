# Swagger Maven Plugin 
This is an forked version from original one at https://github.com/kongchen/swagger-maven-plugin/blob/master/README.md
In here i added new configuration to load additional classpath from external source.

# Usage
```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.github.kongchen</groupId>
			<artifactId>swagger-maven-plugin</artifactId>
			<version>${swagger-maven-plugin-version}</version>
			<configuration>
				<additionalDependency>
					<additionalClassPath>your classloader</additionalClassPath>
						<includeJarsOnFolder>
							<folders>
								<folder>your jars folder 1</folder>
								<folder>your jars folder 2</folder>
							</folders>
						</includeJarsOnFolder> 
					</additionalDependency>
				<apiSources>
					<apiSource>
						...
					</apiSource>
				</apiSources>
			</configuration>
		</plugin>
	</plugins>
</build>
```
