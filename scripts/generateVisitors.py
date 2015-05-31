def printvisitor( type ):
    visitors = open("visitors.txt", 'a');
    visitorObjects = open("visitorObjects.txt", 'a');
    
    visitorObjects.write("new " + type + "Visitor().visit(cu, null);\n");
    
    visitors.write("private static class " + type + "Visitor extends VoidVisitorAdapter {\n")
    visitors.write("\n");
    visitors.write("  @Override\n")
    visitors.write("  public void visit(" + type + " n, Object arg) {\n")
    visitors.write("\n");
    visitors.write("      n.setComment(new LineComment(\"" + type + "\"));\n")
    visitors.write("\n");
    visitors.write("  }\n")
    visitors.write("\n");
    visitors.write("}\n")
    visitors.write("\n");

open("visitors.txt", 'w').close()
open("visitorObjects.txt", 'w').close()

with open("VoidVisitorAdapter.html") as html:
     htmlLines = html.readlines()
     
     for line in htmlLines:
         if "<pre" in line and "href" in line:
             printvisitor(line[(line.rfind("\">")+2):-(len(line)-line.find("</a>"))])
     