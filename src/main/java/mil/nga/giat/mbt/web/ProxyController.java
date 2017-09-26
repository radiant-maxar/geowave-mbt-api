package mil.nga.giat.mbt.web;

import mil.nga.giat.mbt.service.BasemapService;
import mil.nga.giat.mbt.service.GeoServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@RestController
public class ProxyController {
    private static final int BASEMAP_CACHE_HOURS = 6;

    private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

    private final ServletContext context;
    private final BasemapService basemaps;

    @Autowired
    public ProxyController(ServletContext context, BasemapService basemaps, GeoServerService geoserver) {
        this.context = context;
        this.basemaps = basemaps;
    }

    @GetMapping("/basemaps/{id}/{z}/{x}/{y}.png")
    ResponseEntity<InputStreamResource> basemap(@PathVariable String id,
                                                @PathVariable int x,
                                                @PathVariable int y,
                                                @PathVariable int z) {
        try {
            InputStream stream = basemaps.fetchTile(id, x, y, z);
            return ResponseEntity
                    .ok()
                    .cacheControl(CacheControl.maxAge(BASEMAP_CACHE_HOURS, TimeUnit.HOURS))
                    .body(new InputStreamResource(stream));
        }
        catch (BasemapService.UnknownBasemapException e) {
            logger.error("Unknown basemap ID \"{}\"", id);
            return errorAsTile(HttpStatus.NOT_FOUND);
        }
        catch (BasemapService.CommunicationException e) {
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
