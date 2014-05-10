package org.stuartgunter.dropwizard.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cluster.class)
public class CassandraConfigurationTest {

    private final Cluster.Builder builder = mock(Cluster.Builder.class);
    private final Cluster cluster = mock(Cluster.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(Cluster.class);
        when(Cluster.builder()).thenReturn(builder);
        when(builder.build()).thenReturn(cluster);
    }

    @Test
    public void buildsACluster() throws Exception {
        final CassandraConfiguration configuration = new CassandraConfiguration();
        configuration.setClusterName("test-cluster");
        configuration.setCompression(ProtocolOptions.Compression.LZ4);
        configuration.setContactPoints(new String[] {"host1", "host2"});
        configuration.setJmxEnabled(false);
        configuration.setMetricsEnabled(false);
        configuration.setPort(1234);
        configuration.setProtocolVersion(2);
        configuration.setShutdownWaitSeconds(10);

        final Cluster result = configuration.buildCluster();

        assertThat(result, sameInstance(cluster));
        verify(builder).addContactPoints("host1", "host2");
        verify(builder).withPort(1234);
        verify(builder).withCompression(ProtocolOptions.Compression.LZ4);
        verify(builder).withClusterName("test-cluster");
        verify(builder).withoutJMXReporting();
        verify(builder).withoutMetrics();
        verify(builder).build();
        verifyNoMoreInteractions(builder);
    }
}
