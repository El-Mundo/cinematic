from scenedetect import detect, ContentDetector, split_video_ffmpeg

scene_list = detect('CV/test.mp4', ContentDetector())
split_video_ffmpeg('CV/test.mp4', scene_list, show_progress=True)
