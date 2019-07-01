import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity {

	/*
	 * !! This program runs on windows, maybe can't run on Mac os.
	 * */
	public static void main(String[] args) throws IOException  {
		
		/*
		 * These are the two test files£¬you can change the url
		 * Paths.get(url)
		 * */
//		Path file=Paths.get("C:/Users/57380/Desktop/Odyssey 2018 OpenDocument Template.odt");
		Path file=Paths.get("C:/Users/57380/Desktop/ILASS.odt");
		String filename=file.getFileName().toString();
		String name=filename.substring(0,filename.indexOf("."));
		
		/*
		 * use windows to choose the odt file
		 * */
//		JFileChooser fd = new JFileChooser();
//		fd.showOpenDialog(null);
//		File f = fd.getSelectedFile();
//		System.out.println(f);
//		Path file=Paths.get(f.toString());
//		String filename=file.getFileName().toString();
//		String name=filename.substring(0,filename.indexOf("."));
		
		if(!Files.exists(file))
			System.out.println("The file doesn't exis.");
		else {
//			System.out.println(file.toString());
//			UnZipFile zip=new UnZipFile(f.toString(), file.getParent().toString()+"/");
//			System.out.println(file.getParent().toString());
			UnZipFile zip=new UnZipFile(file.toString(), file.getParent().toString()+"/");
			zip.unZipFiles();
			
			Path xml=file.getParent().resolve(name).resolve("content.xml");
			Path dir=file.getParent().resolve(name);
			if(Files.exists(xml)) {
				System.out.println(dir);
				Document doc = Jsoup.parse(new File(xml.toString()), "utf-8");
				FileReader f1=new FileReader(doc,dir);
				f1.build();
				
				ArrayList<String> string=new ArrayList<String>();
				string=f1.getContent();	
				for(String s:string) {
					System.out.println(s);
				}
				
//				Path outputFile=file.getParent().resolve(name).resolve("out.tex");
				Path outputFile=file.getParent().resolve("out.tex");
				FileBuilder p1=new FileBuilder(string,outputFile);
				p1.build();
				
			}
			
		}			
	}
	
	public static String transformTerm(String s, String oldTerm,  String newTerm){
        String[] temporaire = s.split(oldTerm);
        s = temporaire[0];
        int i;
        for (i=1;i<temporaire.length;i++){
            s = s + newTerm + temporaire[i];
        }
        return s;
	 }
	
}
