package com.seda.payer.ottico.facade.unittest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.seda.payer.commons.utility.GZiper;

public class TestZipFiles {

	public static void main(String[] args) {

		String[] files = new String[] { "\\\\10.10.9.106\\BirtReport\\iargenio\\74545b0b-49c6-4c1d-8f93-b93565c6c88c_1310214818334_NAB.pdf", 
										"\\\\10.10.9.106\\BirtReport\\iargenio\\74545b0b-49c6-4c1d-8f93-b93565c6c88c_1310215929037_NAB.pdf" };
		try {
			System.out.println("1. ZIP test start");
			
			//ok zipFiles(files, "aaa.zip", "\\\\10.10.9.106\\BirtReport\\iargenio\\");
			GZiper.zipFiles(files, "aaa.zip", "\\\\10.10.9.106\\BirtReport\\iargenio\\");
//			ZipOutputStream out = new ZipOutputStream(new FileOutputStream("\\\\10.10.9.106\\BirtReport\\iargenio\\pippo.zip"));
//			int byteToRead = 2048;
//			byte data[] = new byte[byteToRead];
//			File f = new File("\\\\10.10.9.106\\BirtReport\\iargenio\\74545b0b-49c6-4c1d-8f93-b93565c6c88c_1310214818334_NAB.pdf");
//			FileInputStream fi = new FileInputStream(f);
//			BufferedInputStream origin = new BufferedInputStream(fi, byteToRead);
//			ZipEntry entry = new ZipEntry(f.getName());
//			out.putNextEntry(entry);
//			int byteToWrite;
//			while((byteToWrite = origin.read(data, 0, byteToRead)) != -1) {
//				out.write(data, 0, byteToWrite);
//				out.flush();
//			}
//			out.flush();
//			out.close();
			
//			GZiper.zipFiles(files, "\\\\10.10.9.106\\BirtReport\\iargenio\\", "pippo.zip");
			System.out.println("1. ZIP test end");
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void zipFiles(String[] files, String zipName, String zipOutDir) throws Exception{
		ZipOutputStream out = null;
		FileInputStream fi = null;
		BufferedInputStream origin = null;
		try {
			int byteToRead = 2048;
			FileOutputStream dest = new FileOutputStream(zipOutDir + zipName);
			out = new ZipOutputStream(new BufferedOutputStream(dest));
			for(int i = 0; i < files.length; i++) {
				byte data[] = new byte[byteToRead];
				File f = new File(files[i]);
				fi = new FileInputStream(f);
				origin = new BufferedInputStream(fi, byteToRead);
				ZipEntry entry = new ZipEntry(f.getName());
				out.putNextEntry(entry);
				int byteToWrite;
				while((byteToWrite = origin.read(data, 0, byteToRead)) != -1) {
					out.write(data, 0, byteToWrite);
					out.flush();
				}
			}
		} finally {
			if (fi != null) fi.close();
			if (out != null) out.close();
			if (origin != null) origin.close();
		}
	}
}