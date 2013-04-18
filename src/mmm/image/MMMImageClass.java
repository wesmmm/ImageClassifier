package mmm.image;

public class MMMImageClass {

	public String getS_class_id() {
		return s_class_id;
	}
	public void set_class_id(String s_class_id) {
		this.s_class_id = s_class_id;
	}
	public String get_class_name() {
		return s_class_name;
	}
	public void set_class_name(String s_class_name) {
		this.s_class_name = s_class_name;
	}
	public String get_class_image_file_name() {
		return s_class_image_file_name;
	}
	public void set_class_image_file_name(String s_class_image_file_name) {
		this.s_class_image_file_name = s_class_image_file_name;
	}
	private String s_class_id;
	private String s_class_name;
	private String s_class_image_file_name;
	
	public double get_eye_width_in_mm() {
		return d_eye_width_in_mm;
	}
	public void set_eye_width_in_mm(double d_eye_width_in_mm) {
		this.d_eye_width_in_mm = d_eye_width_in_mm;
	}
	public double get_nose_height_in_mm() {
		return d_nose_height_in_mm;
	}
	public void set_nose_height_in_mm(double d_nose_height_in_mm) {
		this.d_nose_height_in_mm = d_nose_height_in_mm;
	}
	
	private double d_eye_width_in_mm;
	private double d_nose_height_in_mm;
	
	public MMMImageClass(String ic_file_name, String ic_class_name){
		s_class_image_file_name = ic_file_name;
		s_class_name = ic_class_name;
	}
	// 	- ImageClass(ImageClassId, ImageClassName, ImageClassImage)
}
