package Hack;

import com.cloudinary.Cloudinary;
import com.cloudinary.SmartUrlEncoder;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VideoTrim {
    private int division = 20;
    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "alpinoo",
            "api_key", "518696137578651",
            "api_secret", "NxgHy-dKIC5bHVb1l3dHhWLSxZ8"));


    public int getDivision(){
        return division;
    }
    public VideoTrim(String path, GUI gui) throws IOException {
        deleteFolder(new File("C:/hack/video"));
        File toUpload = new File(path);

    for (int i = 0; i< 100;i+=division){
          System.out.println("►Fetching and Decoding... "+i+"%");
        gui.updateConsole("►Fetching and Decoding... "+i+"%\n");
          int yay = (int)(Math.random()*2000000000+1);
        System.out.println("Seed = "+yay);
        gui.updateConsole("Seed = "+yay+"\n");
          cloudinary.uploader().upload(  toUpload,
                  ObjectUtils.asMap("resource_type", "video","public_id",""+yay,"folder","videosss","unique_filename","false","transformation",(new Transformation().startOffset(""+i+"%").duration(""+(division)+"%"))));
            String url = cloudinary.url().videoTag("videosss/"+yay,
                    ObjectUtils.asMap("resource_type", "video","public_id",""+yay,"folder","videosss","unique_filename","false","transformation",
                            (new Transformation().startOffset(""+i+"%").duration(""+(division)+"%")))).toString();
           url = getUrl(url);
        String saveDir = "C:/hack/video";
        HttpDownloadUtility.downloadFile(url, saveDir, gui);
        File file = new File(saveDir+"/"+url.substring(57));

        File renamed = new File(saveDir+"/"+"Video_"+((i/division)+1)+".mp4");
        file.renameTo(renamed);
        file.delete();
        if(i%60 == 0 && i!=0){
            gui.clearConsole();
        }
          System.out.println(url+"\n");
            gui.updateConsole(url+"\n");

      }


        System.out.println("Video trimmed");
        gui.updateConsole("Video trimmed\n");

        //System.out.println(cloudinary.url().fromIdentifier("25"));
    }
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }
    public String getUrl(String url){
        String[] data = url.split("'");
        for (String str: data) {
            if (str.endsWith("mp4"))
                return str;
        }
        return null;
    }



    }

