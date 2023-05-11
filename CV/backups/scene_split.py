from scenedetect import detect, ContentDetector, split_video_ffmpeg

VIDEO_FILE = 'CV/WomanBasketballNo5_clip.mp4'

scene_list = detect(VIDEO_FILE, ContentDetector())
split_video_ffmpeg(VIDEO_FILE, scene_list, show_progress=True)
