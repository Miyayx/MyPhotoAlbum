package my.photoalbum.album;

import my.photoalbum.decoder.AlbumIconDecoder;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class AlbumInfo implements Comparable<AlbumInfo>{
	protected long albumId;
	protected String albumName;
	protected Bitmap albumBitmap;
	protected int photoNum;
	protected String albumPath;
	protected AlbumIconDecoder iconDecoder;
	
	public AlbumInfo(){
		iconDecoder = new AlbumIconDecoder();
		albumId = 0;
		albumName = "";
		albumBitmap = null;
		photoNum = 0;
		albumPath = "";
	}
	
	public AlbumInfo(String name,int num){
		this(name,null,num);
		iconDecoder = new AlbumIconDecoder();
	}
	
	public AlbumInfo(String name,Bitmap bm,int num){
		albumName = name;
		albumBitmap = bm;
		photoNum = num;
		iconDecoder = new AlbumIconDecoder();
	}
	
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public Bitmap getAlbumBitmap() {
		return albumBitmap;
	}
	public void setAlbumBitmap(Bitmap albumBitmap) {
		this.albumBitmap = albumBitmap;
	}
	public int getPhotoNum() {
		return photoNum;
	}
	public void setPhotoNum(int photoNum) {
		this.photoNum = photoNum;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getAlbumPath() {
		return albumPath;
	}

	public void setAlbumPath(String albumPath) {
		this.albumPath = albumPath;
	}


	@Override
	public int compareTo(AlbumInfo another) {
		return this.albumName.compareTo(another.getAlbumName()); 
	}
	
	
	
}
