package my.photoalbum.photo;

public class Photo {
	private static final String TAG =  Photo.class.getSimpleName();

	protected String photoPath;
	
	public Photo(String path){
		photoPath = path;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	
	
}
