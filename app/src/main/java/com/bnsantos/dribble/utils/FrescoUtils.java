package com.bnsantos.dribble.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class FrescoUtils {
  public static void load(@NonNull final SimpleDraweeView view, @NonNull final String url, final int w, final int h, final boolean animated){
    Uri uri = Uri.parse(url);
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        .setResizeOptions(new ResizeOptions(w, h))
        .build();

    GenericDraweeHierarchy hierarchy = view.getHierarchy();
    ProgressBarDrawable progressBarImage = new ProgressBarDrawable();
    progressBarImage.setIsVertical(true);

    hierarchy.setProgressBarImage(progressBarImage);
    view.setHierarchy(hierarchy);

    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setImageRequest(request)
        .setOldController(view.getController())
        .setAutoPlayAnimations(animated)
        .build();
    view.setController(controller);
  }
}
