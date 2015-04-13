package ua.od.vassio.backup.spring;

import liquibase.resource.ResourceAccessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vzakharchenko on 04.07.14.
 */
public class SpringResourceOpener implements ResourceAccessor {

    private static ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver();

    @Override
    public Set<String> list(String relativeTo, String path, boolean includeFiles, boolean includeDirectories, boolean recursive) throws IOException {
        Set<String> returnSet = new HashSet<String>();

        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(adjustClasspath(path));

        for (Resource res : resources) {

            if (res instanceof ClassPathResource) {
                returnSet.add(((ClassPathResource) res).getPath());
            } else {
                returnSet.add(res.getURL().toExternalForm());
            }
        }

        return returnSet;
    }

    @Override
    public Set<InputStream> getResourcesAsStream(String path) throws IOException {
        Set<InputStream> returnSet = new HashSet<InputStream>();

        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(adjustClasspath(path));

        if (resources == null || resources.length == 0) {
            return null;
        }
        for (Resource resource : resources) {
            returnSet.add(resource.getInputStream());
        }

        return returnSet;
    }

    public Resource getResource(String file) {
        return resourceLoader.getResource(adjustClasspath(file));
    }

    private String adjustClasspath(String file) {
        return !isPrefixPresent(file) ? ResourceLoader.CLASSPATH_URL_PREFIX + file : file;
    }

    public boolean isPrefixPresent(String file) {
        // file:
        // vfs:
        // classpath:
        if (file.matches("(\\w{2,})(:)(.*)")) {
            return true;
        }
        return false;
    }

    @Override
    public ClassLoader toClassLoader() {
        return resourceLoader.getClassLoader();
    }
}
