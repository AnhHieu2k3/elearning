package utc.k61.cntt2.class_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.k61.cntt2.class_management.domain.Video;
import utc.k61.cntt2.class_management.service.VideoService;

import java.util.List;

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
