package mil.nga.giat.mbt.service;

import mil.nga.giat.mbt.service.GeoServerService.GeoServerException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GeoServerServiceTest {
    private static final String BASE_URL = "https://test-base-url.localdomain/path/to/geoserver";

    private RestTemplate restTemplate;
    private Resource resource;

    @Before
    public void setUp() throws IOException {
        restTemplate = mock(RestTemplate.class);
        resource = mock(Resource.class);

        when(restTemplate.getForObject(any(URI.class), eq(Resource.class))).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(loadFixture("getcapabilities.xml"));
    }

    @Test
    public void listLayers_findsAllExpectedLayers() throws GeoServerException {
        GeoServerService service = new GeoServerService(BASE_URL, restTemplate);
        List<String> layers = service.listLayers();

        assertEquals(154, layers.size());
        assertEquals("2017", layers.get(0));
        assertEquals("201705", layers.get(1));
        assertEquals("201706", layers.get(33));
        assertEquals("201707", layers.get(64));
        assertEquals("20170715", layers.get(79));
        assertEquals("20170924", layers.get(152));
        assertEquals("20170925", layers.get(153));
    }

    @Test
    public void listLayers_callsCorrectEndpoint() throws GeoServerException, URISyntaxException {
        GeoServerService service = new GeoServerService(BASE_URL, restTemplate);

        service.listLayers();

        verify(restTemplate).getForObject(new URI("https://test-base-url.localdomain/path/to/geoserver/wms?REQUEST=GetCapabilities"), Resource.class);
    }

    @Test
    public void listLayers_canHandleEmptyList() throws GeoServerException, IOException {
        when(resource.getInputStream()).thenReturn(loadFixture("getcapabilities-no-layers.xml"));

        GeoServerService service = new GeoServerService(BASE_URL, restTemplate);
        List<String> layers = service.listLayers();

        assertEquals(layers.size(), 0);
    }

    private InputStream loadFixture(String relativePath) {
        return getClass().getClassLoader().getResourceAsStream(String.format("fixtures/%s", relativePath));
    }
}
