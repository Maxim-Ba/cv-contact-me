package me.maxim.cvcontactme.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import me.maxim.cvcontactme.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

@Service
public class GeoIpService {

    private static final Logger log = LoggerFactory.getLogger(GeoIpService.class);

    private final AppProperties appProperties;

    private DatabaseReader reader;

    public GeoIpService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @PostConstruct
    public void init() {
        String dbPath = appProperties.getGeoip().getDatabasePath();
        if (dbPath == null || dbPath.trim().isEmpty()) {
            log.warn("GeoIP: app.geoip.database-path not configured — country resolution disabled");
            return;
        }
        try {
            Resource resource;
            if (dbPath.startsWith("classpath:")) {
                resource = new ClassPathResource(dbPath.substring("classpath:".length()));
            } else {
                resource = new FileSystemResource(dbPath);
            }
            reader = new DatabaseReader.Builder(resource.getInputStream()).build();
            log.info("GeoIP database loaded from {}", dbPath);
        } catch (Exception e) {
            log.warn("GeoIP: failed to load database from '{}': {}", dbPath, e.getMessage());
        }
    }

    /**
     * Resolves ISO 3166-1 alpha-2 country code for the given IP.
     * Returns null on any error or when the database is not available.
     */
    public String resolveCountry(String ip) {
        if (reader == null || ip == null || ip.trim().isEmpty()) {
            return null;
        }
        try {
            InetAddress address = InetAddress.getByName(ip.trim());
            CountryResponse response = reader.country(address);
            return response.getCountry().getIsoCode();
        } catch (Exception e) {
            log.debug("GeoIP: could not resolve country for IP '{}': {}", ip, e.getMessage());
            return null;
        }
    }
}
