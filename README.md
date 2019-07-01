# 项目介绍
该项目为ISEN的校园项目，将odt文件转化为tex文件，并在https://www.overleaf.com/ 上验证
本人用java写的，因为这个比较熟悉

# 项目思路
- 首先对odt文件进行解压，会发现一堆xml文件和一些文件夹，文件夹包含图片和公式等xml
- 对content.xml文件进行解析，我用的是Jsoup，从头开始遍历循环每一个标签
- 对得到的标签，进行tex格式转化

# 项目中遇到的问题
因为odt文件是比较自由的，每一个标签都是自己定的，所以在解析的时候就出现了问题。
比如解析作者标签，法国人用的是auteur，英语用的是author，但不管是什么，这都是\\author{"+作者+"}"。
等等标签完全不一样，因为是在法国上的学，所以解析的都是法语

# 代码介绍
- MainActivity.java为主程序
- UnzipFile.java 解压odt
- FileReader.java  解析content.xml文件
- FileBuilder.java   导出为tex文件
- Formule.java  解析公式
- Test_equation.java 对公式的测试文件
