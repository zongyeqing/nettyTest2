package netty.rpc.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 请填写类注释
 *
 * @author 宗业清
 * @since 2018年03月23日
 */
public class ScanPackage {
    
    private String basePath;

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Class> scan(){
        List<Class> classes = new ArrayList<Class>();
       String projectPath = ScanPackage.class.getResource("/").getPath();
       String packagePath = projectPath + basePath.replace(".", "/");
       File file = new File(packagePath);
       List<String> allPath = getAllPath(file);
       for (String fileName : allPath) {
           String className = fileName.replace("\\", "/")
           .replace(projectPath.substring(1), "")
           .replace("/", ".")
           .replace(".class", "");
           try {
               classes.add(Class.forName(className));
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
       }
       return classes;
    }
    
    private List<String> getAllPath(File file){
        List<String> fileNames = new ArrayList<String>();
        File[] files = file.listFiles();
        for (File subFile : files) {
            if (subFile.isDirectory()) {
                fileNames.addAll(getAllPath(subFile));
            } else {
                fileNames.add(subFile.getPath());
            }
        }
        return fileNames;
    }
}
