package mil.nga.giat.mbt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriTemplate;

import javax.validation.constraints.NotNull;

@Configuration
public class BasemapConfiguration {
    private final String basemapDark;
    private final String basemapAerial;
    private final String basemapOsm;

    public BasemapConfiguration(@Value("${basemaps.dark}") String basemapDark,
                                @Value("${basemaps.aerial}") String basemapAerial,
                                @Value("${basemaps.osm}") String basemapOsm) {
        this.basemapDark = basemapDark;
        this.basemapAerial = basemapAerial;
        this.basemapOsm = basemapOsm;
    }

    @NotNull
    public UriTemplate getURITemplate(@NotNull String id) throws UnknownBasemapException {
        String normalizedId = id.trim().toLowerCase();
        if (normalizedId.equals("dark")) {
            return new UriTemplate(basemapDark);
        }

        if (normalizedId.equals("aerial")) {
            return new UriTemplate(basemapAerial);
        }

        if (normalizedId.equals("osm")) {
            return new UriTemplate(basemapOsm);
        }

        throw new UnknownBasemapException();
    }

    public static class UnknownBasemapException extends Exception {}
}
