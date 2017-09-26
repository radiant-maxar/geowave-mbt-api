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
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class GeoServerService {
    private static final Logger logger = LoggerFactory.getLogger(GeoServerService.class);

    @Value("${geoserver.baseURL}")
    private final String baseURL;

    private final RestTemplate client;

    @Autowired
    public GeoServerService(@Value("${geoserver.baseURL}") String baseURL) {
        this(baseURL, new RestTemplate());
    }

    public GeoServerService(String baseURL, RestTemplate client) {
        this.baseURL = baseURL;
        this.client = client;
    }

    public List<String> listLayers() throws CommunicationException, MalformedResponseException {
        URI uri = new UriTemplate("{baseUrl}/wms?REQUEST=GetCapabilities").expand(baseURL);
        logger.debug("Requesting {}", uri);

        try {
            Resource resource = client.getForObject(uri, Resource.class);
            InputStream inputStream = resource.getInputStream();

            return extractLayerNames(inputStream);
        }
        catch (RestClientException e) {
            logger.error("GeoServer request failed: {}", e, "");
            throw new CommunicationException(e);
        }
        catch (IOException e) {
            logger.error("Could not read GeoServer response: {}", e);
            throw new CommunicationException(e);
        }
    }

    private List<String> extractLayerNames(InputStream stream) throws MalformedResponseException {
        List<String> layerNames = new ArrayList<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();

        LinkedList<String> path = new LinkedList<>();
        try {
            final XMLEventReader reader = factory.createXMLEventReader(stream);

            while (reader.hasNext()) {
                final XMLEvent event = reader.nextEvent();

                switch (event.getEventType()) {
                    case XMLEvent.START_ELEMENT:
                        path.add(event.asStartElement().getName().getLocalPart());
                        break;
                    case XMLEvent.END_ELEMENT:
                        path.pop();
                        break;
                    case XMLEvent.CHARACTERS:
                        final String value = event.asCharacters().getData().trim();
                        if (!value.isEmpty() && path.get(path.size() - 2).equals("Layer") && path.peekLast().equals("Name")) {
                            layerNames.add(value);
                        }
                        break;
                }
            }
        }
        catch (XMLStreamException e) {
            throw new MalformedResponseException(e);
        }

        return layerNames;
    }

    public static class GeoServerException extends Exception {
        public GeoServerException(Throwable cause) {
            super(cause);
        }
    }

    public static class MalformedResponseException extends GeoServerException {
        public MalformedResponseException(Throwable cause) {
            super(cause);
        }
    }

    public static class CommunicationException extends GeoServerException {
        CommunicationException(Throwable cause) {
            super(cause);
        }
    }
}
