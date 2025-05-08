package utc.k62.cntt5.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.k62.cntt5.elearning.domain.Video;
import utc.k62.cntt5.elearning.service.VideoService;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PutMapping("/upload")
    public ResponseEntity<Video> save(@RequestBody Video video) {
        Video result = videoService.uploadVideo(video);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-videos")
    public ResponseEntity<?> getAllVideo(@RequestParam String className) {
        return ResponseEntity.ok(videoService.getAllVideos(className));
    }
}
