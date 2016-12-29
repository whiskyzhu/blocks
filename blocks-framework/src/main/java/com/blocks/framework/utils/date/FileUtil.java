/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 *
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * 
 * $Tracker List
 * 
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create 
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5 
 *  
 *  
 */
package com.blocks.framework.utils.date;

import java.io.*;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: Ztesoft
 * </p>
 * 
 * @author lu.zhen
 * @version 1.0
 */
public final class FileUtil {

	private FileUtil() {

	}

	/**
	 * 读文�?
	 * 
	 * @param fileName
	 *            String
	 * @return byte[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static byte[] loadFile(String fileName)
			throws FileNotFoundException, IOException {
		byte[] retBytes = new byte[0];
		File file = new File(fileName);

		FileInputStream fs = new FileInputStream(fileName);
		int fileLen = (int) file.length();
		retBytes = new byte[fileLen];
		fs.read(retBytes);
		fs.close();

		return retBytes;
	}

	/**
	 * 写文�?
	 * 
	 * @param fileName
	 *            String
	 * @param data
	 *            byte[]
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeFile(String fileName, byte[] data)
			throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileOutputStream fs = new FileOutputStream(file);
		fs.write(data);
		fs.close();
	}

	/**
	 * 读文本文�?
	 * 
	 * @param fileName
	 *            String
	 * @return String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String loadTxtFile(String fileName)
			throws FileNotFoundException, IOException {
		byte[] bfs = loadFile(fileName);
		String ret = new String(bfs, "UTF-8");
		return ret;
	}

	/**
	 * 写文本文�?
	 * 
	 * @param fileName
	 *            String
	 * @param data
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeTxtFile(String fileName, String data)
			throws FileNotFoundException, IOException {
		byte[] ofs = data.getBytes("UTF-8");
		writeFile(fileName, ofs);
	}

	/**
	 * 向指定文件写入指定的内容
	 * 
	 * @param filename
	 * @param data
	 * @throws Exception
	 */
	public static void writeTxtFile(String filename, String data, boolean mode)
			throws FileNotFoundException, IOException {
		if (null == filename || 0 == filename.length()) {
			return;
		}

		if (null == data || 0 == data.length()) {
			return;
		}

		File file = new File(filename);

		DataOutputStream out = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			// 设置追加模式
			out = new DataOutputStream(new FileOutputStream(file, mode));

			out.writeBytes(data);
			out.flush();
		} finally {
			try {
				if (null != out) {
					out.close();
				}
			} catch (Exception e) {
				return;
			}
		}
	}

	public static int FILE_TYPE = 1;

	public static int FOLDER_TYPE = 2;

	public static int NULL_TYPE = -1;

	
    /**
     * 获取文件扩展名
     * 
     * @param fileName
     * @return
     */
    public static String getFileExtName(String fileName)
    {
        String extName = null;
        int dotIndex = fileName.indexOf(".");
        if (dotIndex > -1) {
            return fileName.substring(dotIndex, fileName.length());
        }
        return extName;

    }
    /**dir exist or not ,create it .
     * @author tony
     * @param newPath
     */
    public static void createDirIfNoExists(String newPath)
    {
    	 File dir = new File(newPath) ;
    	 if(!dir.exists())
    	 dir.mkdirs(); // 如果文件夹不存在 则建立新文件  
    }
    
    /** 
     * 删除文件 
     * @author tony
     * @param myDelFile 
     *            文本文件完整绝对路径及文件名 
     * @return Boolean 成功删除返回true遭遇异常返回false 
     */  
    public static  boolean deleteFile(File myDelFile) {  
        boolean bea = false;  
        try {  
             
            if (myDelFile.exists()) {  
                myDelFile.delete();  
                bea = true;  
            } else {  
                bea = false;  
              System.out.println("删除文件操作出错");  
            }  
        } catch (Exception e) {  
        	System.out.println(e.toString());  
        }  
        return bea;  
    }
    
    /** 
     * 复制单个文件 
     * @author tony
     * @param oldFile 
     *            准备复制的文件源 
     * @param newFile 
     *            拷贝到新绝对路径带文件名 
     * @return 
     */  
    public static void copyFile(File oldFile, File newFile) {  
        try {  
            int bytesum = 0;  
            int byteread = 0;  
            if (oldFile.exists()) { // 文件存在  
                InputStream inStream = new FileInputStream(oldFile.getAbsolutePath()); // 读入源文件  
                FileOutputStream fs = new FileOutputStream(newFile.getAbsolutePath());  
                byte[] buffer = new byte[1444];  
                while ((byteread = inStream.read(buffer)) != -1) {  
                    bytesum += byteread; // 字节 文件大小  
                    System.out.println(bytesum);  
                    fs.write(buffer, 0, byteread);  
                }  
                inStream.close();  
            }  
        } catch (Exception e) {  
            System.out.println("复制单个文件操作出错");  
        }  
    }
    public static void main(String[] args) throws IOException {
		File file =new File("d:/text.txt");
		if(!file.exists())
		{
			
			file.createNewFile() ;
		}
		System.out.println(file.getAbsolutePath());
	}
}
