package my.photoalbum;

import java.io.File;
import java.io.FilenameFilter;

public class ImageNameFilter implements FilenameFilter {

	private static final String[] IMAGE = { ".png", ".jpg", ".gif", ".bmp" };

	@Override
	public boolean accept(File dir, String filename) {

		for (String extension : IMAGE) {
			if (filename.toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}
}
