/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import org.apache.maven.surefire.shade.booter.org.apache.commons.lang3.SystemUtils;
import org.apache.commons.io.FileUtils;
import org.javatuples.Triplet;

/**
 *
 * @author RLvan
 */
public class ServletUtils {
    public static String defaultWorkingDirectory = null;
    public static String settings = null;
    public final static String settingsFileName = "urenregistratie_settings.json";
    public static String rootPath = "";
    public static String sourcePath = null;
    public static ArrayNode prevOpened = new ObjectMapper().createArrayNode();
    public static String programFilesPath = "";
    public static Path workingDir = null;
    public static String currentPath = "";
    public final static String[] extensions = new String[]{"json"};
    public static String registrationName;
    
    public static String getBody(HttpServletRequest request) throws IOException {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
    
    public static ArrayList<String> getStringArrayListFromRequestBody(HttpServletRequest request) throws IOException{
        String requestBody = getBody(request);
        requestBody = requestBody.substring(2, requestBody.length() - 2); // remove []
        String[] items = requestBody.split(",");
        ArrayList<String> requestArrayList = new ArrayList<>();
        for (String item : items){
            requestArrayList.add(item.trim());
        }
        return requestArrayList;
    }
    
    public static void loadSettings() throws IOException{
        if (settings == null || defaultWorkingDirectory == null){
            if (rootPath == "") {
                determineOS();
            }
            String fileLocation = sourcePath + "\\" + settingsFileName;
//            System.out.println(fileLocation);
            Path path = Paths.get(fileLocation);
            if (Files.exists(path)) {
                settings = new String(Files.readAllBytes(path));
            } else { 
                writeSettings(); // if it isn't where it's supposed to be, we just write a new file ('cuase it doesn't exist or has been moved)
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode settingsNode = mapper.readTree(settings);
            prevOpened = (ArrayNode) settingsNode.get("prevOpened");
            defaultWorkingDirectory = settingsNode.get("defaultWorkingDirectory").asText();
        }
    }
    
    public static Boolean determineOS(){
        Boolean OSDetermined = true;
        if (programFilesPath.equals("") || rootPath.equals("")){
            if (SystemUtils.IS_OS_WINDOWS){
                rootPath = "C:\\";
                programFilesPath = "C:\\Program Files";
            } else if (SystemUtils.IS_OS_MAC){
                rootPath = "/";
                programFilesPath = "/Applications";
            } else if (SystemUtils.IS_OS_LINUX){
                rootPath = "/";
                programFilesPath = "/opt";
            } else {
                OSDetermined = false;
            }
        }
        if (workingDir == null) { workingDir = Paths.get(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()); }
        return OSDetermined;
    }
    
    public static void writeSettings() throws IOException {
        String settingsString = "{";
        settingsString += "\"prevOpened\":" + prevOpened.toPrettyString() + ",\"defaultWorkingDirectory\":";
        if (defaultWorkingDirectory == null) { settingsString += "null}"; }
        else {settingsString += "\"" + defaultWorkingDirectory.replace("\\", "\\\\") + "\"}"; }
        settings = settingsString;
        FileWriter settingsFile = new FileWriter(sourcePath + "\\" + settingsFileName); 
        settingsFile.write(settingsString);
        settingsFile.close();
    }
    
    public static String readProjectFromFile() throws IOException{
        if (!currentPath.equals("")) {
            String file =  new String(Files.readAllBytes(Paths.get(currentPath)));
            String fileContent = file.substring(0, file.length());
            if (checkFileValidity(file)){
                return fileContent;
            } else {
                return "file invalid";
            }
        } else {
            return "null";
        }
    }
    
    public static Boolean checkFileValidity(String projectString) throws JsonProcessingException{
        return true;
        // todo: implement
//        if (projectString.startsWith("\"")) { projectString = projectString.substring(1, projectString.length()); }
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode project = mapper.readTree(projectString);
//        // check whether file was created by this program and contains the correct 'keys'
//        JsonNode projectName = project.get("title");
//        JsonNode workingDir = project.get("workingDir");
//        JsonNode database = project.get("databaseFileName");
//        JsonNode groups = project.get("groups");
//        
//        if (projectName == null || workingDir == null || database == null || groups == null) { return false; }
//        return true;
     }
    
    public static Boolean directoryExists(HttpServletRequest request) throws IOException {
        String folder = getBody(request).replace("\\\\", "\\").replace("\"", "");
        Path path = Paths.get(folder);
        return Files.exists(path);
    }
    
    public static Triplet<String, String, String> openProject(HttpServletRequest request) throws IOException{
        Boolean determinationSuccess = determineOS();
        String encodedAssignments = null;
        String encodedProjects = null;
        if (!determinationSuccess) { return new Triplet<>("", "", "Unsupported OS"); }
        loadSettings();
        String fileName = getBody(request);
        String registration = null;
        if (fileName.contains("\\")) { 
            fileName = fileName.replace("\\\\", "\\");
            fileName = fileName.substring(1, fileName.length()-1); }
        if (fileName.startsWith(rootPath)) { // fully specified path
            registration = new String(Files.readAllBytes(Paths.get(fileName)));
            if (!checkFileValidity(registration)) { return new Triplet<>("", "", "Invalid file, not DBVis"); }
            currentPath = fileName;
            fileName = Paths.get(fileName).getFileName().toString();
            
        } else { //find file and read
            String pathToFile = null;
            fileName = fileName.substring(1, fileName.length()-1);
            if (workingDir != null) {
                Iterator<File> fileIterator = FileUtils.iterateFiles(new File(workingDir.toString()), extensions, true);
                while (fileIterator.hasNext() && pathToFile == null) {
                    File file = fileIterator.next();
                    if (file.getName().equals(fileName)) { pathToFile = file.getPath(); }
                }
            }
            if (pathToFile == null) {
                if (defaultWorkingDirectory == null) { loadSettings(); }
                if (defaultWorkingDirectory != null) {
                    Iterator<File> fileIterator = FileUtils.iterateFiles(new File(defaultWorkingDirectory), extensions, true);
                    while (fileIterator.hasNext() && pathToFile == null) {
                        File file = fileIterator.next();
                        if (file.getName().equals(fileName)) { pathToFile = file.getPath(); }
                    }
                }
                if (pathToFile == null) {
                    Iterator<File> fileIterator = FileUtils.iterateFiles(new File(rootPath), extensions, true);
                    while (fileIterator.hasNext() && pathToFile == null) {
                        File file = fileIterator.next();
                        if (file.getName().equals(fileName)) { pathToFile = file.getPath(); }
                    }
                }
            }
            if (pathToFile == null) { return new Triplet<>("", "", "Invalid file, no path"); }
            registration = new String(Files.readAllBytes(Paths.get(pathToFile)));
            registration = registration.substring(1, registration.length() - 1);
            if (!checkFileValidity(registration)) { return new Triplet<>("", "", "Invalid file, file format incorrect"); }
            currentPath = pathToFile;
        }
        
        Pattern openRegistrationPattern = Pattern.compile("\"registrationName\":\"(.*)\",\"workingDir\":\"(.*)\",\"projects\":(.*),\"assignments\":(.*)");
        Matcher openRegistrationMatcher = openRegistrationPattern.matcher(registration);
        Boolean openRegistrationMatchFound = openRegistrationMatcher.find();
        if (openRegistrationMatchFound){
            registrationName = openRegistrationMatcher.group(1);
            workingDir = Paths.get(openRegistrationMatcher.group(2));
            encodedAssignments = openRegistrationMatcher.group(4);
            encodedProjects = openRegistrationMatcher.group(3);
        } else { return new Triplet<>("", "", "Invalid file, possible file corruption"); }
        
        return new Triplet<>(encodedAssignments, encodedProjects, "File opened successfully");
    }
}
