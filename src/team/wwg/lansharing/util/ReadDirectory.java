package team.wwg.lansharing.util;

import java.io.File;
import java.util.ArrayList;

import team.wwg.lansharing.ui.FileWnd;  

public class ReadDirectory {  
        // 文件所在的层数  
        private int fileLevel;  
        ArrayList<String> FlieList;
  
        ReadDirectory()
        {
        	FlieList=new ArrayList<String>();
        }
        /** 
         * 生成输出格式 
         * @param name 输出的文件名或目录名 
         * @param level 输出的文件名或者目录名所在的层次 
         * @return 输出的字符串 
         */  
        public String createPrintStr(String name, int level) {  
                // 输出的前缀  
                String printStr = "";  
                // 按层次进行缩进  
                for (int i = 0; i < level; i ++) {  
                        printStr  = printStr + "  ";  
                }  
                printStr = level + " " + name;  
                return printStr;  
        }  
  
  
        /** 
         * 输出给定目录下的文件，包括子目录中的文件 
         * @param dirPath 给定的目录 
         */  
        public void readFile(String dirPath) {  
                // 建立当前目录中文件的File对象  
                File file = new File(dirPath);  
                // 取得代表目录中所有文件的File对象数组  
                File[] list = file.listFiles();  
                // 遍历file数组  
                for (int i = 0; i < list.length; i++) {  
                        if (list[i].isDirectory()) {
                        	FlieList.add(createPrintStr(list[i].getName(), fileLevel));
                                //System.out.println(createPrintStr(list[i].getName(), fileLevel));  
                                fileLevel ++;  
                                // 递归子目录  
                                readFile(list[i].getPath());  
                                fileLevel --;  
                        } else {  
                        	FlieList.add(createPrintStr(list[i].getName(), fileLevel));
                                //System.out.println(createPrintStr(list[i].getName(), fileLevel));  
                        }  
                }  
        }  
          
        public static void main(String[] args) {  
                ReadDirectory rd = new ReadDirectory();  
                String dirPath = "E:\\tempworkspace";   
                rd.readFile(dirPath);  
                FileWnd a = new FileWnd(rd.FlieList);
        }  
}  
