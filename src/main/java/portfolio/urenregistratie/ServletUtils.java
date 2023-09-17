/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import org.apache.maven.surefire.shade.booter.org.apache.commons.lang3.SystemUtils;
import org.apache.commons.io.FileUtils;
import org.javatuples.Triplet;
import static portfolio.urenregistratie.urenregistratieServlet.assignments;

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
    public static String registrationName = null;
    
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
    
    public static String writeSettings() throws IOException {
        try {
            String settingsString = "{";
            settingsString += "\"prevOpened\":" + prevOpened.toPrettyString() + ",\"defaultWorkingDirectory\":";
            if (defaultWorkingDirectory == null) { settingsString += "null}"; }
            else {settingsString += "\"" + defaultWorkingDirectory.replace("\\", "\\\\") + "\"}"; }
            settings = settingsString;
            FileWriter settingsFile = new FileWriter(sourcePath + "\\" + settingsFileName); 
            settingsFile.write(settingsString);
            settingsFile.close();
            return "OK";
        } catch (java.nio.file.NoSuchFileException exception) {
            return "ERROR";
        }
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
        try{
            if (projectString.startsWith("\"\"")) { projectString = projectString.substring(1, projectString.length()); }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode project = mapper.readTree(projectString);
            // check whether file was created by this program and contains the correct 'keys'
            JsonNode registrationNameNode = project.get("registrationName");
            JsonNode workingDirNode = project.get("workingDir");
            JsonNode projectsNode = project.get("projects");
            JsonNode assignmentsNode = project.get("assignments");
            
            if (registrationNameNode == null || workingDirNode == null || projectsNode == null || assignmentsNode == null) { return false; }
            return true;
        } catch (Exception exception) { System.out.println("exception"); return false; }
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
            try{
                registration = new String(Files.readAllBytes(Paths.get(fileName)));
                if (!checkFileValidity(registration)) { return new Triplet<>("", "", "Invalid file, file format incorrect"); }
                currentPath = Paths.get(fileName).toString();
                fileName = Paths.get(fileName).getFileName().toString(); 
            } catch(java.nio.file.NoSuchFileException exception){
                return new Triplet<>("", "", "Invalid file, path does not exist");
            } 
        } else { //find file and read
            String pathToFile = null;
            fileName = fileName.substring(1, fileName.length()-1);
            System.out.println(workingDir.toString());
            if (workingDir != null) {
                try{
                    Iterator<File> fileIterator = FileUtils.iterateFiles(new File(workingDir.toString()), extensions, true);
                    while (fileIterator.hasNext() && pathToFile == null) {
                        File file = fileIterator.next();
                        if (file.getName().equals(fileName)) { pathToFile = file.getPath(); }
                    }
                } catch (java.lang.IllegalArgumentException exception) {}
                
            }
            if (pathToFile == null) {
                if (defaultWorkingDirectory == null) { loadSettings(); }
                if (defaultWorkingDirectory != null && !defaultWorkingDirectory.equals("null")) {
                    try {
                        Iterator<File> fileIterator = FileUtils.iterateFiles(new File(defaultWorkingDirectory), extensions, true);
                        while (fileIterator.hasNext() && pathToFile == null) {
                            File file = fileIterator.next();
                            if (file.getName().equals(fileName)) { pathToFile = file.getPath(); }
                        }
                    } catch (java.lang.IllegalArgumentException exception) {}                    
                }
                if (pathToFile == null) {
                    try{
                        Iterator<File> fileIterator = FileUtils.iterateFiles(new File(rootPath), extensions, true);
                        while (fileIterator.hasNext() && pathToFile == null) {
                            File file = fileIterator.next();
                            if (file.getName().equals(fileName)) { pathToFile = file.getPath(); }
                        }
                    } catch (java.lang.IllegalArgumentException exception) {}                    
                }
            }
            if (pathToFile == null) { return new Triplet<>("", "", "Invalid file, no path"); }
            registration = new String(Files.readAllBytes(Paths.get(pathToFile)));
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
        
        updatePrevOpened(fileName);
        return new Triplet<>(encodedAssignments, encodedProjects, "File opened successfully");
    }

    public static void updatePrevOpened(String fileName) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode newPrevOpened = new ObjectMapper().createArrayNode();
        for (int i = 0; i < prevOpened.size(); i++){
            String prevOpenedFileName = prevOpened.get(i).get("fileName").toString().replaceAll("\"", "");
            if (!prevOpenedFileName.equals(fileName)) {
                newPrevOpened.insert(i, prevOpened.get(i));
            }
        }

        ObjectNode projectFile = mapper.createObjectNode();
        projectFile.put("fileName", fileName);
        projectFile.put("path", currentPath.replace("\\\\", "\\"));
        projectFile.put("date", new Date().toString());
        newPrevOpened.insert(0, projectFile);
        if (newPrevOpened.size() > 5) { newPrevOpened.remove(5); }
        prevOpened = newPrevOpened;
        writeSettings();
    }

    public static String supervisorsToString() {
        ArrayList<String> supervisors = Utils.getSupervisors(assignments);
        if (supervisors.isEmpty()) { return "[]"; }
        String encodedSupervisors = "[";
        for (String supervisor : supervisors) { encodedSupervisors += "\"" + supervisor + "\","; }
        encodedSupervisors = encodedSupervisors.substring(0, encodedSupervisors.length() - 1) + "]";
        return encodedSupervisors;
    }

    static void deleteOldRegistrationFile(String oldFileName) throws IOException {
        Path fileToDelete = Paths.get(ServletUtils.workingDir + "\\" + oldFileName + ".json");
        Files.delete(fileToDelete);
    }
}
