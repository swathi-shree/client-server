import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDir {


    public String listFiles(String startDir, String format) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(startDir))) {
            // c
            String allFiles = "+" + startDir+"\r\n" ;

            File dir = new File(startDir);
            File[] files = dir.listFiles();

            // find only regular files
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

          //  result.forEach(System.out::println);

            if(format.equals("F")){
                // Get the names of the files by using the .getName() method
                for (int i = 0; i < files.length; i++) {

                    //c
                    String file = result.get(i).replace(startDir, "");
                    allFiles = allFiles + file + "\r\n";

                //    String all =   System.out.println(files[i].getName());
                //    System.out.println("filedir:if:f =");
            }
            }
            else if(format.equals("V")) {
                for(int i = 0; i < files.length; i++) {
                    //c
                    String file = result.get(i).replace(startDir, "");


                    BasicFileAttributes info = Files.readAttributes(Paths.get(files[i].getPath()), BasicFileAttributes.class);

                    DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    String creationTime = "    file created time: " + df.format(info.creationTime().toMillis());
                    String lastAccessTime = "    file last accessed time: " + df.format(info.lastAccessTime().toMillis());
                    String lastModifiedTime = "    file last modified time: " + df.format(info.lastModifiedTime().toMillis());

                    allFiles = allFiles + file + creationTime + lastAccessTime + lastModifiedTime + "\r\n";


                  //  String all =  System.out.println(file.getName() + " (size in bytes: " + file.length()+")");
                  //  System.out.println("filedir:elseif:v =");
                }

        } } catch (IOException e) {
            return "-Invalid directory";
        }
        return null;
    }

}