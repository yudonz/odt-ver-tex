import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class FileBuilder {
	
	private Path outputFile;
	private ArrayList<String> string;

	
	public FileBuilder(ArrayList<String> string, Path outputFile) {
		this.string=string;
		this.outputFile=outputFile;
	}
	
	public void build() throws IOException{
		try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
			writeFileContent(string, writer);
			writer.flush();
		}
	}
	
	private void writeFileContent(ArrayList<String> string, Writer writer)  {
		try {
			for(String s:string) {
				writer.write(s);
				((BufferedWriter) writer).newLine();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println("finish");
	}
	
	
}
