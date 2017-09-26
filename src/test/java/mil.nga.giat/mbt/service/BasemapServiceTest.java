package mil.nga.giat.mbt.service;

import mil.nga.giat.mbt.service.BasemapService.BasemapException;
import mil.nga.giat.mbt.service.BasemapService.CommunicationException;
import mil.nga.giat.mbt.service.BasemapService.UnknownBasemapException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BasemapServiceTest {
    private static final String DARK = "https://test-basemap-dark.localdomain/{z}/{x}/{y}.png";
    private static final String AERIAL = "https://test-basemap-aerial.localdomain/{z}/{x}/{y}.png";
    private static final String OSM = "https://test-basemap-osm.localdomain/{z}/{x}/{y}.png";

    private RestTemplate restTemplate;
    private Resource resource;

    @Before
    public void setUp() throws IOException {
        restTemplate = mock(RestTemplate.class);
        resource = mock(Resource.class);

        when(restTemplate.getForObject(any(URI.class), eq(Resource.class))).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
    }

    @Test
    public void fetchTile_callsCorrectDarkURL() throws BasemapException, URISyntaxException {
        BasemapService service = new BasemapService(DARK, AERIAL, OSM, restTemplate);
        service.fetchTile("dark", 55, 22, 99);

        verify(restTemplate)
                .getForObject(new URI("https://test-basemap-dark.localdomain/99/55/22.png"), Resource.class);
    }

    @Test
    public void fetchTile_callsCorrectAerialURL() throws BasemapException, URISyntaxException {
        BasemapService service = new BasemapService(DARK, AERIAL, OSM, restTemplate);
        service.fetchTile("aerial", 11, 22, 33);

        verify(restTemplate)
                .getForObject(new URI("https://test-basemap-aerial.localdomain/33/11/22.png"), Resource.class);
    }

    @Test
    public void fetchTile_callsCorrectOSMURL() throws BasemapException, URISyntaxException {
        BasemapService service = new BasemapService(DARK, AERIAL, OSM, restTemplate);
        service.fetchTile("osm", 13, 46, 79);

        verify(restTemplate)
                .getForObject(new URI("https://test-basemap-osm.localdomain/79/13/46.png"), Resource.class);
    }

    @Test(expected = UnknownBasemapException.class)
    public void createURI_throwsIfUnknownBasemapID() throws BasemapException, URISyntaxException {
        BasemapService service = new BasemapService(DARK, AERIAL, OSM, restTemplate);
        service.fetchTile("lolwut", 0, 0, 0);
    }

    @Test(expected = CommunicationException.class)
    public void createURI_throwsIfRequestFails() throws BasemapException, URISyntaxException {
        when(restTemplate.getForObject(any(URI.class), eq(Resource.class)))
                .thenThrow(new RestClientResponseException("oh no", 500, "", new HttpHeaders(), null, null));

        BasemapService service = new BasemapService(DARK, AERIAL, OSM, restTemplate);
        service.fetchTile("dark", 0, 0, 0);
    }
}
