package com.di.toolkit;

/**
 * @author di
 */
public enum ContentTypeEnum {
	BMP(".bmp", "application/x-bmp"), DOC(".doc", "application/msword"), GIF(".gif", "image/gif"), 
	HTML(".html","text/html"), ICO(".ico", "image/x-icon"), IMG(".img", "application/x-img"), 
	JAVA(".java", "java/*"), JPEG(".jpeg", "image/jpeg"), JPG(".jpg", "application/x-jpg"), 
	JSP(".jsp", "text/html"), MP4(".mp4","video/mpeg4"), MPEG(".mpeg", "video/mpg"), 
	PNG(".png", "application/x-png"), PPT(".ppt","application/x-ppt"), RM(".rm", "application/vnd.rn-realmedia"), 
	SWF(".swf","application/x-shockwave-flash"), TIF(".tif", "image/tiff"), WAV(".wav","audio/wav"), 
	WMA(".wma", "audio/x-ms-wma"), WMV(".wmv","video/x-ms-wmv"), XHTML(".xhtml", "text/html"), 
	XLS(".xls","application/x-xls"), XML(".xml", "text/xml"), EXE(".exe", "application/x-msdownload"), 
	HTM(".htm", "text/html"), JS(".js","application/x-javascript"), MP3(".mp3","audio/mp3"),CSS(".css","text/css");
	
	String fileExt;
	String mimeType;

	ContentTypeEnum(String fileExt, String mimeType) {
		this.fileExt = fileExt;
		this.mimeType = mimeType;
	}
	
	public String getFileExt() {
		return fileExt;
	}

	public String getMimeType() {
		return mimeType;
	}

	public static String getMimeByFileExt(String fileName){
		for (ContentTypeEnum ct:ContentTypeEnum.values()) {
			if(fileName.endsWith(ct.getFileExt())){
				return ct.getMimeType();
			}
		}
		return "text/html";
	}
}
