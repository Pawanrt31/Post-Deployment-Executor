package com.demo.dummy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;

import com.demo.automation.config.DataConfig;

public class FileService {
	
	
	
	public static void automateExecution(String name) {
        List<String> directories = getDirectories(name);
       if(directories.contains("java")) {
        	//List<File> files = getFiles(name + "/java",".jar");
            //List<File> files = FilenameFilter(name + "/java/",".jar");
//            files.stream().forEach(file -> {
//            	executeFile("java -jar " + file.getAbsolutePath());
//            });
        	//System.out.println(this.resourceFile);
//            for(File file: files) {
//            	executeFile("java -jar " + file.getAbsolutePath());
//           }
        	List<File> files = getFiles(name + "/java",".jar");
        	
        	List<String> fileNames = new ArrayList<String>();
        	files.stream().forEach(file -> {
        		if(file.isFile()) {
        			fileNames.add(file.getName());
        		}
        	});
        	JSONParser parser = new JSONParser();
        	try
        	{
        		JSONObject jsonObject = (JSONObject)(parser.parse(new FileReader(FileService.getCanonical(name)+"/java/sequence.json")));
	        	List<String> file1 = new ArrayList<String>();
	        	JSONArray jar = (JSONArray)jsonObject.get("jars");
	        	Iterator<String> iterator = jar.iterator();
	        	while (iterator.hasNext()) {
	        		file1.add((String) iterator.next());
	        	}
	        	//System.setProperty("user.dir", System.getProperty("user.dir") + DataConfig)
	        	//System.getProperty(key)
	        	file1.stream().forEach(name2 -> {
	        		executeFile("java -jar " + name2);
	        	});
	        	if((fileNames.size() - file1.size())!=0) {
		        	List<String> remainingFiles = fileNames.stream()
		        			.filter(element -> !file1.contains(element))
		        			.collect(Collectors.toList());
		        	remainingFiles.stream().forEach(nameFile -> {
		        		executeFile("java -jar " + FileService.getCanonical(name) + "\\java\\"+nameFile);
		        	});
	        	}
	        }
        	catch(Exception e) {
        	e.printStackTrace();
        	
        }
        	
        }else if(directories.contains("python")) {
            List<File> files = getFiles(name + "/python/",".py");
            for(File file: files) {
            executeFile("py " + file.getAbsolutePath());
            }
        }else if(directories.contains("bat")) {
            List<File> files = getFiles(name + "/bat/",".bat");
            for(File file:files) {
            executeFile(file.getAbsolutePath());
            }
        }
	}
	
	public static String getCanonical(String path) {
		try {
			return new File(path).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static List<String> getDirectories(String path) {
//		URL url = getClass().getResource("philips");
//		File filePath = new File(url.getPath());
        String name;
		File filePath = new File(getCanonical(path));
		List<String> directories = new ArrayList<String>();
		if(filePath.exists()){
			List<File> files = Arrays.asList(filePath.listFiles());
		    files.stream().forEach(file -> {
		        if (file.isDirectory()) {
		            directories.add(file.getName());
		        }
		    });
		}else {
		    System.out.println("File path does not exist");
		}
		return directories; 
    }
	private static List<File> getFiles(String path, String extension) {
        List<File> files = new ArrayList<File>();
        File filePath = new File(path);
        if(filePath.exists()){
        	List<File> fileList = Arrays.asList(filePath.listFiles());
            fileList.stream().forEach(file -> {
                if (file.getName().endsWith(extension)) {
                    files.add(file);
                }
            });
        }else {
            System.out.println("File path does not exist");
        }
        return files;
    }
	private static void executeFile(String command){
    	Process proc = null;
        try {
            InputStream in,error;
            String line;
            proc = Runtime.getRuntime().exec(command);
            in = proc.getInputStream();
            error = proc.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            line = null;
            System.out.println("Output");
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }

            System.out.println("Errors");
            reader = new BufferedReader(new InputStreamReader(error));
            line = null;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }

            proc.waitFor();
            System.out.println(proc.exitValue());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
        	if(proc!=null) {
        		long pid = proc.pid();
        		try {
					Runtime.getRuntime().exec("taskkill /F /IM "+pid);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		proc.destroy();
        	}
        }
    }
//	public static void main(String[] args) {
//		System.out.println(FileService.getDirectories(""));
//	}
}
