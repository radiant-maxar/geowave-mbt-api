package mil.nga.giat.mbt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Component
public class BasemapService {
    private static final Logger logger = LoggerFactory.getLogger(BasemapService.class);

    private final UriTemplate basemapDark;
    private final UriTemplate basemapAerial;
    private final UriTemplate basemapOsm;
    private final RestTemplate restTemplate;

    @Autowired
    public BasemapService(@Value("${basemaps.dark}") String basemapDark,
                          @Value("${basemaps.aerial}") String basemapAerial,
                          @Value("${basemaps.osm}") String basemapOsm) {
        this(basemapDark, basemapAerial, basemapOsm, new RestTemplate());
    }

    public BasemapService(String basemapDark, String basemapAerial, String basemapOsm, RestTemplate restTemplate) {
        this.basemapDark = new UriTemplate(basemapDark);
        this.basemapAerial = new UriTemplate(basemapAerial);
        this.basemapOsm = new UriTemplate(basemapOsm);
        this.restTemplate = restTemplate;
    }

    public InputStream fetchTile(String id, int x, int y, int z) throws CommunicationException, UnknownBasemapException {
        URI uri = templateFor(id).expand(z, x, y);

        logger.debug("Fetching tile \"{}\"", uri);
        try {
            return restTemplate.getForObject(uri, Resource.class).getInputStream();
        }
        catch (IOException | RestClientException e) {
            logger.error("Could not fetch tile: {} (uri={})", e, uri);
            throw new CommunicationException(e);
        }
    }

    private UriTemplate templateFor(@NotNull String id) throws UnknownBasemapException {
        String normalizedId = id.trim().toLowerCase();

        switch (normalizedId) {
            case "dark":
                return basemapDark;
            case "aerial":
                return basemapAerial;
            case "osm":
                return basemapOsm;
        }

        throw new UnknownBasemapException();
    }

    public static class BasemapException extends Exception {
        public BasemapException(Throwable cause) {
            super(cause);
        }

        public BasemapException() {
            super();
        }
    }

    public static class CommunicationException extends BasemapException {
        public CommunicationException(Throwable cause) {
            super(cause);
        }
    }

    public static class UnknownBasemapException extends BasemapException {
    }
}
