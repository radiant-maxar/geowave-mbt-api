package mil.nga.giat.mbt.web;

import mil.nga.giat.mbt.config.BasemapConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class TileProxyController {
    private static final int BASEMAP_CACHE_HOURS = 6;

    private static final Logger logger = LoggerFactory.getLogger(TileProxyController.class);

    private final ServletContext context;

    private final BasemapConfiguration basemaps;

    @Autowired
    public TileProxyController(ServletContext context, BasemapConfiguration basemaps) {
        this.context = context;
        this.basemaps = basemaps;
    }

    @GetMapping("/basemaps/{id}/{z}/{x}/{y}.png")
    ResponseEntity<InputStreamResource> basemap(@PathVariable String id,
                                                @PathVariable int x,
                                                @PathVariable int y,
                                                @PathVariable int z) {
        RestTemplate rt = new RestTemplate();
        Map<String, Integer> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        params.put("z", z);

        URI uri;
        try {
            uri = basemaps.templateFor(id).expand(params);
        }
        catch (BasemapConfiguration.UnknownBasemapException e) {
            logger.error("Unknown basemap ID \"{}\"", id);
            return errorAsTile(HttpStatus.NOT_FOUND);
        }

        logger.debug("Proxying basemap request {}", uri);
        try {
            Resource resource = rt.getForObject(uri, Resource.class);
            return ResponseEntity
                    .ok()
                    .cacheControl(CacheControl.maxAge(BASEMAP_CACHE_HOURS, TimeUnit.HOURS))
                    .body(new InputStreamResource(resource.getInputStream()));
        }
        catch (IOException | RestClientException e) {
            logger.error("Could not proxy basemap request: {} (uri={})", e, uri);
            return errorAsTile();
        }
    }

    private ResponseEntity<InputStreamResource> errorAsTile() {
        return errorAsTile(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<InputStreamResource> errorAsTile(HttpStatus status) {
        final InputStream image = context.getResourceAsStream("/tile-error.png");
        return ResponseEntity
                .status(status)
                .body(new InputStreamResource(image));
    }
}
