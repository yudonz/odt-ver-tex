import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * This class is to analyse all the tags and contents of the content.xml file.
 * */
public class FileReader {
	/*
	 * au : the position of author in the latex
	 * keyword : the position of keyword in the latex
	 * mk : the position of make title in the latex
	 * */
	private Path root;
	private Document doc;
	private boolean isAb=false,isKw=false,isTC=false;
	private String author="",tname="";
	private int au=0,keyword,mk,tcolumn=1;
	private ArrayList<String> string=new ArrayList<String>();
	
	public FileReader(Document doc) {
		this.doc=doc;		
	}	
	
	public FileReader(Document doc,Path root) {
		this.doc=doc;		
		this.root=root;
	}
	
	public ArrayList<String> getContent() {
		return string;
	}
	
	public void build() {
		Elements es=doc.select("office|text>*");
		for(Element e:es) {			
			faireElement(e);
		}	
		mk=mk==0?au:mk;
		string.add(mk,"\\maketitle");
		string.add(mk,"\\date{}");
		string.add(au,"\\author{"+author+"}");
		string.add(0,"\\begin{document}");
		string.add(0,"\\renewcommand{\\tablename}{Table}");
		string.add(0,"\\renewcommand\\refname{}");
		
		string.add(0,"    \\vskip 2.5ex}{\\par\\vskip 2.5ex}");
		string.add(0,"    \\noindent\\mbox{}\\hfill{\\bfseries \\enabstractname}\\hfill\\mbox{}\\par");
		string.add(0,"    \\par\\large");
		string.add(0,"\\newenvironment{enabstract}{%");
		string.add(0,"\\newcommand{\\enabstractname}{Abstract}");	
		
		string.add(0,"\\usepackage{authblk}");
		string.add(0,"\\usepackage{float}");
		string.add(0,"\\usepackage[space]{ctex}");
		if(tcolumn==2)
			string.add(0,"\\documentclass[11pt, a4paper,twocolumn]{article}");
		else
			string.add(0,"\\documentclass[11pt, a4paper]{article}");
		string.add("\\end{document}");	
	}
		
	public void faireElement(Element e) {
		String tag=e.tagName();
		if(tag.indexOf("text:p")!=-1) 
			textP(e);	
		else if(tag.indexOf("text:h")!=-1) 
			textH(e);
		else if(tag.indexOf("table:table")!=-1) 
			table(e);
		else if(tag.indexOf("text:list")!=-1)
			list(e);
		else if(tag.indexOf("office:forms")!=-1) 
			System.out.println("");
		else if(tag.indexOf("text:sequence-decls")!=-1) 
			System.out.println("");
		else {
			if(tag.equals("text:section")) {
				String styleName=e.attr("text:style-name");
				Elements styles=doc.select("office|automatic-styles>*");
				for(Element estyle:styles) {
					if(e.text().length()!=0 && estyle.attr("style:name").equals(styleName)) {
						tcolumn=estyle.select("style|column").size();
					}
				}
			}
			
			Elements es=e.select(">*");
			for(Element e1:es)
				faireElement(e1);
		}
	}	
	
	public void textH(Element e) {
		String styleName=e.attr("text:style-name");
		if(e.text().toLowerCase().indexOf("abstract")!=-1)
			isAb=true;
		else if(e.text().toLowerCase().indexOf("keyword")!=-1)
			isKw=true;
		else if(e.attr("text:style-name").toLowerCase().indexOf("titre")!=-1)
			string.add("\\section"+"{"+e.text()+"}");
		else if(styleName.substring(0,1).equals("P")&&styleName.length()<=4){
			Elements styles=doc.select("office|automatic-styles>*");
			for(Element estyle:styles) {
				if(e.text().length()!=0 && estyle.attr("style:name").equals(styleName)) {
					String psn=estyle.attr("style:parent-style-name");
					if(psn.equals("Heading_20_1"))
						string.add("\\section"+"{"+e.text()+"}");
					else if(psn.equals("Heading_20_2"))
						string.add("\\subsection"+"{"+e.text()+"}");
				}
			}
		}
		
	}
	
