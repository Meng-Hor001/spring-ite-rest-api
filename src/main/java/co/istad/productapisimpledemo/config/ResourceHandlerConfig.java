package co.istad.productapisimpledemo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {
    @Value("${file.client-path}")
    private  String clientPath;
    @Value("${file.storage-location}")
    private  String storageLocation;
    // localhost:8080/files/filename.png
    // files/filename........
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler(clientPath+"/**").addResourceLocations("file:"+storageLocation);
    }
}
