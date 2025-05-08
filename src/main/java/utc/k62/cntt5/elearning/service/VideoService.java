package utc.k62.cntt5.elearning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utc.k62.cntt5.elearning.domain.Video;
import utc.k62.cntt5.elearning.exception.BusinessException;
import utc.k62.cntt5.elearning.repository.VideoRepository;

import java.util.List;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> getAllVideos(String className) {
        List<Video> allVideos = videoRepository.findAllByClassName(className);
        if (allVideos.isEmpty()) {
            throw new BusinessException("Not found video for class: " + className);
        }
        return allVideos;
    }

    public Video uploadVideo(Video video) {
        return videoRepository.save(video);
    }

}
