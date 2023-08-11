package com.carbon;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import java.io.StringReader;

public class CarbonCodeGeneratorTest extends AbstractMojoTestCase {

    private String pom;

    private Xpp3Dom pomDom;

    private PlexusConfiguration pluginConfiguration;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        pom = "<project>" + "<build>"
                + "<plugins>"
                + "<plugin>"
                + "<artifactId>maven-simple-plugin</artifactId>"
                + "<configuration>"
                + "<basedir>${basedir}/src/main/resources/carbon-code-generator-config.yml</basedir>"
//                + "<keyTwo>valueTwo</keyTwo>"
                + "</configuration>"
                + "</plugin>"
                + "</plugins>"
                + "</build>"
                + "</project>";

        pomDom = Xpp3DomBuilder.build(new StringReader(pom));

        pluginConfiguration = extractPluginConfiguration("maven-simple-plugin", pomDom);
    }

    public void testMojoConfiguration() throws Exception {
        CarbonCodeGenerator mojo = new CarbonCodeGenerator();

        mojo = (CarbonCodeGenerator) configureMojo(mojo, pluginConfiguration);

        mojo.execute();
    }

}