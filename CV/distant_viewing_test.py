import dvt

anno_face = dvt.AnnoFaces()
for img, frame, msec in dvt.yield_video("CV/WomanBasketballNo5_clip.mp4"):
    if (frame % 6) == 0: # only process every 6th frame
        anno = anno_face.run(img, visualize=True, embed=True)
        if anno:
            anno['frame'] = frame
            dvt.save_image("CV/Temp/frame%d_face.png" % (frame), anno['img']) # save the annotated image