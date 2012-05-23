package my.photoalbum.photo;

public class SharedPhoto extends Photo{
	
	private String text;
	
	public SharedPhoto(String path,String text){
		super(path);
		this.text = text;		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
