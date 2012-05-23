package my.photoalbum;

import java.io.File;
import java.io.FileFilter;

public class FolderFilter implements FileFilter {
	public boolean accept(File file) {
		if(file.isHidden()){
			return false;
		}
		
		if(file.getAbsolutePath().contains(".")){
			return false;
		}
		
		if(file.isDirectory()){
			return true;
		}
		return false;
	}
}
