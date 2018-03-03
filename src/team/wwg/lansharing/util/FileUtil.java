package team.wwg.lansharing.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	private static String savePath = "E:/cliant/";
	
	/**
	 * 
	 * @param Fileuri  �ļ���uri �� D:/dsd/dsd.txt
	 * @return  �ļ� ��                     �� dsd.txt
	 */
	public static String getFileAllName(String Fileuri){
		
		int i = Fileuri.lastIndexOf("\\");
		String fileName = Fileuri.substring(i + 1);
		return fileName;
		
	}
	
	
	public static String getRightUri(String Fileuri){
		
		String fileName = Fileuri.replaceAll("\\\\", "/");
	
		return fileName;
	}
	
	
	
	public static void setDefaultPath(String str) {
		
		savePath = getRightUri(str);
	}
	
	
	
	
	
	
	
	
	
	/**
	 * ��ȡ������·�������� �µ��ļ�
	 * @param fileUrl  �Է����ļ�·��  ��  D:/txds.txt
	 * @return ���ر��ؽ�Ҫ������ļ��� ����������ĩβ+_copy ������ʶ ��  "E:/cliant/txds.txt 
	 * 												 �ĸ����� E:/cliant/txds_copy.txt
	 */
	
	public static File getTargetFile(String fileUrl){
		
		String	fileName = FileUtil.getFileAllName(fileUrl);
		
		
		
		File filepath =  new File(savePath);
		
		if(!filepath.exists() && !filepath .isDirectory()){
			filepath.mkdir();
		}
		
		File file =  new File(savePath+fileName);
		file =getOnlyfile(fileName,file);
		
		return file;
	}
	

	

	
	/**
	 *  ��ȡ������·����������Ҫ���Ϊ���ļ���·��   ���� �µ��ļ�
	 * @param fileUrl  �Է����ļ�·��  ��  D:/txds.txt
	 * @param newPath  �Լ�Ҫ���Ϊ�ĵ�ַ
	 * @return  �������ձ�����ļ�
	 * 			���ر��ؽ�Ҫ������ļ��� ����������ĩβ+_copy ������ʶ ��  "E:/cliant/txds.txt 
	 * 												 �ĸ����� E:/cliant/txds_copy.txt
	 */
	public static File getTargetFile(String fileUrl , String newPath){
		
		String	fileName = FileUtil.getFileAllName(fileUrl);
		
		String newpath_ =  getRightUri(newPath)+"/";
		
		File filepath =  new File(newpath_);
		
		if(!filepath.exists() && !filepath .isDirectory()){
			filepath.mkdir();
		}
		
		File file =  new File(newpath_+fileName);
		file =getOnlyfile(fileName,file,newpath_);
		
		return file;
	}
	
	
	
	
	/**
	 *  ��ȡ�ļ������� ��׺��  �� dsad��txt  
	 * @param fileuri  �ļ��������ļ�·��
	 * @return  �����ļ�����  ��    txt
	 */
	public static String getFiletype(String  fileuri){
		int i = fileuri.lastIndexOf(".");
		String filetype = fileuri.substring(i + 1);
		return filetype;
	}
	

	/**
	 * ��ȡ�ļ�������    
	 * @param fileUri �ļ�uri  ��D:/ds��������d/dsd.txt
	 * @return     dsd
	 */
	public static String getFileName(String fileUri){
		
		String fileAllname =getFileAllName(fileUri);
		
		int i = fileAllname.lastIndexOf(".");
		
		String filename = fileAllname.substring(0,i);
	
		return filename;
		
	}
	
	
	
	
	
	
	
	
	/**
	 * �ж��ļ������Ƿ���ڣ�������ڣ�����Ӻ�׺
	 * @param filename Ҫ�жϵ��ļ���
	 * @return
	 */
	public static File getOnlyfile(String fileName ,File file){
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{
			
			fileName = getFileName(fileName)+"_copy"+"."+getFiletype(fileName);
			
			file = new File(savePath+fileName);
			
			file = getOnlyfile(fileName,file);
		}
		
		return file;
		
	}
	
	
	
	/**
	 * �ж��ļ������Ƿ���ڣ�������ڣ�����Ӻ�׺
	 * @param filename Ҫ�жϵ��ļ���
	 * @return
	 */
	public static File getOnlyfile(String fileName ,File file,String newpath){
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{
			
			fileName = getFileName(fileName)+"_copy"+"."+getFiletype(fileName);
			
			file = new File(newpath+fileName);
			
			file = getOnlyfile(fileName,file,newpath);
		}
		
		return file;
		
	}
	
	
	
	
	public static String showFilelength(long  thelenth){
		
		
		String str =null;
		if(thelenth>=1024){
			thelenth = thelenth/1024;
			if(thelenth>1024){
				thelenth = thelenth/1024;
				
				if(thelenth>1024){
					thelenth = thelenth/1024;
					if(thelenth>1024){
						thelenth = thelenth/1024;
						
					}else{
						str = thelenth+"GB";
					}
					
				}else{
					str = thelenth+"MB";
				}
			}else{
				str = thelenth+"KB";
			}
				
		}else{
			str = thelenth+"B";
		}
		
		return str;
		
	}
	
	
	
	
}