	public void textP(Element e) {
		String styleName=e.attr("text:style-name");
		if(styleName.indexOf("Title")!=-1) {
			string.add("\\title"+"{"+e.text()+"}");	
			au++;
		}
		else if(styleName.indexOf("Author")!=-1||styleName.indexOf("作者")!=-1) {
//			string.add("\\author"+"{"+e.text()+"}");
			if(author.equals("")&&e.text().length()!=0)
				author=e.text();
			else if(e.text().length()!=0)
				author=author+"\n\\\\"+e.text();
		}
		else if(styleName.indexOf("Abstract")!=-1) {
			isAb=true;
		}
		else if(styleName.equals("Affiliation")) {
			affiliation(e);
		}
		else if(isAb) {	
			string.add("\\begin{enabstract}\n"+e.text());
			keyword=string.size();
			string.add("\\end{enabstract}");	
			isAb=false;
		}
		else if(isKw) {
			string.add(keyword,"\\textbf{Keywords: }"+e.text());
			string.add(keyword,"\\newline");
			string.add(keyword,"\\newline");
			isKw=false;			
		}
		else if(styleName.equals("Standard")&&e.text().length()!=0) {
			string.add(e.text());	
		}
		else if(styleName.indexOf("Text")!=-1&&e.text().length()!=0) {
			string.add("\\par");
			string.add(e.text());	
		}
		else if(styleName.equals("Picture")) {
			picture(e);
		}
		else if(styleName.indexOf("Table")!=-1
				&&styleName.toLowerCase().indexOf("caption")!=-1) {
			tname=e.text();
			isTC=true;
		}
		else if(styleName.indexOf("Figure")!=-1
				&&styleName.toLowerCase().indexOf("caption")!=-1) {
			string.add("\\begin{center}");
			string.add(e.text());
			string.add("\\end{center}");
		}
		//If the tag contains 'p'
		else if(styleName.substring(0,1).equals("P")&&styleName.length()<=4){
//			System.out.println("这里");
			Elements styles=doc.select("office|automatic-styles>*");
			for(Element estyle:styles) {
				if(e.text().length()!=0 && estyle.attr("style:name").equals(styleName)) {
					if(estyle.attr("style:parent-style-name").indexOf("Title")!=-1
							||estyle.attr("style:parent-style-name").indexOf("Titre")!=-1) {
						string.add("\\title"+"{"+e.text()+"}");		
						au++;
					}
					else if(estyle.attr("style:parent-style-name").equals("Author")) {
//						string.add("\\author"+"{"+e.text()+"}");
						if(author.equals("")&&e.text().length()!=0)
							author=e.text();
						else if(e.text().length()!=0)
							author=author+"\n\\\\"+e.text();
					}
					else if(estyle.attr("style:parent-style-name").indexOf("Abstract")!=-1) {
						isAb=true;
					}
					else if(isAb) {	
						string.add("\\begin{enabstract}\n"+e.text());
						keyword=string.size();
						string.add("\\end{enabstract}");	
						isAb=false;
					}
					else if(isKw) {
						string.add(keyword,"\\textbf{Keywords: }"+e.text());
						string.add(keyword,"\\newline");
						string.add(keyword,"\\newline");
						isKw=false;
					}
					else if(estyle.attr("style:parent-style-name").equals("Affiliation")) {
						affiliation(e);
					}
					else if(estyle.attr("style:parent-style-name").equals("Text")
							&&estyle.attr("style:family").equals("paragraph")) {
						string.add("\\par");
						string.add(e.text());
					}
					else if(estyle.attr("style:parent-style-name").equals("Picture")
							||estyle.attr("style:parent-style-name").equals("FigureNorm")) {
						picture(e);
					}
					else if(estyle.attr("style:parent-style-name").equals("Equation")) {
						try {
							equation(e);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else
						string.add(e.text());		
				}		
			}
		}

		
	}
	
	public void table(Element e) {
		string.add("\\begin{table}[H]");
		string.add("\\centering");
		if(isTC=true) {
			string.add("\\caption{"+tname+"}");
			isTC=false;
		}
//		int columnCount=e.select("table|table-column").size();
		int columnCount=e.select("table|table-row").get(0).select("table|table-cell").size();
//		System.out.println("columnCount："+columnCount);
		String out="\\begin{tabular}{|";
		for(int i=0;i<columnCount;i++) {
			out=out+"l|";
		}
		out=out+" p{5cm}|} \n\\hline";
		string.add(out);
		
		String tr="";
		Elements row=e.select("table|table-row");

		for(Element row1:row) {
			tr="";
			Elements column=row1.select("table|table-cell");
//			System.out.println("列数："+column.size());
			for(int i=0;i<columnCount;i++) {
				if(i==columnCount-1) {
					tr=tr+column.get(i).text()+" \\\\ \\hline";
				}
				else {
//					System.out.println(column.get(i).text());
					tr=tr+column.get(i).text()+" & ";;
				}					
			}		
			string.add(tr);
		}
		string.add("\\end{tabular}");
		string.add("\\label{tab:my_label}");
		string.add("\\end{table}");

//		table的标题已加
	}
	
	
	public void list(Element e) {
		String styleName;
		styleName=e.attr("text:style-name");
//		System.out.println(styleName);
		boolean list=false;
		Elements styles=doc.select("office|automatic-styles>*");
		for(Element estyle:styles) {
			if(estyle.tagName().equals("text:list-style")&&estyle.attr("style:name").equals(styleName)) {
//				System.out.println("这里1"+estyle.select(">*").get(0).attr("text:bullet-char "));
				list=true;
				listItem(e);
//				这里 break?
			}
		}
		if(list==false) {
			for(Element estyle:styles) {
				if(estyle.attr("style:list-style-name").equals(styleName)) {
//					System.out.println("这里2");
					if(estyle.attr("style:parent-style-name").indexOf("Heading")!=-1) {
						listList(e);	
//						System.out.println("这里3");
					}
					else if(estyle.attr("style:parent-style-name").indexOf("Reference")!=-1) {
						reference(e);
					}
					else if(estyle.attr("style:parent-style-name").indexOf("Bullet")!=-1) {
						listItem(e);
					}
					break;
				}
			}	
		}
			
		
		
	}
	
	private void listItem(Element e) {
//		System.out.println("开始");
		Elements elist=e.select(">text|list-item");
		string.add("\\begin{itemize}");
//		System.out.println(elist.size());
		for(Element item:elist) {
			if(item.select(">*").get(0).tagName().equals("text:p")) {
				string.add("\\item[·]"+item.text());
//				System.out.println(item.text());
			}				
		}
		string.add("\\end{itemize}");		
	}
	
	private void listList(Element e) {
//		System.out.println("开始");
		Element e1,e2,e3;
		if((e1=e.select(">*").get(0).select(">*").get(0)).tagName().equals("text:p")) {
			string.add("\\section"+"{"+e1.text()+"}");
//			System.out.println(e1.text());
		}
		else if((e2=e1.select(">*").get(0).select(">*").get(0)).tagName().equals("text:p")) {
			string.add("\\subsection"+"{"+e2.text()+"}");
//			System.out.println(e2.text());
		}
		else if((e3=e2.select(">*").get(0).select(">*").get(0)).tagName().equals("text:p")) {
			string.add("\\subsubsection"+"{"+e3.text()+"}");
//			System.out.println(e3.text());
		}		
	}
	
	private void reference(Element e) {
//		System.out.println("开始");
		Elements elist=e.select(">text|list-item");
		string.add("\\begin{thebibliography}{1}");
//		System.out.println(elist.size());
		for(Element item:elist) {
			if(item.select(">*").get(0).tagName().equals("text:p")) {
				string.add("\\bibitem{}"+item.text());
//				System.out.println(item.text());
			}				
		}
		string.add("\\end{thebibliography}");		
	};
	
	public void affiliation(Element e) {
		string.add("\\affil{"+e.text()+"}");
		mk=string.size();
//		string.add("\\begin{itemize}");
//		string.add("\\centering");		
//		string.add("\\item[]"+e.text());
//		string.add("\\end{itemize}");
	}
	
	public void picture(Element e) {
		Elements draws=e.select("draw|frame");
		for(Element draw:draws)	{
			String file0=draw.select(">draw|image").get(0).attr("xlink:href");
			String name=file0.substring(file0.indexOf("/")+1,file0.indexOf("."));
//			System.out.println(name);
//			System.out.println(file0);
			Path file=root.resolve(file0);
//			System.out.println(file.toString());
			if(Files.exists(file)) {
				string.add("\\begin{figure}[H]");
				string.add("\\centering");
				string.add("\\includegraphics[width=4cm]{"+file0+"}");
				string.add("\\label{fig:"+name+"}");
				string.add("\\end{figure}");				
			}			
		}		
		//图片标题未加
	}
	
	public void equation(Element e) throws IOException {
		Elements equations=e.select("draw|frame");
		for(Element draw:equations) {
			String folder=draw.select(">draw|object").get(0).attr("xlink:href").substring(2);
//			System.out.println(folder);
			Path file1=root.resolve(folder);
			Path file=file1.resolve("content.xml");
//			System.out.println(file.toString());
			
			if(Files.exists(file)) {
				Document object=Jsoup.parse(new File(file.toString()), "utf-8");
				Elements annotation=object.select("math>semantics>annotation");
				for(Element a:annotation) {
//					System.out.println(a.text());
//					Equation e1=new Equation(a.text());
//					e1.equation();
					Formule e2=new Formule(a.text());
					e2.build();
					string.add(e2.get());
				}
					
			}
				
			
		}			
	}
	
	
	
}
