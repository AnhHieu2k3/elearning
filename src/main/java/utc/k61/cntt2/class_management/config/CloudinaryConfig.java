package utc.k61.cntt2.class_management.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "doxbp2hxh",
                "api_key", "544361568943935",
                "api_secret", "Xu-YAafAoIsc86otfUvb87_KQew"));
    }
}
