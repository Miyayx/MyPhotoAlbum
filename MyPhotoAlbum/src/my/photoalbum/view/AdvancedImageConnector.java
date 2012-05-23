package my.photoalbum.view;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.footmark.utils.image.ImageManager;
import com.footmark.utils.image.ImageSetter;

public class AdvancedImageConnector extends ImageSetter {

	public AdvancedImageConnector(ImageManager mgr, ImageView imager, String uri) {
		super(imager, uri);
		mgr.requestImageAsync(uri, this);
		Drawable d = target.getDrawable();
		if(d != null) {
			if(d instanceof TransitionDrawable) {
				target.setImageDrawable(((TransitionDrawable)d).getDrawable(1));
			}
			target.getDrawable().setAlpha(96);
		}
		hdlSetBitmap = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if(!enabled) return;
				if(image().valid() && 
						target.getTag() == AdvancedImageConnector.this){
					
					Drawable black = target.getResources().getDrawable(android.R.color.transparent);
					
					BitmapDrawable fg = new BitmapDrawable(target.getResources(),
							image().bmp());
					
//					TransitionDrawable fore = new TransitionDrawable(new Drawable[]{black, fg});
//					
//					target.setImageDrawable(fore);
//					target.setBackgroundDrawable(null);
//					fore.startTransition(100);
					
					target.setImageBitmap(image().bmp());
				}
				super.handleMessage(msg);
			}
			
		};
	}
	
}
